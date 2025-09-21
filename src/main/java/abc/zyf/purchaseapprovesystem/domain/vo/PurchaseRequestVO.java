package abc.zyf.purchaseapprovesystem.domain.vo;

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
public class PurchaseRequestVO {

    private Long id;


    private Long applicantId;


    private String applicantName;


    private Integer purchaseTypeId;


    private BigDecimal budgetAmount;

    //明细
    private List<PurchaseRequestDetail> details;

    private Integer status;


    private Date createTime;


    private String remark;


    private Date applyTime;
}
