package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.constant.ApprovalResultConstant;
import abc.zyf.purchaseapprovesystem.domain.entity.ApprovalRecord;
import abc.zyf.purchaseapprovesystem.domain.entity.ApprovalRecordDetail;
import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseRequest;
import abc.zyf.purchaseapprovesystem.domain.vo.ApprovalRecordVO;
import abc.zyf.purchaseapprovesystem.domain.vo.PurchaseRequestVO;
import abc.zyf.purchaseapprovesystem.mapper.ApprovalRecordDetailMapper;
import abc.zyf.purchaseapprovesystem.mapper.ApprovalRecordMapper;
import abc.zyf.purchaseapprovesystem.mapper.PurchaseRequestMapper;
import abc.zyf.purchaseapprovesystem.utils.SnowflakeIdWorker;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ApprovalRecordServiceImpl extends ServiceImpl<ApprovalRecordMapper, ApprovalRecord>
        implements ApprovalRecordService {

    @Autowired
    private ApprovalRecordMapper approvalRecordMapper;

    @Autowired
    private ApprovalRecordDetailMapper approvalRecordDetailMapper;

    @Autowired
    private PurchaseRequestService purchaseRequestService;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private PurchaseRequestMapper purchaseRequestMapper;

    @Override
    public List<ApprovalRecordVO> getListByApproverId(Long approverId) {
        //1根据id查需要审批的表 这里假设approvalStep就是approverId
        List<ApprovalRecord> approvalRecords = approvalRecordMapper.selectList(new QueryWrapper<ApprovalRecord>().lambda()
                .eq(ApprovalRecord::getApprovalStep, approverId));

        List<ApprovalRecordVO> approvalRecordVOList = new ArrayList<>();

        for(ApprovalRecord approvalRecord : approvalRecords){
            ApprovalRecordVO approvalRecordVO = new ApprovalRecordVO();
            BeanUtils.copyProperties(approvalRecord, approvalRecordVO);
            //2根据审批记录id查明细
            List<ApprovalRecordDetail> approvalRecordDetails = approvalRecordDetailMapper.selectList(new QueryWrapper<ApprovalRecordDetail>().lambda()
                    .eq(ApprovalRecordDetail::getApprovalId, approvalRecord.getId()));
            approvalRecordVO.setDetails(approvalRecordDetails);
            //3查询采购申请信息
            PurchaseRequestVO purchaseRequestVO = purchaseRequestService.getByIdWithDetails(approvalRecord.getRequestId());
            approvalRecordVO.setPurchaseRequest(purchaseRequestVO);
            approvalRecordVOList.add(approvalRecordVO);
        }
        return approvalRecordVOList;
    }

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    //审批
    @Override
    @Transactional
    public ApprovalRecordDetail approve(ApprovalRecordDetail approvalRecordDetail) {
        approvalRecordDetail.setId(snowflakeIdWorker.nextId());
        approvalRecordDetail.setApprovalTime(new Date());
        //更新采购表状态
        approvalRecordDetailMapper.insert(approvalRecordDetail);
        ApprovalRecord approvalRecord = approvalRecordMapper.selectOne(new QueryWrapper<ApprovalRecord>().lambda()
                .eq(ApprovalRecord::getApprovalStep, approvalRecordDetail.getId()));
        UpdateWrapper<PurchaseRequest> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(PurchaseRequest::getId, approvalRecord.getRequestId());
        if(approvalRecordDetail.getApprovalResult() == ApprovalResultConstant.PASS){
            //总经理审批通过，设置采购申请状态为已完成
            if(approvalRecordDetail.getApproverId() == 3){
                updateWrapper.lambda().set(PurchaseRequest::getStatus, 3);
                //采购申请审批通过后，系统自动生成采购订单
                purchaseOrderService.generateOrder(approvalRecord.getRequestId());
            }else {
                //其他角色通过，设置采购申请状态为审批中
                updateWrapper.lambda().set(PurchaseRequest::getStatus, 2);
            }
        }else if(approvalRecordDetail.getApprovalResult() == ApprovalResultConstant.REJECT){
            //设置采购申请状态为已驳回
            updateWrapper.lambda().set(PurchaseRequest::getStatus, 4);
        }
        purchaseRequestMapper.update(null, updateWrapper);
        return approvalRecordDetail;
    }
}
