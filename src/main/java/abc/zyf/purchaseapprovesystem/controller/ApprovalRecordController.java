package abc.zyf.purchaseapprovesystem.controller;

import abc.zyf.purchaseapprovesystem.domain.Result;
import abc.zyf.purchaseapprovesystem.domain.vo.ApprovalRecordVO;
import abc.zyf.purchaseapprovesystem.service.ApprovalRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api("审批记录")
@RequestMapping("/approvalRecord")
public class ApprovalRecordController {

    @Autowired
    private ApprovalRecordService approvalRecordService;

    @ApiOperation("根据审批人id查询本人负责的待审批任务")
    @ApiImplicitParam(name = "approverId", value = "审批人id", required = true, dataType = "Long")
    @GetMapping("/approvalList")
    public Result<List<ApprovalRecordVO>> approvalList(Long approverId) {
        return Result.success(approvalRecordService.getListByApproverId(approverId));
    }
}
