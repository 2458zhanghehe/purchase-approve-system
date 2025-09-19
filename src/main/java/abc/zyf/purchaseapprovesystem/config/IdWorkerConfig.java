package abc.zyf.purchaseapprovesystem.config;

import abc.zyf.purchaseapprovesystem.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdWorkerConfig {

    @Value("${snowflake.worker-id}")
    private long workerId;
    @Value("${snowflake.datacenter-id}")
    private long datacenterId;

    @Bean
    public SnowflakeIdWorker getSnowflakeIdWorker() {
        return new SnowflakeIdWorker(workerId, datacenterId);
    }
}
