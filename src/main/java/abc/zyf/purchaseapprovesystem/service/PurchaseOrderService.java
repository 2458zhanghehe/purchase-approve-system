package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseOrder;
import abc.zyf.purchaseapprovesystem.domain.vo.PurchaseOrderVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PurchaseOrderService extends IService<PurchaseOrder> {
    public PurchaseOrderVO generateOrder(Long requestId);
}
