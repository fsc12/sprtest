package de.fsc.sprint.sprtest.controller;

import de.fsc.sprint.sprtest.model.WeatherForm;
import de.fsc.sprint.sprtest.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * allg. Controller
 */
@Controller
@Slf4j
class CiCdController extends AbstractController {

    private final WeatherService weatherService;
    private final BrokerService brokerService;

    public CiCdController(WeatherService weatherService, BrokerService brokerService) {
        this.weatherService = weatherService;
        this.brokerService = brokerService;
    }

    @GetMapping("/")
    public String weatherRoot() {
        return "index";
    }

    @GetMapping("/weather")
    public String weather(Model model) {
        model.addAttribute("weatherForm", new WeatherForm());
        return "index";
    }

    @GetMapping("/calc")
    public String calc(Model model) {
        brokerService.sendMessage("------------------------", Collections.emptyList());
//        Map<String, List<Double>> weatherMap = weatherService.calc();
        Map<String, List<Double>> weatherMap = weatherService.calc2();
        for (String city : weatherMap.keySet()) {
            brokerService.sendMessage(city, weatherMap.get(city));
        }
        brokerService.sendMessage("------------------------", Collections.emptyList());
        model.addAttribute("weatherForm", new WeatherForm());
        return "index";
    }

    @PostMapping("/cicd")
    public String submit(@Valid @ModelAttribute("weatherForm") WeatherForm cForm, BindingResult bindingresult, Model model) {
        model.addAttribute("weatherForm", cForm);
        if (bindingresult.hasErrors()) {
            return "index";
        }
        weatherService.start(cForm);
        return "index";
    }

    /**
     * @return User
     */
    private String getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();

    }

}
