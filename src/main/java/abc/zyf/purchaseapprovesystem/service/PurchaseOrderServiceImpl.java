package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseOrder;
import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseOrderDetail;
import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseRequest;
import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseRequestDetail;
import abc.zyf.purchaseapprovesystem.domain.vo.PurchaseOrderVO;
import abc.zyf.purchaseapprovesystem.mapper.PurchaseOrderDetailMapper;
import abc.zyf.purchaseapprovesystem.mapper.PurchaseOrderMapper;
import abc.zyf.purchaseapprovesystem.mapper.PurchaseRequestDetailMapper;
import abc.zyf.purchaseapprovesystem.mapper.PurchaseRequestMapper;
import abc.zyf.purchaseapprovesystem.utils.SnowflakeIdWorker;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder>
        implements PurchaseOrderService {

    @Autowired
    private PurchaseRequestMapper purchaseRequestMapper;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private PurchaseRequestDetailMapper purchaseRequestDetailMapper;

    @Autowired
    private PurchaseOrderDetailMapper purchaseOrderDetailMapper;

    @Override
    public PurchaseOrderVO generateOrder(Long requestId) {
        PurchaseRequest purchaseRequest = purchaseRequestMapper.selectById(requestId);
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setRequestId(requestId);
        purchaseOrder.setCreateTime(new Date());
        purchaseOrder.setStatus(1);
        purchaseOrder.setTotalAmount(purchaseRequest.getBudgetAmount());
        purchaseOrder.setId(snowflakeIdWorker.nextId());
        save(purchaseOrder);

        PurchaseOrderVO purchaseOrderVO = new PurchaseOrderVO();
        BeanUtils.copyProperties(purchaseOrder, purchaseOrderVO);


        List<PurchaseRequestDetail> requestDetails = purchaseRequestDetailMapper.selectList(new QueryWrapper<PurchaseRequestDetail>()
                .lambda().eq(PurchaseRequestDetail::getRequestId, purchaseRequest.getId()));

        List<PurchaseOrderDetail> details = new ArrayList<>();
        for(PurchaseRequestDetail requestDetail : requestDetails) {
            PurchaseOrderDetail orderDetail = new PurchaseOrderDetail();
            BeanUtils.copyProperties(requestDetail, orderDetail);
            orderDetail.setOrderId(purchaseOrder.getId());
            purchaseOrderDetailMapper.insert(orderDetail);
            details.add(orderDetail);
        }
        purchaseOrderVO.setDetails(details);
        return purchaseOrderVO;
    }
}
