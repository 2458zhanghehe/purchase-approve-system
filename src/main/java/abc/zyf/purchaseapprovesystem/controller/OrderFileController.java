package abc.zyf.purchaseapprovesystem.controller;

import abc.zyf.purchaseapprovesystem.domain.Result;
import abc.zyf.purchaseapprovesystem.domain.entity.OrderFile;
import abc.zyf.purchaseapprovesystem.service.OrderFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/orderFile")
@Api("订单文件上传")
public class OrderFileController {

    @Autowired
    private OrderFileService orderFileService;


    @PostMapping("/upload/{orderId}")
    @ApiOperation("文件上传")
    public Result<OrderFile> uploadFile(MultipartFile file, @PathVariable Long orderId) {
        return Result.success(orderFileService.uploadFile(file,orderId));
    }
}
