package abc.zyf.purchaseapprovesystem.mapper;

import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PurchaseTypeMapper {

    @Select("select * from purchase_type")
    List<PurchaseType> getAll();
}
