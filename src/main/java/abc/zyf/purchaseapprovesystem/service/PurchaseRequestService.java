package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.dto.PurchaseRequestDTO;
import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseRequest;
import abc.zyf.purchaseapprovesystem.domain.vo.PurchaseRequestVO;
import com.baomidou.mybatisplus.extension.service.IService;


public interface PurchaseRequestService extends IService<PurchaseRequest> {

    PurchaseRequestVO insertPurchaseRequest(PurchaseRequestDTO purchaseRequestDTO);
}
