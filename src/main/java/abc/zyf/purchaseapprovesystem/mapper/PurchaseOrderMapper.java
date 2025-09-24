package abc.zyf.purchaseapprovesystem.mapper;

import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
    BigDecimal getTotalAmountByRequestIds(@Param("requestIds") List<Long> requestIds);

    BigDecimal getTotalAmountDaily(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
