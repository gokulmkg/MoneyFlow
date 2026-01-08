package com.gokul.finance.MoneyManager.Controller;

import com.gokul.finance.MoneyManager.Dto.ExpensDto;
import com.gokul.finance.MoneyManager.Dto.IncomeDto;
import com.gokul.finance.MoneyManager.Services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpensController {

    private final ExpenseService expenseService;

    @PostMapping("/add")
    public ResponseEntity<ExpensDto> addExpense(@RequestBody ExpensDto expensDto) {
     ExpensDto dto = expenseService.addExpenses(expensDto);
     return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExpensDto>> getAllIncomes() {
        List<ExpensDto> list = expenseService.getExpensesForCurrentMoth();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteincomeById(@PathVariable Long id) {
        expenseService.deleteexpenseBasedonId(id);
        return ResponseEntity.noContent().build();
    }


}
