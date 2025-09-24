package abc.zyf.purchaseapprovesystem;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;

public class POITest {

    @Test
    public void write() throws Exception{
        //在内存中创建一个Excel文件
        XSSFWorkbook excel = new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet("purchase");
        XSSFRow row1 = sheet.createRow(0);
        row1.createCell(1).setCellValue("项目");
        row1.createCell(2).setCellValue("金额");

        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(1).setCellValue("电脑");
        row2.createCell(2).setCellValue("10000");

        FileOutputStream outputStream = new FileOutputStream(new File("purchase.xls"));
        excel.write(outputStream);
        outputStream.flush();
        outputStream.close();
        excel.close();
    }
}
