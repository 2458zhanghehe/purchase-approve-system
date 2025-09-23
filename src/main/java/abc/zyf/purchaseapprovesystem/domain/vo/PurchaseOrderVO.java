package abc.zyf.purchaseapprovesystem.domain.vo;

import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseOrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseOrderVO {

    private Long id;


    private Long requestId;


    private BigDecimal totalAmount;


    private Integer status;  //1表示已创建 2表示已支付 3表示已收货


    private Date createTime;

    private List<PurchaseOrderDetail> details;

}
