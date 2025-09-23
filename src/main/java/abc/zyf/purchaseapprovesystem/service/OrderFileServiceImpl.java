package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.entity.OrderFile;
import abc.zyf.purchaseapprovesystem.mapper.OrderFileMapper;
import abc.zyf.purchaseapprovesystem.utils.MinioTemplate;
import abc.zyf.purchaseapprovesystem.utils.SnowflakeIdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class OrderFileServiceImpl extends ServiceImpl<OrderFileMapper, OrderFile> implements OrderFileService {

    @Autowired
    private MinioTemplate minioTemplate;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;


    @Override
    public OrderFile uploadFile(MultipartFile multipartFile, Long orderId) {
        //1.检查参数
        if(multipartFile == null || multipartFile.getSize() == 0){
            //抛出异常
            return null;
        }
        //2.上传文件到MinIO服务器
        Long fileId = snowflakeIdWorker.nextId();
        String originalFilename = multipartFile.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String urlPath = null;
        try{
            if("pdf".equals(postfix)){
                urlPath = minioTemplate.uploadPDFFile("order",
                        fileId + postfix, multipartFile.getInputStream());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //3.保存文件信息到数据库
        OrderFile orderFile = new OrderFile();
        orderFile.setId(fileId);
        orderFile.setUploadTime(new Date());
        orderFile.setFileUrl(urlPath);
        orderFile.setFileType(postfix.equals("pdf")?1:2);
        orderFile.setOrderId(orderId);
        save(orderFile);

        return orderFile;
    }
}
