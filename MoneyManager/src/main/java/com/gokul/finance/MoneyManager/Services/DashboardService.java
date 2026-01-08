package com.gokul.finance.MoneyManager.Services;

import com.gokul.finance.MoneyManager.Dto.ExpensDto;
import com.gokul.finance.MoneyManager.Dto.IncomeDto;
import com.gokul.finance.MoneyManager.Dto.RecentTransactionDto;
import com.gokul.finance.MoneyManager.Entities.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;


@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final ProfileService profileService;


//    public Map<String, Object> getDashboardData() {
//       ProfileEntity profile =  profileService.getCurrentProfile();
//       Map<String,Objects> returnValue = new LinkedHashMap<>();
//        List<IncomeDto> latestIncomes = incomeService.latestTopFiveExpense();
//        List<ExpensDto> latestExpense = expenseService.latestTopFiveExpense();
//       List<RecentTransactionDto> recentTransactionDtos =  concat(latestIncomes.stream().map(income ->   // concat the two list
//                RecentTransactionDto.builder()
//                        .id(income.getId())
//                        .profileId(profile.getId())
//                        .icon(income.getIcon())
//                        .name(income.getName())
//                        .amount(income.getAmount())
//                        .date(income.getDate())
//                        .createdAt(income.getCreatedAt())
//                        .updatedAt(income.getUpdatedAt())
//                        .type("income")
//                        .build()),
//                latestExpense.stream().map(expense ->
//                        RecentTransactionDto.builder()
//                                .id(expense.getId())
//                                .profileId(profile.getId())
//                                .icon(expense.getIcon())
//                                .name(expense.getName())
//                                .amount(expense.getAmount())
//                                .date(expense.getDate())
//                                .createdAt(expense.getCreatedAt())
//                                .updatedAt(expense.getUpdatedAt())
//                                .type("expense")
//                                .build()))
//               .sorted((a,b) ->{
//                    int cmp = b.getDate().compareTo(a.getDate());
//                    if(cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
//                        return b.getCreatedAt().compareTo(a.getCreatedAt());
//                    }
//                    return cmp;
//        }).collect(Collectors.toList());
//
//        BigDecimal totalIncome = incomeService.getTotalIncome();
//        BigDecimal totalExpense = expenseService.getTotalExpense();
//        BigDecimal remainingBalance = totalIncome.subtract(totalExpense);
//
//        returnValue.put("totalIncome", totalIncome);
//        returnValue.put("totalExpense", totalExpense);
//        returnValue.put("remainingBalance", remainingBalance);
//        returnValue.put("recentTransactions", recentTransactionDtos);
//
//
//    }



    public Map<String, Object> getDashboardData() {

        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();

        List<IncomeDto> latestIncomes = incomeService.latestTopFiveExpense();
        List<ExpensDto> latestExpense = expenseService.latestTopFiveExpense();

        List<RecentTransactionDto> recentTransactionDtos =
                Stream.concat(
                                latestIncomes.stream().map(income ->
                                        RecentTransactionDto.builder()
                                                .id(income.getId())
                                                .profileId(profile.getId())
                                                .icon(income.getIcon())
                                                .name(income.getName())
                                                .amount(income.getAmount())
                                                .date(income.getDate())
                                                .createdAt(income.getCreatedAt())
                                                .updatedAt(income.getUpdatedAt())
                                                .type("income")
                                                .build()
                                ),
                                latestExpense.stream().map(expense ->
                                        RecentTransactionDto.builder()
                                                .id(expense.getId())
                                                .profileId(profile.getId())
                                                .icon(expense.getIcon())
                                                .name(expense.getName())
                                                .amount(expense.getAmount())
                                                .date(expense.getDate())
                                                .createdAt(expense.getCreatedAt())
                                                .updatedAt(expense.getUpdatedAt())
                                                .type("expense")
                                                .build()
                                )
                        )
                        .sorted((a, b) -> {
                            int cmp = b.getDate().compareTo(a.getDate());
                            if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                                return b.getCreatedAt().compareTo(a.getCreatedAt());
                            }
                            return cmp;
                        })
                        .collect(Collectors.toList());

        BigDecimal totalIncome = incomeService.getTotalIncome();
        BigDecimal totalExpense = expenseService.getTotalExpense();
        BigDecimal remainingBalance = totalIncome.subtract(totalExpense);
        remainingBalance = remainingBalance.max(BigDecimal.ZERO);


        returnValue.put("remainingBalance", remainingBalance);
        returnValue.put("totalIncome", totalIncome);
        returnValue.put("totalExpense", totalExpense);
        returnValue.put("recent5Expense",latestExpense);
        returnValue.put("recent5Incomes",latestIncomes);
        returnValue.put("recentTransactions", recentTransactionDtos);
        return returnValue;
    }



}
