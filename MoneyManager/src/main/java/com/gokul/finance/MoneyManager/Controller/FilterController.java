package com.gokul.finance.MoneyManager.Controller;

import com.gokul.finance.MoneyManager.Dto.ExpensDto;
import com.gokul.finance.MoneyManager.Dto.FilterDto;
import com.gokul.finance.MoneyManager.Dto.IncomeDto;
import com.gokul.finance.MoneyManager.Services.ExpenseService;
import com.gokul.finance.MoneyManager.Services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDto filterDto) {
        LocalDate startDate = filterDto.getStartDate() != null ? filterDto.getStartDate():LocalDate.MIN;
        LocalDate endDate = filterDto.getEndDate()!= null ? filterDto.getEndDate():LocalDate.now();
        String keyword = filterDto.getKeyword()!=null ? filterDto.getKeyword():"";
        String sortField =  filterDto.getSortfield()!=null ? filterDto.getSortfield(): "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filterDto.getSortOrder()) ? Sort.Direction.DESC:Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortField);

        if("income".equalsIgnoreCase(filterDto.getType())) {
          List<IncomeDto> incomelist =  incomeService.filterIncome(startDate,endDate,keyword,sort);
          return ResponseEntity.ok(incomelist);
        }else if("expense".equalsIgnoreCase(filterDto.getType())) {
         List<ExpensDto> expenselist =  expenseService.filterExpense(startDate,endDate,keyword,sort);
         return ResponseEntity.ok(expenselist);
        } else{
            return ResponseEntity.badRequest().body("Invalid type. Must be 'income' or 'expense' ");
        }
        }


}
