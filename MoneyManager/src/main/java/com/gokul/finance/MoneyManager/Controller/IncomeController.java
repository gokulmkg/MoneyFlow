package com.gokul.finance.MoneyManager.Controller;

import com.gokul.finance.MoneyManager.Dto.ExpensDto;
import com.gokul.finance.MoneyManager.Dto.IncomeDto;
import com.gokul.finance.MoneyManager.Entities.IncomeEntity;
import com.gokul.finance.MoneyManager.Services.ExpenseService;
import com.gokul.finance.MoneyManager.Services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping("/add")
    public ResponseEntity<IncomeDto> addExpense(@RequestBody IncomeDto incomeDto) {
        IncomeDto dto = incomeService.addIncomes(incomeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

     @GetMapping("/all")
    public ResponseEntity<List<IncomeDto>> getAllIncomes() {
       List<IncomeDto> list = incomeService.getIncomeForCurrentMoth();
       return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteincomeById(@PathVariable Long id) {
        incomeService.deleteIncomeBasedonId(id);
        return ResponseEntity.noContent().build();
    }
}
