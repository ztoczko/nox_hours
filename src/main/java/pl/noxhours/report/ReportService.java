package pl.noxhours.report;

import com.itextpdf.io.font.FontNames;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.itextpdf.layout.Document;
import pl.noxhours.case_.Case;
import pl.noxhours.client.Client;
import pl.noxhours.configuration.EmailService;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.rate.RateService;
import pl.noxhours.timesheet.Timesheet;
import pl.noxhours.timesheet.TimesheetService;
import pl.noxhours.user.User;
import pl.noxhours.user.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final TimesheetService timesheetService;
    private final RateService rateService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final EmailService emailService;

    public void create(Report report) {
        report.setCreated(LocalDateTime.now());
        if (report.getBasedOnClient() == null) {
            report.setBasedOnClient(false);
        }
        if (report.getBasedOnCase() == null || !report.getBasedOnClient()) {
            report.setBasedOnCase(false);
        }
        if (report.getBasedOnUser() == null) {
            report.setBasedOnUser(false);
        }
        if (report.getShowDetails() == null) {
            report.setShowDetails(false);
        }
        if (report.getShowNames() == null) {
            report.setShowNames(false);
        }
        if (report.getShowRates() == null) {
            report.setShowRates(false);
        }
        if (!report.getBasedOnUser()) {
            report.setBaseUser(null);
        }
        if (!report.getBasedOnClient()) {
            report.setBaseClient(null);
        }
        if (!report.getBasedOnCase()) {
            report.setBaseCase(null);
        }
        if (!report.getShowDetails() && report.getShowNames()) {
            report.setShowNames(false);
        }
        reportRepository.save(report);
    }

    public Report read(Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    public void update(Report report) {
        reportRepository.save(report);
    }

    public void delete(Report report) {
        reportRepository.delete(report);
    }

    public Page<Report> findAll(Pageable pageable) {
        return reportRepository.findAllByCreator(pageable, userService.read(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public List<Report> findAll(User user) {
        return reportRepository.findAllByCreatorOrBaseUser(user, user);
    }

    public List<Report> findAll(Client client) {
        return reportRepository.findAllByBaseClient(client);
    }

    public List<Report> findAll(Case aCase) {
        return reportRepository.findAllByBaseCase(aCase);
    }

    public void replaceUserWithDto(Report report) {

        report.setCreatorDTO(userService.userToUserNameDto(report.getCreator()));
        report.setCreator(null);
        if (report.getBaseUser() != null) {
            report.setBaseUserDTO(userService.userToUserNameDto(report.getBaseUser()));
            report.setBaseUser(null);
        } else {
            report.setBaseUserDTO(null);
        }
    }

    public void generate(Report report) {

        report.setHoursByRank(new ArrayList<>());
        report.setMinutesByRank(new ArrayList<>());

        List<Timesheet> result = new ArrayList<>();

        if (report.getBasedOnClient() && report.getBasedOnUser() && report.getBasedOnCase()) {
            result = timesheetService.findAll(report.getBaseUser(), report.getBaseClient(), report.getBaseCase(), report.getDateFrom(), report.getDateTo());
        } else {

            if (report.getBasedOnClient() && report.getBasedOnCase()) {
                result = timesheetService.findAll(report.getBaseClient(), report.getBaseCase(), report.getDateFrom(), report.getDateTo());
            } else {
                if (report.getBasedOnUser() && report.getBasedOnClient()) {
                    result = timesheetService.findAll(report.getBaseUser(), report.getBaseClient(), report.getDateFrom(), report.getDateTo());
                } else {
                    if (report.getBasedOnClient()) {
                        result = timesheetService.findAll(report.getBaseClient(), report.getDateFrom(), report.getDateTo());
                    }
                    if (report.getBasedOnUser()) {
                        result = timesheetService.findAll(report.getBaseUser(), report.getDateFrom(), report.getDateTo());
                    }
                    if (!report.getBasedOnClient() && !report.getBasedOnUser()) {
                        result = timesheetService.findAll(report.getDateFrom(), report.getDateTo());
                    }
                }
            }
        }
        if (result == null) {
            result = new ArrayList<>();
        }
        report.setTimesheets(result);
        for (int i = 1; i < 5; i++) {
            int tempRank = i;
            report.getHoursByRank().add(result.stream().filter(item -> item.getRankWhenCreated() == tempRank).map(Timesheet::getHours).reduce(0, Integer::sum) + result.stream().filter(item -> item.getRankWhenCreated() == tempRank).map(Timesheet::getMinutes).reduce(0, Integer::sum) / 60);
            report.getMinutesByRank().add(result.stream().filter(item -> item.getRankWhenCreated() == tempRank).map(Timesheet::getMinutes).reduce(0, Integer::sum) % 60);
        }
        report.setTotalHours(report.getHoursByRank().stream().reduce(0, Integer::sum) + report.getMinutesByRank().stream().reduce(0, Integer::sum) / 60);
        report.setTotalMinutes(report.getMinutesByRank().stream().reduce(0, Integer::sum) % 60);

        if (report.getShowRates() && rateService.validateRateAvailablity(result)) {
            rateService.getTimesheetValueForRank(report);
            report.setTotalValue(report.getValueByRank().stream().reduce(new BigDecimal(0), BigDecimal::add));
        }
        report.getTimesheets().forEach(timesheetService::replaceUserWithDto);
        replaceUserWithDto(report);
    }

    public boolean sendMail(Report report) {
        return emailService.sendMessage(SecurityContextHolder.getContext().getAuthentication().getName(), messageSource.getMessage("email.report.subject", null, LocaleContextHolder.getLocale()), generateHtmlMessage(report, LocaleContextHolder.getLocale()), null);
    }

    public boolean sendMail(Report report, String filename) {
        return emailService.sendMessage(SecurityContextHolder.getContext().getAuthentication().getName(), messageSource.getMessage("email.report.subject", null, LocaleContextHolder.getLocale()), messageSource.getMessage("email.report.attachment.body", null, LocaleContextHolder.getLocale()) + " " + report.getCreatedString(), filename);
    }

    public void getPdf(Report report, Locale locale) {

        generate(report);
        String fileName = "NoxHoursReport" + report.getId() + ".pdf";
        try (PdfWriter writer = new PdfWriter(fileName)) {

            String pathToFont = "/static/fonts/times.ttf";
            String fontName = ReportService.class.getResource(pathToFont).toString();
            FontProgram fontProgram = FontProgramFactory.createFont(fontName, true);
            PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H);

            PdfDocument pdfDocument = new PdfDocument(writer);
            pdfDocument.addFont(font);
            Document document = new Document(pdfDocument);
            Paragraph par = new Paragraph().setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(24).setBold();
            par.add(messageSource.getMessage("pdf.title", new String[]{report.getDateFromString(), report.getDateToString()}, locale));
            document.add(par);
            par = new Paragraph().setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(16);
            par.add(messageSource.getMessage("pdf.subtitle", new String[]{report.getCreatorDTO().getFullName(), report.getCreatedString()}, locale));
            document.add(par);
            par = new Paragraph().setMinHeight(100);
            document.add(par);

            Table table = new Table(new float[]{150, 50}, true).setHorizontalAlignment(HorizontalAlignment.CENTER);
            Cell cell = new Cell(1, 2).setBackgroundColor(new DeviceRgb(0, 128, 128));
            par = new Paragraph().add(messageSource.getMessage("report.show.aggregate", null, locale)).setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(14).setBold();
            cell.add(par);
            table.addCell(cell);

            par = new Paragraph().add(messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.student", null, locale)).setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(14);
            table.addCell(par);

            par = new Paragraph().add(report.getHoursByRankString().get(0)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14);
            table.addCell(par);

            par = new Paragraph().add(messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.applicant", null, locale)).setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(14);
            table.addCell(par);

            par = new Paragraph().add(report.getHoursByRankString().get(1)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14);
            table.addCell(par);

            par = new Paragraph().add(messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.attorney", null, locale)).setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(14);
            table.addCell(par);

            par = new Paragraph().add(report.getHoursByRankString().get(2)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14);
            table.addCell(par);

            par = new Paragraph().add(messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.partner", null, locale)).setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(14);
            table.addCell(par);

            par = new Paragraph().add(report.getHoursByRankString().get(3)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14);
            table.addCell(par);

            par = new Paragraph().add(messageSource.getMessage("report.show.total.hours", null, locale)).setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(14).setBold();
            table.addCell(par);

            par = new Paragraph().add(report.getTotalHoursString()).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14).setBold();
            table.addCell(par);

            if (report.getShowRates() && SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("RATES"))) {
                if (report.getTotalValue() == null) {

                    par = new Paragraph().add(messageSource.getMessage("report.show.rates.error", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14).setFontColor(ColorConstants.RED);
                    table.addCell(new Cell(1, 2).setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));
                } else {

                    par = new Paragraph().add(messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.student", null, locale)).setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(14);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(report.getValueByRank().get(0) + " " + messageSource.getMessage("app.currency", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.applicant", null, locale)).setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(14);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(report.getValueByRank().get(1) + " " + messageSource.getMessage("app.currency", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.attorney", null, locale)).setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(14);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(report.getValueByRank().get(2) + " " + messageSource.getMessage("app.currency", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.partner", null, locale)).setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(14);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(report.getValueByRank().get(3) + " " + messageSource.getMessage("app.currency", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(messageSource.getMessage("report.show.total.value", null, locale)).setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(14).setBold();
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(report.getTotalValue() + " " + messageSource.getMessage("app.currency", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(14).setBold();
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));
                }
            }
            document.add(table);
            table.complete();

            if (report.getShowDetails()) {

                pdfDocument.setDefaultPageSize(PageSize.A4.rotate());

                document.add(new AreaBreak());


                table = new Table(report.getShowNames() ? 7 : 6, true).setHorizontalAlignment(HorizontalAlignment.CENTER);

                par = new Paragraph().add(messageSource.getMessage("report.show.details", null, locale)).setTextAlignment(TextAlignment.LEFT).setFont(font).setFontSize(12).setBold();
                table.addCell(new Cell(1, (report.getShowNames() ? 7 : 6)).setBackgroundColor(new DeviceRgb(0, 128, 128)).add(par));

                par = new Paragraph().add(messageSource.getMessage("timesheet.date.executed", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10).setBold();
                table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                par = new Paragraph().add(messageSource.getMessage("timesheet.case", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10).setBold();
                table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                if (report.getShowNames()) {
                    par = new Paragraph().add(messageSource.getMessage("timesheet.user", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10).setBold();
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));
                }

                par = new Paragraph().add(messageSource.getMessage("timesheet.rank.when.created", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10).setBold();
                table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                par = new Paragraph().add(messageSource.getMessage("timesheet.client", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10).setBold();
                table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                par = new Paragraph().add(messageSource.getMessage("timesheet.hours", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10).setBold();
                table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                par = new Paragraph().add(messageSource.getMessage("timesheet.description", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10).setBold();
                table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                if (report.getTimesheets().size() == 0) {
                    par = new Paragraph().add(messageSource.getMessage("report.show.no.timesheets.message", null, locale)).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(12).setFontColor(ColorConstants.RED);
                    table.addCell(new Cell(1, (report.getShowNames() ? 6 : 5)).add(par));
                }

                for (Timesheet timesheet : report.getTimesheets()) {

                    par = new Paragraph().add(timesheet.getDateExecutedString()).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(timesheet.getClientCase() == null ? messageSource.getMessage("case.no.case", null, locale) : timesheet.getClientCase().getName()).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    if (report.getShowNames()) {
                        par = new Paragraph().add(timesheet.getUserNameDTO().getFullName()).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10);
                        table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));
                    }

                    String rankString = new String();
                    switch (timesheet.getRankWhenCreated()) {
                        case 1:
                            rankString = messageSource.getMessage("user.rank.student", null, locale);
                            break;
                        case 2:
                            rankString = messageSource.getMessage("user.rank.applicant", null, locale);
                            break;
                        case 3:
                            rankString = messageSource.getMessage("user.rank.attorney", null, locale);
                            break;
                        case 4:
                            rankString = messageSource.getMessage("user.rank.partner", null, locale);
                            break;
                    }

                    par = new Paragraph().add(rankString).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(timesheet.getClient().getName()).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(timesheet.getHoursString()).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));

                    par = new Paragraph().add(timesheet.getDescription()).setTextAlignment(TextAlignment.CENTER).setFont(font).setFontSize(10);
                    table.addCell(new Cell().setVerticalAlignment(VerticalAlignment.MIDDLE).add(par));
                }
                document.add(table);
                table.complete();

            }
            document.close();
            pdfDocument.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void deleteFile(String filename) {

        try {
            Files.deleteIfExists(Paths.get(filename));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void getXls(Report report, Locale locale) {

        generate(report);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(messageSource.getMessage("report.show.aggregate", null, locale));
        sheet.setColumnWidth(1, 20000);
        sheet.setColumnWidth(2, 5000);

        CellStyle title = workbook.createCellStyle();
        title.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
        title.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
        title.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        title.setFillForegroundColor(IndexedColors.TEAL.getIndex());
        title.setBorderTop(BorderStyle.THIN);
        title.setBorderBottom(BorderStyle.THIN);
        title.setBorderLeft(BorderStyle.THIN);
        title.setBorderRight(BorderStyle.THIN);
        XSSFFont fontTitle = ((XSSFWorkbook) workbook).createFont();
        fontTitle.setFontName("Arial");
        fontTitle.setBold(true);
        fontTitle.setFontHeight(16);
        title.setFont(fontTitle);

        CellStyle headerSummary = workbook.createCellStyle();
        headerSummary.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);
        headerSummary.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
        headerSummary.setBorderTop(BorderStyle.THIN);
        headerSummary.setBorderBottom(BorderStyle.THIN);
        headerSummary.setBorderLeft(BorderStyle.THIN);
        headerSummary.setBorderRight(BorderStyle.THIN);
        XSSFFont fontHeaderSummary = ((XSSFWorkbook) workbook).createFont();
        fontHeaderSummary.setFontName("Arial");
        fontHeaderSummary.setFontHeight(12);
        headerSummary.setFont(fontHeaderSummary);

        CellStyle headerSummaryBolded = workbook.createCellStyle();
        headerSummaryBolded.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);
        headerSummaryBolded.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
        headerSummaryBolded.setBorderTop(BorderStyle.THIN);
        headerSummaryBolded.setBorderBottom(BorderStyle.THIN);
        headerSummaryBolded.setBorderLeft(BorderStyle.THIN);
        headerSummaryBolded.setBorderRight(BorderStyle.THIN);
        XSSFFont fontHeaderSummaryBolded = ((XSSFWorkbook) workbook).createFont();
        fontHeaderSummaryBolded.setFontName("Arial");
        fontHeaderSummaryBolded.setBold(true);
        fontHeaderSummaryBolded.setFontHeight(12);
        headerSummaryBolded.setFont(fontHeaderSummaryBolded);

        CellStyle headerDetails = workbook.createCellStyle();
        headerDetails.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
        headerDetails.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
        headerDetails.setBorderTop(BorderStyle.THIN);
        headerDetails.setBorderBottom(BorderStyle.THIN);
        headerDetails.setBorderLeft(BorderStyle.THIN);
        headerDetails.setBorderRight(BorderStyle.THIN);
        XSSFFont fontHeaderDetails = ((XSSFWorkbook) workbook).createFont();
        fontHeaderDetails.setFontName("Arial");
        fontHeaderDetails.setBold(true);
        fontHeaderDetails.setFontHeight(12);
        headerDetails.setFont(fontHeaderDetails);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        XSSFFont fontCell = ((XSSFWorkbook) workbook).createFont();
        fontCell.setFontName("Arial");
        fontCell.setFontHeight(12);
        cellStyle.setFont(fontCell);

        Row row = sheet.createRow(2);
        row.setHeight((short) 1200);
        org.apache.poi.ss.usermodel.Cell cell = row.createCell(1);
        cell.setCellValue("    " + messageSource.getMessage("report.show.aggregate", null, locale) + "\n    " + messageSource.getMessage("report.show.aggregate.msg", new String[]{report.getDateFromString(), report.getDateToString()}, locale));
        cell.setCellStyle(title);
        cell = row.createCell(2);
        cell.setCellStyle(title);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));

        row = sheet.createRow(3);
        row.setHeight((short) 500);
        cell = row.createCell(1);
        cell.setCellValue(messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.student", null, locale) + "   ");
        cell.setCellStyle(headerSummary);

        cell = row.createCell(2);
        cell.setCellValue(report.getHoursByRankString().get(0));
        cell.setCellStyle(cellStyle);

        row = sheet.createRow(4);
        row.setHeight((short) 500);
        cell = row.createCell(1);
        cell.setCellValue(messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.applicant", null, locale) + "   ");
        cell.setCellStyle(headerSummary);

        cell = row.createCell(2);
        cell.setCellValue(report.getHoursByRankString().get(1));
        cell.setCellStyle(cellStyle);

        row = sheet.createRow(5);
        row.setHeight((short) 500);
        cell = row.createCell(1);
        cell.setCellValue(messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.attorney", null, locale) + "   ");
        cell.setCellStyle(headerSummary);

        cell = row.createCell(2);
        cell.setCellValue(report.getHoursByRankString().get(2));
        cell.setCellStyle(cellStyle);

        row = sheet.createRow(6);
        row.setHeight((short) 500);
        cell = row.createCell(1);
        cell.setCellValue(messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.partner", null, locale) + "   ");
        cell.setCellStyle(headerSummary);

        cell = row.createCell(2);
        cell.setCellValue(report.getHoursByRankString().get(3));
        cell.setCellStyle(cellStyle);

        row = sheet.createRow(7);
        row.setHeight((short) 500);
        cell = row.createCell(1);
        cell.setCellValue(messageSource.getMessage("report.show.total.hours", null, locale) + "   ");
        cell.setCellStyle(headerSummaryBolded);

        cell = row.createCell(2);
        cell.setCellValue(report.getTotalHoursString());
        cell.setCellStyle(headerDetails);

        if (report.getShowRates() && SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("RATES"))) {
            if (report.getTotalValue() == null) {
                row = sheet.createRow(8);
                row.setHeight((short) 500);
                cell = row.createCell(1);
                cell.setCellValue(messageSource.getMessage("report.show.rates.error", null, locale));
                cell.setCellStyle(cellStyle);
                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(8, 8, 1, 2));

            } else {

                row = sheet.createRow(8);
                row.setHeight((short) 500);
                cell = row.createCell(1);
                cell.setCellValue(messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.student", null, locale) + "   ");
                cell.setCellStyle(headerSummary);

                cell = row.createCell(2);
                cell.setCellValue(report.getValueByRank().get(0) + " " + messageSource.getMessage("app.currency", null, locale));
                cell.setCellStyle(cellStyle);

                row = sheet.createRow(9);
                row.setHeight((short) 500);
                cell = row.createCell(1);
                cell.setCellValue(messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.applicant", null, locale) + "   ");
                cell.setCellStyle(headerSummary);

                cell = row.createCell(2);
                cell.setCellValue(report.getValueByRank().get(1) + " " + messageSource.getMessage("app.currency", null, locale));
                cell.setCellStyle(cellStyle);

                row = sheet.createRow(10);
                row.setHeight((short) 500);
                cell = row.createCell(1);
                cell.setCellValue(messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.attorney", null, locale) + "   ");
                cell.setCellStyle(headerSummary);

                cell = row.createCell(2);
                cell.setCellValue(report.getValueByRank().get(2) + " " + messageSource.getMessage("app.currency", null, locale));
                cell.setCellStyle(cellStyle);

                row = sheet.createRow(11);
                row.setHeight((short) 500);
                cell = row.createCell(1);
                cell.setCellValue(messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.partner", null, locale) + "   ");
                cell.setCellStyle(headerSummary);

                cell = row.createCell(2);
                cell.setCellValue(report.getValueByRank().get(3) + " " + messageSource.getMessage("app.currency", null, locale));
                cell.setCellStyle(cellStyle);

                row = sheet.createRow(12);
                row.setHeight((short) 500);
                cell = row.createCell(1);
                cell.setCellValue(messageSource.getMessage("report.show.total.value", null, locale) + "   ");
                cell.setCellStyle(headerSummaryBolded);

                cell = row.createCell(2);
                cell.setCellValue(report.getTotalValue() + " " + messageSource.getMessage("app.currency", null, locale));
                cell.setCellStyle(headerDetails);
            }
        }
        if (report.getShowDetails()) {

            sheet = workbook.createSheet(messageSource.getMessage("report.show.details", null, locale));
            sheet.setColumnWidth(1, 5000);
            sheet.setColumnWidth(2, 10000);
            int columnCount = 3;
            if (report.getShowNames()) {
                sheet.setColumnWidth(3, 10000);
                columnCount = 4;
            }
            sheet.setColumnWidth(columnCount, 7000);
            sheet.setColumnWidth(columnCount + 1, 10000);
            sheet.setColumnWidth(columnCount + 2, 5000);
            sheet.setColumnWidth(columnCount + 3, 10000);

            row = sheet.createRow(2);
            row.setHeight((short) 1200);
            cell = row.createCell(1);
            cell.setCellValue("    " + messageSource.getMessage("report.show.details", null, locale));
            cell.setCellStyle(title);

            for (int i = 2; i < columnCount + 4; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(title);
            }
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, columnCount + 3));

            row = sheet.createRow(3);
            row.setHeight((short) 500);

            cell = row.createCell(1);
            cell.setCellValue(messageSource.getMessage("timesheet.date.executed", null, locale));
            cell.setCellStyle(headerDetails);

            cell = row.createCell(2);
            cell.setCellValue(messageSource.getMessage("timesheet.case", null, locale));
            cell.setCellStyle(headerDetails);

            if (report.getShowNames()) {
                cell = row.createCell(3);
                cell.setCellValue(messageSource.getMessage("timesheet.user", null, locale));
                cell.setCellStyle(headerDetails);
            }

            cell = row.createCell(columnCount);
            cell.setCellValue(messageSource.getMessage("timesheet.rank.when.created", null, locale));
            cell.setCellStyle(headerDetails);

            cell = row.createCell(columnCount + 1);
            cell.setCellValue(messageSource.getMessage("timesheet.client", null, locale));
            cell.setCellStyle(headerDetails);

            cell = row.createCell(columnCount + 2);
            cell.setCellValue(messageSource.getMessage("timesheet.hours", null, locale));
            cell.setCellStyle(headerDetails);

            cell = row.createCell(columnCount + 3);
            cell.setCellValue(messageSource.getMessage("timesheet.description", null, locale));
            cell.setCellStyle(headerDetails);

            if (report.getTimesheets().size() == 0) {
                row = sheet.createRow(4);
                row.setHeight((short) 500);
                cell = row.createCell(1);
                cell.setCellValue(messageSource.getMessage("report.show.no.timesheets.message", null, locale));
                cell.setCellStyle(cellStyle);
                for (int i = 2; i < columnCount + 4; i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyle);
                }
                sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, columnCount + 3));
            }
            int rowCount = 3;

            for (Timesheet timesheet : report.getTimesheets()) {

                row = sheet.createRow(++rowCount);
                row.setHeight((short) 500);

                cell = row.createCell(1);
                cell.setCellValue(timesheet.getDateExecutedString());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(2);
                cell.setCellValue(timesheet.getClientCase() == null ? messageSource.getMessage("case.no.case", null, locale) : timesheet.getClientCase().getName());
                cell.setCellStyle(cellStyle);

                if (report.getShowNames()) {
                    cell = row.createCell(3);
                    cell.setCellValue(timesheet.getUserNameDTO().getFullName());
                    cell.setCellStyle(cellStyle);
                }

                cell = row.createCell(columnCount);

                switch (timesheet.getRankWhenCreated()) {
                    case 1:
                        cell.setCellValue(messageSource.getMessage("user.rank.student", null, locale));
                        break;
                    case 2:
                        cell.setCellValue(messageSource.getMessage("user.rank.applicant", null, locale));
                        break;
                    case 3:
                        cell.setCellValue(messageSource.getMessage("user.rank.attorney", null, locale));
                        break;
                    case 4:
                        cell.setCellValue(messageSource.getMessage("user.rank.partner", null, locale));
                        break;
                }
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount + 1);
                cell.setCellValue(timesheet.getClient().getName());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount + 2);
                cell.setCellValue(timesheet.getHoursString());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(columnCount + 3);
                cell.setCellValue(timesheet.getDescription());
                cell.setCellStyle(cellStyle);
            }
        }
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "NoxHoursReport" + report.getId() + ".xlsx";
        try {
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();

            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateHtmlMessage(Report report, Locale locale) {
        generate(report);

        String message = GlobalConstants.EMAIL_HEAD;
        message += "\n<p>" + messageSource.getMessage("email.report.head", null, locale) + " " + report.getCreatedString() + "</p>\n";
        message += "<table style=\"border: thin solid black; border-collapse: collapse;\">\n<thead>\n<tr>\n<th colspan=\"4\">\n" + messageSource.getMessage("report.show.aggregate", null, locale) + "\n<br>\n" + messageSource.getMessage("report.show.aggregate.msg", new String[]{report.getDateFromString(), report.getDateToString()}, locale) + "\n</th>\n</tr>\n</thead>\n";
        message += "<tbody>\n";
        message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.student", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getHoursByRankString().get(0) + "\n</td>\n</tr>\n";
        message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.applicant", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getHoursByRankString().get(1) + "\n</td>\n</tr>\n";
        message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.attorney", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getHoursByRankString().get(2) + "\n</td>\n</tr>\n";
        message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.hours.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.partner", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getHoursByRankString().get(3) + "\n</td>\n</tr>\n";
        message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right; font-weight: bold;\">\n" + messageSource.getMessage("report.show.total.hours", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center; font-weight: bold;\">\n" + report.getTotalHoursString() + "\n</td>\n</tr>\n";
        if (report.getShowRates() && SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("RATES"))) {
            if (report.getTotalValue() == null) {
                message += "<tr>\n<td colspan=\"4\">\n" + messageSource.getMessage("report.show.rates.error", null, locale) + "\n</td>\n</tr>\n";
            } else {
                message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.student", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getValueByRank().get(0) + " " + messageSource.getMessage("app.currency", null, locale) + "\n</td>\n</tr>\n";
                message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.applicant", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getValueByRank().get(1) + " " + messageSource.getMessage("app.currency", null, locale) + "\n</td>\n</tr>\n";
                message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.attorney", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getValueByRank().get(2) + " " + messageSource.getMessage("app.currency", null, locale) + "\n</td>\n</tr>\n";
                message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right;\">\n" + messageSource.getMessage("report.show.values.for.rank", null, locale) + " " + messageSource.getMessage("user.rank.partner", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + report.getValueByRank().get(3) + " " + messageSource.getMessage("app.currency", null, locale) + "\n</td>\n</tr>\n";
                message += "<tr>\n<td colspan=\"3\" style=\"border: thin solid black; border-collapse: collapse; text-align: right; font-weight: bold;\">\n" + messageSource.getMessage("report.show.total.value", null, locale) + "\n</td>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center; font-weight: bold;\">\n" + report.getTotalValue() + " " + messageSource.getMessage("app.currency", null, locale) + "\n</td>\n</tr>\n";
            }
        }
        message += "</tbody></table>";

        if (report.getShowDetails()) {
            message += "\n<br><br>\n";
            message += "<table style=\"border: thin solid black; border-collapse: collapse;\">\n<thead>\n<tr>\n<th colspan=\"" + (report.getShowNames() ? "7" : "6") + "\">\n" + messageSource.getMessage("report.show.details", null, locale) + "\n</th>\n</tr>\n</thead>\n";
            message += "<tbody>\n<tr>\n";
            message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.date.executed", null, locale) + "\n</th>\n";
            message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.case", null, locale) + "\n</th>\n";
            if (report.getShowNames()) {
                message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.user", null, locale) + "\n</th>\n";
            }
            message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.rank.when.created", null, locale) + "\n</th>\n";
            message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.client", null, locale) + "\n</th>\n";
            message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.hours", null, locale) + "\n</th>\n";
            message += "<th style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("timesheet.description", null, locale) + "\n</th>\n";
            message += "</tr>\n";
            if (report.getTimesheets().size() == 0) {
                message += "<tr>\n<td colspan=\"" + (report.getShowNames() ? "7" : "6") + "\" style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + messageSource.getMessage("report.show.no.timesheets.message", null, locale) + "\n</td>\n</tr>\n";
            }
            for (Timesheet timesheet : report.getTimesheets()) {
                message += "<tr>\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + timesheet.getDateExecutedString() + "\n</td>\n";
                message += "\n<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + (timesheet.getClientCase() == null ? messageSource.getMessage("case.no.case", null, locale) : timesheet.getClientCase().getName()) + "\n</td>\n";
                if (report.getShowNames()) {
                    message += "<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + timesheet.getUserNameDTO().getFullName() + "\n</td>\n";
                }
                message += "<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n";
                switch (timesheet.getRankWhenCreated()) {
                    case 1:
                        message += messageSource.getMessage("user.rank.student", null, locale);
                        break;
                    case 2:
                        message += messageSource.getMessage("user.rank.applicant", null, locale);
                        break;
                    case 3:
                        message += messageSource.getMessage("user.rank.attorney", null, locale);
                        break;
                    case 4:
                        message += messageSource.getMessage("user.rank.partner", null, locale);
                        break;
                }
                message += "\n</td>\n";
                message += "<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + timesheet.getClient().getName() + "\n</td>\n";
                message += "<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + timesheet.getHoursString() + "\n</td>\n";
                message += "<td style=\"border: thin solid black; border-collapse: collapse; text-align: center;\">\n" + timesheet.getDescription() + "\n</td>\n</tr>\n";
            }
            message += "</tbody></table>\n";
        }
        message += "<br><br><p>" + messageSource.getMessage("email.footer", null, locale) + "</p>";

        return message;
    }
}
