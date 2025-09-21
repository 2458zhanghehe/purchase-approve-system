package abc.zyf.purchaseapprovesystem.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


//分页条件查询
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchPurchaseRequestDTO {
    private String applicantName;

    private Integer purchaseTypeId;   //1表示办公用品 2表示IT设备  3表示市场活动

    private Date startDate;

    private Date endDate;

    private int pageNum;

    private int pageSize;
}
