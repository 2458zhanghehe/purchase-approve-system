package abc.zyf.purchaseapprovesystem.mapper;

import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper  //在配置文件或者启动类配置了MapperScan时，这里可以不用写注解
public interface PurchaseRequestMapper extends BaseMapper<PurchaseRequest> {
}
