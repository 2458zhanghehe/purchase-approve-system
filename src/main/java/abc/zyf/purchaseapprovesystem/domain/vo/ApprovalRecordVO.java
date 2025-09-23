package abc.zyf.purchaseapprovesystem.domain.vo;


import abc.zyf.purchaseapprovesystem.domain.entity.ApprovalRecordDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApprovalRecordVO {

    private Long id;

    private Long requestId;

    private Integer approvalStep;

    private List<ApprovalRecordDetail> details;

    private PurchaseRequestVO purchaseRequest;
}
