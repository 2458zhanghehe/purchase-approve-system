package abc.zyf.purchaseapprovesystem.job;

import abc.zyf.purchaseapprovesystem.mapper.PurchaseOrderMapper;
import io.swagger.models.auth.In;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

//这个任务类如果比较简单不需要注入其他Bean，可以不加这个注解
@Component
public class SummuryJob extends QuartzJobBean {

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //统计当天采购总额
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime start = localDateTime.toLocalDate().atStartOfDay();
        LocalDateTime end = localDateTime.toLocalDate().atTime(23, 59, 59);
        Date startDate = Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
        BigDecimal totalAmountDaily = purchaseOrderMapper.getTotalAmountDaily(startDate, endDate);
        LocalDate today = LocalDate.now(); //精确到日

        //写入统计表
        //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("PurchaseSummary.xls");
        File file = new File("src\\main\\resources\\PurchaseSummary.xls");

        try{
            InputStream inputStream = new FileInputStream(file);
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = excel.getSheet("Sheet1");
            int lastRowNum = sheet.getLastRowNum();
            XSSFRow row = sheet.getRow(lastRowNum);
            row.getCell(0).setCellValue(today.toString());
            row.getCell(1).setCellValue(totalAmountDaily.toString());
            //保存
            inputStream.close();
            OutputStream outputStream = new FileOutputStream(file);
            excel.write(outputStream);
            outputStream.flush();
            outputStream.close();
            excel.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
