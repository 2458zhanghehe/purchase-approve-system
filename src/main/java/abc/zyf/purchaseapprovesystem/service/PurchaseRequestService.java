package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.dto.PurchaseRequestDTO;
import abc.zyf.purchaseapprovesystem.domain.dto.SearchPurchaseRequestDTO;
import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseRequest;
import abc.zyf.purchaseapprovesystem.domain.vo.PurchaseRequestVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface PurchaseRequestService extends IService<PurchaseRequest> {

    PurchaseRequestVO insertPurchaseRequest(PurchaseRequestDTO purchaseRequestDTO);

    void updatePurchaseRequest(PurchaseRequestDTO purchaseRequestDTO);

    void deletePurchaseRequest(Long id);

    List<PurchaseRequestVO> pageQuery(SearchPurchaseRequestDTO searchPurchaseRequestDTO);

    List<PurchaseRequestVO> getListByApplicantId(Long applicantId);

    PurchaseRequestVO getByIdWithDetails(Long id);
}
