package abc.zyf.purchaseapprovesystem.controller;

import abc.zyf.purchaseapprovesystem.service.PurchaseOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/purchaseOrder")
@Api("采购订单相关接口")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;


    @GetMapping("/exportAmountpercentByType")
    @ApiOperation("获取各类型占比统计表格")
    public void getAmountpercentByType(HttpServletResponse response) {
        purchaseOrderService.exportPurchaseSummary(response);
    }

}
