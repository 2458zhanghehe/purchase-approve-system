package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.dto.PurchaseRequestDTO;
import abc.zyf.purchaseapprovesystem.domain.dto.SearchPurchaseRequestDTO;
import abc.zyf.purchaseapprovesystem.domain.entity.ApprovalRecord;
import abc.zyf.purchaseapprovesystem.domain.entity.ApprovalRecordDetail;
import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseRequest;
import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseRequestDetail;
import abc.zyf.purchaseapprovesystem.domain.vo.PurchaseRequestVO;
import abc.zyf.purchaseapprovesystem.mapper.ApprovalRecordDetailMapper;
import abc.zyf.purchaseapprovesystem.mapper.ApprovalRecordMapper;
import abc.zyf.purchaseapprovesystem.mapper.PurchaseRequestDetailMapper;
import abc.zyf.purchaseapprovesystem.mapper.PurchaseRequestMapper;
import abc.zyf.purchaseapprovesystem.utils.SnowflakeIdWorker;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseRequestServiceImpl extends ServiceImpl<PurchaseRequestMapper, PurchaseRequest> implements PurchaseRequestService {


    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private PurchaseRequestDetailMapper purchaseRequestDetailMapper;

    @Autowired
    private ApprovalRecordMapper approvalRecordMapper;

    @Autowired
    private ApprovalRecordDetailMapper approvalRecordDetailMapper;



    @Override
    public PurchaseRequestVO insertPurchaseRequest(PurchaseRequestDTO purchaseRequestDTO) {

        //1.插入主表
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        BeanUtils.copyProperties(purchaseRequestDTO, purchaseRequest);
        purchaseRequest.setCreateTime(new Date());
        purchaseRequest.setStatus(1); //1表示待审批
        purchaseRequest.setId(snowflakeIdWorker.nextId());
        save(purchaseRequest);

        //2.插入采购申请明细表
        for(PurchaseRequestDetail detail : purchaseRequestDTO.getDetails()) {
            detail.setRequestId(purchaseRequest.getId());
            detail.setId(snowflakeIdWorker.nextId());
            purchaseRequestDetailMapper.insert(detail);
        }

        //3.保存审批记录
        ApprovalRecord approvalRecord = new ApprovalRecord();
        approvalRecord.setId(snowflakeIdWorker.nextId());
        approvalRecord.setRequestId(purchaseRequest.getId());
        approvalRecord.setApprovalStep(1); //1部门经理审批  2财务审批  3总经理审批

        PurchaseRequestVO purchaseRequestVO = new PurchaseRequestVO();
        BeanUtils.copyProperties(purchaseRequest, purchaseRequestVO);
        return purchaseRequestVO;
    }

    @Override
    public void updatePurchaseRequest(PurchaseRequestDTO purchaseRequestDTO) {
        //1.修改主表
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        BeanUtils.copyProperties(purchaseRequestDTO, purchaseRequest);
        purchaseRequest.setUpdateTime(new Date());
        updateById(purchaseRequest);

        //2.修改明细
        for(PurchaseRequestDetail detail : purchaseRequestDTO.getDetails()) {
            if(detail.getId() == null) {
                detail.setId(snowflakeIdWorker.nextId());
                detail.setRequestId(purchaseRequest.getId());
                purchaseRequestDetailMapper.insert(detail);
            } else {
                purchaseRequestDetailMapper.updateById(detail);
            }
        }

        //3.删除审批记录明细，重新开始审批
        approvalRecordMapper.update(null, new UpdateWrapper<ApprovalRecord>().lambda()
                .eq(ApprovalRecord::getRequestId, purchaseRequest.getId())
                .set(ApprovalRecord::getApprovalStep, 1));
        //获取id
        Long approvalRecordId = approvalRecordMapper.selectOne(new QueryWrapper<ApprovalRecord>().lambda()
                .eq(ApprovalRecord::getRequestId, purchaseRequest.getId())).getId();
        approvalRecordDetailMapper.delete(new QueryWrapper<ApprovalRecordDetail>().lambda()
                .eq(ApprovalRecordDetail::getApprovalId, approvalRecordId));

    }

    @Override
    public void deletePurchaseRequest(Long id) {
        //1.删除主表
        removeById(id);
        //2.删除明细
        purchaseRequestDetailMapper.delete(new QueryWrapper<PurchaseRequestDetail>().lambda()
                .eq(PurchaseRequestDetail::getId, id));
        //3.删除审批记录
        ApprovalRecord approvalRecord = approvalRecordMapper.selectOne(new QueryWrapper<ApprovalRecord>().lambda()
                .eq(ApprovalRecord::getRequestId, id));
        if(approvalRecord == null){
            return;
        }
        approvalRecordDetailMapper.delete(new QueryWrapper<ApprovalRecordDetail>().lambda()
                .eq(ApprovalRecordDetail::getApprovalId, approvalRecord.getId()));
        approvalRecordMapper.deleteById(approvalRecord.getId());
    }

    @Override
    public List<PurchaseRequestVO> pageQuery(SearchPurchaseRequestDTO searchPurchaseRequestDTO) {

        //查询采购主表
        QueryWrapper<PurchaseRequest> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(searchPurchaseRequestDTO.getApplicantName())){
            queryWrapper.lambda().like(PurchaseRequest::getApplicantName, searchPurchaseRequestDTO.getApplicantName());
        }
        if(searchPurchaseRequestDTO.getPurchaseTypeId() != null){
            queryWrapper.lambda().eq(PurchaseRequest::getPurchaseTypeId, searchPurchaseRequestDTO.getPurchaseTypeId());
        }
        if(searchPurchaseRequestDTO.getStartDate() != null){
            queryWrapper.lambda().gt(PurchaseRequest::getCreateTime, searchPurchaseRequestDTO.getStartDate());
        }
        if(searchPurchaseRequestDTO.getEndDate() != null){
            queryWrapper.lambda().lt(PurchaseRequest::getCreateTime, searchPurchaseRequestDTO.getEndDate());
        }
        //构建分页器
        Page<PurchaseRequest> page = new Page<>(searchPurchaseRequestDTO.getPageNum(), searchPurchaseRequestDTO.getPageSize());
        //执行分页查询
        IPage<PurchaseRequest> ipage = page(page, queryWrapper);
        if(page.getSize() == 0){
            //抛出异常
            return null;
        }
        List<PurchaseRequest> purchaseRequestList = ipage.getRecords();
        List<PurchaseRequestVO> purchaseRequestVOList = new ArrayList<>();
        //查询采购明细
        for(PurchaseRequest request : purchaseRequestList) {
            PurchaseRequestVO purchaseRequestVO = new PurchaseRequestVO();
            BeanUtils.copyProperties(request, purchaseRequestVO);
            List<PurchaseRequestDetail> details = purchaseRequestDetailMapper.selectList(new QueryWrapper<PurchaseRequestDetail>().lambda()
                    .eq(PurchaseRequestDetail::getRequestId, request.getId()));
            purchaseRequestVO.setDetails(details);
            purchaseRequestVOList.add(purchaseRequestVO);
        }
        return purchaseRequestVOList;
    }

    @Override
    public List<PurchaseRequestVO> getListByApplicantId(Long applicantId) {
        //查询主表
        List<PurchaseRequest> purchaseRequestList = list(new QueryWrapper<PurchaseRequest>().lambda().eq(PurchaseRequest::getApplicantId, applicantId));
        //查询明细
        List<PurchaseRequestVO> purchaseRequestVOList = new ArrayList<>();
        for(PurchaseRequest request : purchaseRequestList) {
            PurchaseRequestVO purchaseRequestVO = new PurchaseRequestVO();
            BeanUtils.copyProperties(request, purchaseRequestVO);
            List<PurchaseRequestDetail> details = purchaseRequestDetailMapper.selectList(new QueryWrapper<PurchaseRequestDetail>().lambda()
                    .eq(PurchaseRequestDetail::getRequestId, request.getId()));
            purchaseRequestVO.setDetails(details);
            purchaseRequestVOList.add(purchaseRequestVO);
        }
        return purchaseRequestVOList;
    }

    @Override
    public PurchaseRequestVO getByIdWithDetails(Long id) {
        PurchaseRequest purchaseRequest = getById(id);
        if(purchaseRequest == null){
            return null;
        }
        PurchaseRequestVO purchaseRequestVO = new PurchaseRequestVO();
        BeanUtils.copyProperties(purchaseRequest, purchaseRequestVO);
        List<PurchaseRequestDetail> details = purchaseRequestDetailMapper.selectList(new QueryWrapper<PurchaseRequestDetail>().lambda()
                .eq(PurchaseRequestDetail::getRequestId, purchaseRequest.getId()));
        purchaseRequestVO.setDetails(details);
        return purchaseRequestVO;
    }
}
