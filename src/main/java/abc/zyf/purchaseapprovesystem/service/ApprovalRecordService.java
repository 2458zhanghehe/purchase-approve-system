package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.entity.ApprovalRecord;
import abc.zyf.purchaseapprovesystem.domain.entity.ApprovalRecordDetail;
import abc.zyf.purchaseapprovesystem.domain.vo.ApprovalRecordVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ApprovalRecordService extends IService<ApprovalRecord> {

    List<ApprovalRecordVO> getListByApproverId(Long approverId);

    ApprovalRecordDetail approve(ApprovalRecordDetail approvalRecordDetail);
}
