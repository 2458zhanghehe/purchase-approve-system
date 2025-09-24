package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseOrder;
import abc.zyf.purchaseapprovesystem.domain.vo.PurchaseOrderVO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

public interface PurchaseOrderService extends IService<PurchaseOrder> {
    PurchaseOrderVO generateOrder(Long requestId);

    void exportPurchaseSummary(HttpServletResponse response);
}
