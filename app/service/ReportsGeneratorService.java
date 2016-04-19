package service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import exception.ServiceException;
import models.FinancialHighlights;
import models.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.MoneyRepository;

import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dmitriy on 9.4.16.
 */
public class ReportsGeneratorService {
    private static final Logger.ALogger LOGGER = Logger.of(ReportsGeneratorService.class);

    @Inject
    MoneyRepository moneyRepository;


    public InputStream generateExcelReport(Date minDate, Date maxDate) throws ServiceException {
        LOGGER.debug("Get excel report: {}, {}", minDate, maxDate);
        try {
            return JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                List<FinancialHighlights> financialHighlightsList = moneyRepository.getFinancialHighlights(minDate, maxDate, user.company.id);
                return getExcelReport(financialHighlightsList);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get excel report error: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public InputStream generatePdfReport(Date minDate, Date maxDate) throws ServiceException {
        LOGGER.debug("Get pdf report: {}, {}", minDate, maxDate);
        try {
            return JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                List<FinancialHighlights> financialHighlightsList = moneyRepository.getFinancialHighlights(minDate, maxDate, user.company.id);
                return getPdfReport(financialHighlightsList);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get pdf report error: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public InputStream generateCsvReport(Date minDate, Date maxDate) throws ServiceException {
        LOGGER.debug("Get csv report: {}, {}", minDate, maxDate);
        try {
            return JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                List<FinancialHighlights> financialHighlightsList = moneyRepository.getFinancialHighlights(minDate, maxDate, user.company.id);
                return getCsvReport(financialHighlightsList);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get csv report error: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }


    private InputStream getExcelReport(List<FinancialHighlights> financialHighlightsList) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");
        Row head = sheet.createRow(0);
        Cell headCell = head.createCell(0);
        headCell.setCellValue("Date");
        Cell headCell1 = head.createCell(1);
        headCell1.setCellValue("Transportation Income");
        Cell headCell2 = head.createCell(2);
        headCell2.setCellValue("Vehicle Fuel Loss");
        Cell headCell3 = head.createCell(3);
        headCell3.setCellValue("Products Loss");
        Cell headCell4 = head.createCell(4);
        headCell4.setCellValue("Profit");
        Cell headCell5 = head.createCell(5);
        headCell5.setCellValue("Driver");
        int rowCount = 0;
        for (FinancialHighlights financialHighlights : financialHighlightsList) {
            Row row = sheet.createRow(++rowCount);
            Cell cell = row.createCell(0);
            cell.setCellValue(financialHighlights.deliveredDate.toString());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(financialHighlights.transportationIncome);
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(financialHighlights.vehicleFuelLoss);
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(financialHighlights.productsLoss);
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(financialHighlights.profit);
            Cell cell5 = row.createCell(5);
            User driver = financialHighlights.waybillVehicleDriver.driver;
            cell5.setCellValue(driver.surname + " " + driver.name + " " + driver.patronymic);
        }
        for (int i = 0; i < 6; i++)
            sheet.autoSizeColumn(i);
        ByteArrayOutputStream reportOutputStream = new ByteArrayOutputStream();
        workbook.write(reportOutputStream);
        reportOutputStream.flush();
        ByteArrayInputStream reportInputStream = new ByteArrayInputStream(reportOutputStream.toByteArray());
        return reportInputStream;
    }

    private InputStream getPdfReport(List<FinancialHighlights> financialHighlightsList) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4, 0f, 0f, 30f, 0f);
        ByteArrayOutputStream reportOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, reportOutputStream);
        /*writer.setEncryption("cargotraffic".getBytes(), "user_pass".getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128);
        writer.createXmpMetadata();*/

        document.open();

        Paragraph paragraph = new Paragraph(new Phrase("Financial Report",
                FontFactory.getFont(FontFactory.HELVETICA, 20)));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(15);
        document.add(paragraph);
        BaseFont font = BaseFont.createFont("public/style/fonts/TIMCYR.TTF", "cp1251", BaseFont.EMBEDDED);
        PdfPTable table = new PdfPTable(6);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Date");
        table.addCell("Income");
        table.addCell("Vehicle Fuel Loss");
        table.addCell("Products Loss");
        table.addCell("Profit");
        table.addCell("Driver");
        for(FinancialHighlights financialHighlights : financialHighlightsList) {
            table.addCell(financialHighlights.deliveredDate.toString());
            table.addCell(financialHighlights.transportationIncome.toString());
            table.addCell(financialHighlights.vehicleFuelLoss.toString());
            table.addCell(financialHighlights.productsLoss.toString());
            table.addCell(financialHighlights.profit.toString());
            User driver = financialHighlights.waybillVehicleDriver.driver;
            Paragraph driverCell =
                    new Paragraph(driver.surname + " " + driver.name + " " + driver.patronymic, new Font(font));
            table.addCell(driverCell);
        }
        document.add(table);

        document.close();
        reportOutputStream.flush();
        ByteArrayInputStream reportInputStream = new ByteArrayInputStream(reportOutputStream.toByteArray());
        return reportInputStream;
    }

    private InputStream getCsvReport(List<FinancialHighlights> financialHighlightsList) throws IOException, DocumentException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);

        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        CSVPrinter printer = new CSVPrinter(outputStreamWriter, format.withDelimiter(','));

        printer.printRecord("Date","Income","Vehicle Fuel Loss","Products Loss", "Profit", "Driver");
        for(FinancialHighlights financialHighlights : financialHighlightsList){
            List<String> financialData = new ArrayList<String>();
            financialData.add(financialHighlights.deliveredDate.toString());
            financialData.add(String.valueOf(financialHighlights.transportationIncome));
            financialData.add(String.valueOf(financialHighlights.vehicleFuelLoss));
            financialData.add(String.valueOf(financialHighlights.productsLoss));
            financialData.add(String.valueOf(financialHighlights.profit));
            User driver = financialHighlights.waybillVehicleDriver.driver;
            financialData.add(driver.surname + " " + driver.name + " " + driver.patronymic);
            printer.printRecord(financialData);
        }
        outputStreamWriter.flush();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        outputStreamWriter.close();
        return inputStream;
    }
}
