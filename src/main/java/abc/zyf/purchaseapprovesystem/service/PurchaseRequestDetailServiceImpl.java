package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseRequestDetail;
import abc.zyf.purchaseapprovesystem.mapper.PurchaseRequestDetailMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PurchaseRequestDetailServiceImpl extends ServiceImpl<PurchaseRequestDetailMapper,
        PurchaseRequestDetail> implements PurchaseRequestDetailService{
}
