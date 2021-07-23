package pl.noxhours.configuration;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class NoxErrorController implements ErrorController {

    @RequestMapping("/error")
    public String errorHandler(HttpServletRequest request, Model model) {

        String result = String.valueOf(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));

        if (result == null) {
            return "error/error";
        }
        switch (result) {
            case "403":
                model.addAttribute("code", 403);
                return "error/error";
            case "404":
                model.addAttribute("code", 404);
                return "error/error";
            case "500":
                model.addAttribute("code", 500);
                model.addAttribute("errorMsg", request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
                return "error/error";
            default:
                return "error/error";
        }
    }
}
