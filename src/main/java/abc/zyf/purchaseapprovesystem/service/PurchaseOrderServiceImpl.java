package abc.zyf.purchaseapprovesystem.service;

import abc.zyf.purchaseapprovesystem.domain.entity.*;
import abc.zyf.purchaseapprovesystem.domain.vo.PurchaseOrderVO;
import abc.zyf.purchaseapprovesystem.mapper.*;
import abc.zyf.purchaseapprovesystem.utils.SnowflakeIdWorker;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder>
        implements PurchaseOrderService {

    @Autowired
    private PurchaseRequestMapper purchaseRequestMapper;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private PurchaseRequestDetailMapper purchaseRequestDetailMapper;

    @Autowired
    private PurchaseOrderDetailMapper purchaseOrderDetailMapper;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Override
    public PurchaseOrderVO generateOrder(Long requestId) {
        PurchaseRequest purchaseRequest = purchaseRequestMapper.selectById(requestId);
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setRequestId(requestId);
        purchaseOrder.setCreateTime(new Date());
        purchaseOrder.setStatus(1);
        purchaseOrder.setTotalAmount(purchaseRequest.getBudgetAmount());
        purchaseOrder.setId(snowflakeIdWorker.nextId());
        save(purchaseOrder);

        PurchaseOrderVO purchaseOrderVO = new PurchaseOrderVO();
        BeanUtils.copyProperties(purchaseOrder, purchaseOrderVO);

        List<PurchaseRequestDetail> requestDetails = purchaseRequestDetailMapper.selectList(new QueryWrapper<PurchaseRequestDetail>()
                .lambda().eq(PurchaseRequestDetail::getRequestId, purchaseRequest.getId()));

        List<PurchaseOrderDetail> details = new ArrayList<>();
        for(PurchaseRequestDetail requestDetail : requestDetails) {
            PurchaseOrderDetail orderDetail = new PurchaseOrderDetail();
            BeanUtils.copyProperties(requestDetail, orderDetail);
            orderDetail.setOrderId(purchaseOrder.getId());
            purchaseOrderDetailMapper.insert(orderDetail);
            details.add(orderDetail);
        }
        purchaseOrderVO.setDetails(details);
        return purchaseOrderVO;
    }

    @Autowired
    private PurchaseTypeMapper purchaseTypeMapper;

    @Override
    public void exportPurchaseSummary(HttpServletResponse response) {
        //获取各采购类型金额
        HashMap<Integer, Double> map = new HashMap<>();
        Double sum = 0.0;
        for(int type = 1; type <= 3; type++){
            //这种方法会select id，但是仍然会返回PurchaseRequest，只是里面其他字段都为null，只有id有值
//            List<PurchaseRequest> purchaseRequestList = purchaseRequestMapper.selectList(new QueryWrapper<PurchaseRequest>()
//                    .lambda().eq(PurchaseRequest::getStatus, 1).select(PurchaseRequest::getId));
            List<Long> requestIds = purchaseRequestMapper.selectObjs(new QueryWrapper<PurchaseRequest>()
                            .lambda().eq(PurchaseRequest::getPurchaseTypeId, type)
                            .select(PurchaseRequest::getId))
                    .stream()
                    .map(obj -> (Long) obj)
                    .collect(Collectors.toList());
            BigDecimal totalAmountType = purchaseOrderMapper.getTotalAmountByRequestIds(requestIds);
            map.put(type, totalAmountType.doubleValue());
            sum += totalAmountType.doubleValue();
        }
        for (int i = 1; i <= 3; i++) {
            map.put(i, map.get(i) == null ? 0.0 : map.get(i)/sum);
        }

        //List2Map
        Map<Integer, String> typeMap = purchaseTypeMapper.getAll().stream().collect(Collectors.toMap(PurchaseType::getId, PurchaseType::getName));


        try{
            //获取提供好的excel模板对象创建一个新的Excel表格对象
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("PurchaseSummaryTemplate.xlsx");
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = excel.getSheet("Sheet1");
            sheet.getRow(1).getCell(0).setCellValue(typeMap.get(1));
            sheet.getRow(1).getCell(1).setCellValue(map.get(1));
            sheet.getRow(2).getCell(0).setCellValue(typeMap.get(2));
            sheet.getRow(2).getCell(1).setCellValue(map.get(2));
            sheet.getRow(3).getCell(0).setCellValue(typeMap.get(3));
            sheet.getRow(3).getCell(1).setCellValue(map.get(3));

            //通过输出流将文件下载到客户端浏览器中
            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            excel.close();

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
