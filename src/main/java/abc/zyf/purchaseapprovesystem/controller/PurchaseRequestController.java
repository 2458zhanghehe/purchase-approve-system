package abc.zyf.purchaseapprovesystem.controller;


import abc.zyf.purchaseapprovesystem.domain.Result;
import abc.zyf.purchaseapprovesystem.domain.dto.PurchaseRequestDTO;
import abc.zyf.purchaseapprovesystem.domain.vo.PurchaseRequestVO;
import abc.zyf.purchaseapprovesystem.service.PurchaseRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    public Result<PurchaseRequestVO> insert(PurchaseRequestDTO purchaseRequestDTO) {
        return Result.success(purchaseRequestService.insertPurchaseRequest(purchaseRequestDTO));
    }


    @GetMapping("/test")
    public String test(){return "hello";}





}
