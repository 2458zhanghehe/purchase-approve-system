package abc.zyf.purchaseapprovesystem.domain.dto;

import abc.zyf.purchaseapprovesystem.domain.entity.PurchaseRequestDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequestDTO {

    private Long id;

    private Long applicantId;

    private String applicantName;

    private Integer purchaseTypeId;   //1表示办公用品 2表示IT设备  3表示市场活动

    private BigDecimal budgetAmount;

    private List<PurchaseRequestDetail> details;

    //备注说明
    private String remark;

    private Date applyTime;
}
