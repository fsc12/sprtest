package de.fsc.sprint.sprtest.controller;

import de.fsc.sprint.sprtest.model.WeatherForm;
import de.fsc.sprint.sprtest.services.BrokerService;
import de.fsc.sprint.sprtest.services.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * allg. Controller
 */
@Controller
@Slf4j
class CiCdController extends AbstractController {

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private BrokerService brokerService;

    private String component;

    @GetMapping("/")
    public String weatherRoot(Model model) {
        return "index";
    }

    @GetMapping("/weather")
    public String weather(Model model) {
        model.addAttribute("weatherForm", new WeatherForm());
        return "index";
    }

    @GetMapping("/calc")
    public String calc(Model model) {
        Map<String, List<Double>> weatherMap = weatherService.calc();
        for (String city : weatherMap.keySet()) {
            brokerService.sendMessage(city, weatherMap.get(city));
        }
        model.addAttribute("weatherForm", new WeatherForm());
        return "index";
    }

    @PostMapping("/cicd")
    public String submit(@Valid @ModelAttribute("weatherForm") WeatherForm cForm, BindingResult bindingresult, Model model) {
        model.addAttribute("weatherForm", cForm);
        model.addAttribute("component", this.component);
        if (bindingresult.hasErrors()) {
            return "index";
        }
        weatherService.start(cForm);
        return "index";
    }

    private String getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();

    }

}
