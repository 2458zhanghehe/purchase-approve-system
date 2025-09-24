package abc.zyf.purchaseapprovesystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.Date;

@SpringBootTest
class PurchaseApproveApplicationTests {

    @Test
    void contextLoads() {
        Date date = new Date();
        System.out.println(date);

        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
    }

}
