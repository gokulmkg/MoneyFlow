package com.gokul.finance.MoneyManager.Controller;

import com.gokul.finance.MoneyManager.Services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<Map<String,Object>> getDashboardDate() {
        Map<String,Object> dashboard = dashboardService.getDashboardData();
        return ResponseEntity.ok(dashboard);
    }


}
