package com.gokul.finance.MoneyManager.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staus")
public class HomeController {

    @GetMapping("/health")
    public String healthCheck() {

        return "Application is Running";
    }

}
