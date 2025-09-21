package abc.zyf.purchaseapprovesystem.controller;


import abc.zyf.purchaseapprovesystem.domain.Result;
import abc.zyf.purchaseapprovesystem.domain.dto.PurchaseRequestDTO;
import abc.zyf.purchaseapprovesystem.domain.dto.SearchPurchaseRequestDTO;
import abc.zyf.purchaseapprovesystem.domain.vo.PurchaseRequestVO;
import abc.zyf.purchaseapprovesystem.service.PurchaseRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api("采购申请接口")
@RestController
@RequestMapping("/purchaseRequest")
public class PurchaseRequestController {

    @Autowired
    private PurchaseRequestService purchaseRequestService;

    @ApiOperation("新增采购申请")
    @ApiImplicitParam(name = "purchaseRequestDTO", value = "采购申请信息",
            required = true, dataType = "PurchaseRequestDTO", paramType = "query")
    @PostMapping("/insert")
    public Result<PurchaseRequestVO> insert(@RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        return Result.success(purchaseRequestService.insertPurchaseRequest(purchaseRequestDTO));
    }


    @ApiOperation("修改采购申请")
    @ApiImplicitParam(name = "purchaseRequestDTO", value = "采购申请信息",
            required = true, dataType = "PurchaseRequestDTO", paramType = "query")
    @PutMapping("/update")
    public void update(@RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        purchaseRequestService.updatePurchaseRequest(purchaseRequestDTO);
    }


    @ApiOperation("删除采购申请")
    @ApiImplicitParam(name = "id", value = "采购申请id",
            required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        purchaseRequestService.deletePurchaseRequest(id);
    }


    @ApiOperation("查询采购申请列表分页条件查询")
    @ApiImplicitParam(name = "searchPurchaseRequestDTO", value = "采购申请查询信息",
            required = true, dataType = "SearchPurchaseRequestDTO", paramType = "query")
    @PostMapping("/pageQuery")
    public Result<List<PurchaseRequestVO>> pageQuery(@RequestBody SearchPurchaseRequestDTO searchPurchaseRequestDTO) {
        return Result.success(purchaseRequestService.pageQuery(searchPurchaseRequestDTO));
    }


    @ApiOperation("查询列表")
    @ApiImplicitParam(name = "applicantId", value = "申请人id",
            required = true, dataType = "Long", paramType = "query")
    @GetMapping("/list")
    public Result<List<PurchaseRequestVO>> list(Long applicantId) {
        return Result.success(purchaseRequestService.getListByApplicantId(applicantId));
    }
}
