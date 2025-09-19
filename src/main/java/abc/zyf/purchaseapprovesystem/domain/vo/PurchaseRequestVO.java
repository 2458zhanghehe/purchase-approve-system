package abc.zyf.purchaseapprovesystem.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequestVO {

    private Long id;


    private Long applicantId;


    private String applicantName;


    private Integer purchaseTypeId;


    private BigDecimal budgetAmount;


    private Integer status;


    private Date createTime;


    private String remark;


    private Date applyTime;
}
