package pl.noxhours.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.noxhours.client.ClientService;
import pl.noxhours.configuration.GlobalConstants;
import pl.noxhours.configuration.security.NoxUserDetails;
import pl.noxhours.user.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;
    private final ClientService clientService;

    @RequestMapping("/list")
    public String showList(Model model, @RequestParam(required = false) Integer page) {

        if (page == null || page < 1) {
            page = 1;
        }
        Page<Report> reports = reportService.findAll(PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "created")));

        if (page > reports.getTotalPages() && page != 1) {
            page = reports.getTotalPages();
            reports = reportService.findAll(PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "created")));
        }

        model.addAttribute("page", page);
        model.addAttribute("totalPages", reports.getTotalPages());
        model.addAttribute("reports", reports.getContent().stream().peek(reportService::replaceUserWithDto).collect(Collectors.toList()));

        return "report/reportList";
    }

    @GetMapping("/add")
    public String addNewSendToForm(Model model) {

        model.addAttribute("report", new Report());
        model.addAttribute("clients", clientService.findAllActive());
        model.addAttribute("users", userService.findAllActive().stream().map(userService::userToUserNameDto).collect(Collectors.toList()));
        model.addAttribute("dateFormat", GlobalConstants.DATE_FORMAT.replace("M", "m"));
        return "report/reportAdd";
    }

    @PostMapping("/add")
    public String addNew(@Valid Report report, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            reportService.replaceUserWithDto(report);
            model.addAttribute("clients", clientService.findAllActive());
            model.addAttribute("users", userService.findAllActive().stream().map(userService::userToUserNameDto).collect(Collectors.toList()));
            model.addAttribute("dateFormat", GlobalConstants.DATE_FORMAT.replace("M", "m"));
            return "report/reportAdd";
        }
        if (!report.getCreator().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to create Report entity while impersonating user with id of " + report.getCreator().getId());
            return "redirect:/reports/list";
        }
        if (report.getShowRates() != null && report.getShowRates() && !SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("RATES"))) {
            report.setShowRates(false);
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to create Report with rates without proper privileges");
        }
        reportService.create(report);
        return "redirect:/reports/show/" + report.getId();
    }

    @RequestMapping("/delete/{report}")
    public String deleteReport(@PathVariable(required = false) Report report) {
        if (report == null) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to delete invalid Report entity");
            return "redirect:/reports/list";
        }
        if (!report.getCreator().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to delete Report entity " + report.getId() + " which he doesn't own");
            return "redirect:/reports/list";
        }
        reportService.delete(report);
        return "redirect:/reports/list?deleteSuccess=true";
    }

    @RequestMapping("show/{report}")
    public String showReport(@PathVariable(required = false) Report report, Model model) {

        if (report == null) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to display invalid Report entity");
            return "redirect:/reports/list";
        }
        if (!report.getCreator().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            log.warn("User " + SecurityContextHolder.getContext().getAuthentication().getName() + " attempted to display Report entity " + report.getId() + " which he doesn't own");
            return "redirect:/reports/list";
        }
        reportService.generate(report);
        if (report.getShowRates() && !SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("RATES"))) {
            report.setValueByRank(null);
            report.setTotalValue(null);
            model.addAttribute("unauthorizedRateAccess", true);
        }
        if (report.getShowRates() && SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("RATES"))) {
            model.addAttribute("rateError",report.getTotalValue() == null);
        }
        model.addAttribute("report", report);
        return "report/reportShow";
    }

    @RequestMapping("mail/{report}")
    @ResponseBody
    public String sendMail(@PathVariable(required = false) Report report, HttpServletResponse response) {
        if (report == null) {
            response.setStatus(404);
            return null;
        }
        if (!report.getCreator().getId().equals(((NoxUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())) {
            response.setStatus(403);
            return null;
        }

        response.setStatus(reportService.sendMail(report) ? 200 : 500);
        return null;
    }

    @RequestMapping("pdf/{report}")
    @ResponseBody
    public ResponseEntity<byte[]> getPdf(@PathVariable(required = false) Report report) throws IOException {

        reportService.getPdf(report, LocaleContextHolder.getLocale());
        byte[] bytes = Files.readAllBytes(Paths.get("NoxHoursReport" + report.getId() + ".pdf"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("NoxHoursReport" + report.getId() + ".pdf", "NoxHoursReport" + report.getId() + ".pdf");

//        TODO sprawdzić jak dokładnie działa cache control i czy tego potrzebuje
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }

    @RequestMapping("mail/pdf/{report}")
    @ResponseBody
    public String sendPdfMail(@PathVariable(required = false) Report report, HttpServletResponse response) {
        if (report == null) {
            response.setStatus(404);
            return null;
        }
        if (!report.getCreator().getId().equals(((NoxUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())) {
            response.setStatus(403);
            return null;
        }
        reportService.getPdf(report, LocaleContextHolder.getLocale());
        response.setStatus(reportService.sendMail(report, "NoxHoursReport" + report.getId() + ".pdf") ? 200 : 500);
        return null;
    }

}
