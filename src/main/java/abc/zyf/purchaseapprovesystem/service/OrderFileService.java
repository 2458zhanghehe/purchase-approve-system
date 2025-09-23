package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.entity.OrderFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

public interface OrderFileService extends IService<OrderFile> {

    OrderFile uploadFile(MultipartFile file, Long userId);

}
