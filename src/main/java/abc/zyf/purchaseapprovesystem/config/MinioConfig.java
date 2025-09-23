package abc.zyf.purchaseapprovesystem.config;


import abc.zyf.purchaseapprovesystem.utils.MinioTemplate;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//启用指定的@ConfigurationProperties配置类，并将其注册为Spring Bean
@EnableConfigurationProperties(MinioConfigProperties.class)
public class MinioConfig {

    @Bean
    public MinioClient minioClient(MinioConfigProperties minioConfigProperties) {
        return MinioClient.builder()
                .endpoint(minioConfigProperties.getEndpoint())
                .credentials(minioConfigProperties.getAccessKey(), minioConfigProperties.getSecretKey())
                .build();
    }


    @Bean
    public MinioTemplate getTemplate(MinioClient minioClient, MinioConfigProperties minioConfigProperties) {
        return new MinioTemplate(minioConfigProperties, minioClient);
    }
}
