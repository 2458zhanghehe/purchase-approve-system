package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.dto.PurchaseRequestDTO;
import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseRequest;
import abc.zyf.purchaseapprovesystem.domain.vo.PurchaseRequestVO;
import abc.zyf.purchaseapprovesystem.mapper.PurchaseRequestMapper;
import abc.zyf.purchaseapprovesystem.utils.SnowflakeIdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PurchaseRequestServiceImpl extends ServiceImpl<PurchaseRequestMapper, PurchaseRequest> implements PurchaseRequestService {


    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public PurchaseRequestVO insertPurchaseRequest(PurchaseRequestDTO purchaseRequestDTO) {

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        BeanUtils.copyProperties(purchaseRequestDTO, purchaseRequest);
        purchaseRequest.setCreateTime(new Date());
        purchaseRequest.setId(snowflakeIdWorker.nextId());
        save(purchaseRequest);

        PurchaseRequestVO purchaseRequestVO = new PurchaseRequestVO();
        BeanUtils.copyProperties(purchaseRequest, purchaseRequestVO);
        return purchaseRequestVO;
    }
}
