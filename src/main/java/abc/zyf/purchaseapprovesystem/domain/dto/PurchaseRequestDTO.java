package abc.zyf.purchaseapprovesystem.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequestDTO {

    private Long applicantId;

    private String applicantName;

    private Integer purchaseTypeId;   //1表示办公用品 2表示IT设备  3表示市场活动

    private BigDecimal budgetAmount;

    private Integer status;   //1表示待审批 2表示审批中 3表示已通过 4表示已驳回

    //备注说明
    private String remark;

    private Date applyTime;
}
