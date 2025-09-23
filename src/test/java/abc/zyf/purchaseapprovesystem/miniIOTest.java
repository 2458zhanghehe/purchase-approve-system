package abc.zyf.purchaseapprovesystem;


import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

public class miniIOTest {

    @Test
    public void testUpload() {
        try{
            FileInputStream fileInputStream = new FileInputStream("E:\\yan\\requirements.txt");
            MinioClient minioClient = MinioClient.builder().credentials("minioadmin", "minioadmin")
                    .endpoint("http://localhost:9005").build();

            PutObjectArgs putObjectArgs = PutObjectArgs.builder().object("requirements.txt")
                    .bucket("purchaseapprove")
                    .stream(fileInputStream, fileInputStream.available(), -1)
                    .contentType("text/plain")
                    .build();
            minioClient.putObject(putObjectArgs);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
