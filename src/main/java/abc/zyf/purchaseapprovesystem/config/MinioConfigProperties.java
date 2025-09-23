package abc.zyf.purchaseapprovesystem.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ConfigurationProperties(prefix = "minio")
public class MinioConfigProperties implements Serializable {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String readPath;
}
