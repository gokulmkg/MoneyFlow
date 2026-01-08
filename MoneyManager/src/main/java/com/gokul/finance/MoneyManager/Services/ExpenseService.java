package com.gokul.finance.MoneyManager.Services;

import com.gokul.finance.MoneyManager.Dto.ExpensDto;
import com.gokul.finance.MoneyManager.Entities.CategoryEntity;
import com.gokul.finance.MoneyManager.Entities.ExpenseEntity;
import com.gokul.finance.MoneyManager.Entities.ProfileEntity;
import com.gokul.finance.MoneyManager.Repository.CategoryRepository;
import com.gokul.finance.MoneyManager.Repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;

         // to add the expens
    public ExpensDto addExpenses(ExpensDto dto) {
     ProfileEntity profile = profileService.getCurrentProfile();
  CategoryEntity categoryEntity = categoryRepository.findById(dto.getCategoryId())
          .orElseThrow(()->new RuntimeException("Category Not found: "));

 ExpenseEntity newExpense =   toEntity(dto,categoryEntity,profile);
      newExpense = expenseRepository.save(newExpense);
      return toDto(newExpense);
    }

    // to Retrieves all expenses from current moth / start date and end date
    public List<ExpensDto> getExpensesForCurrentMoth() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now =  LocalDate.now();
       LocalDate startDate =  now.withDayOfMonth(1);
       LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

      List<ExpenseEntity> list  = expenseRepository.findByProfile_IdAndDateBetween(profile.getId(),startDate,endDate);
      return list.stream().map(this::toDto).toList();
    }

    // to delete the expense by id for current user
    public void deleteexpenseBasedonId(Long expenseId) {
              ProfileEntity profile = profileService.getCurrentProfile();
           ExpenseEntity entity = expenseRepository.findById(expenseId)
                   .orElseThrow(()->new RuntimeException("Expense not Present"));
             if (!entity.getProfile().getId().equals(profile.getId())) {
                 throw new RuntimeException("Unauthorized to delete this expense");
             }

           expenseRepository.deleteById(expenseId);
    }


    // Get latest 5 expense for current users
    public List<ExpensDto> latestTopFiveExpense() {
        ProfileEntity profile = profileService.getCurrentProfile();
      List<ExpenseEntity> list=   expenseRepository.findTop5ByProfile_IdOrderByDateDesc(profile.getId());

       return list.stream().map(this::toDto).toList();
    }

    // total expense for current user
    public BigDecimal getTotalExpense() {
        ProfileEntity profile = profileService.getCurrentProfile();
       BigDecimal total =  expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total!=null?total:BigDecimal.ZERO;
    }


    // filter expense
    public List<ExpensDto> filterExpense(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
 ProfileEntity profile = profileService.getCurrentProfile();
 List<ExpenseEntity> list = expenseRepository.findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
 return  list.stream().map(this::toDto).toList();
    }

    //Notification
public List<ExpensDto> getExpensesForUserOnDate(Long profile_id,LocalDate date) {
       List<ExpenseEntity> list =  expenseRepository.findByProfile_IdAndDate(profile_id,date);
       return list.stream().map(this::toDto).toList();
}

   // helper method
    private ExpenseEntity toEntity(ExpensDto expensDto, CategoryEntity categoryEntity, ProfileEntity profileEntity) {
             return ExpenseEntity.builder()
                     .name(expensDto.getName())
                     .icon(expensDto.getIcon())
                     .amount(expensDto.getAmount())
                     .date(expensDto.getDate())
                     .profile(profileEntity)
                     .category(categoryEntity)
                     .build();
    }

    private ExpensDto toDto(ExpenseEntity expenseEntity) {
       return ExpensDto.builder()
                .id(expenseEntity.getId())
                .name(expenseEntity.getName())
                .icon(expenseEntity.getIcon())
                .amount(expenseEntity.getAmount())
                .categoryId(expenseEntity.getCategory() != null ? expenseEntity.getCategory().getId():null)
                .categoryName(expenseEntity.getCategory() != null ? expenseEntity.getCategory().getName():"N/A")
                .date(expenseEntity.getDate())
                .createdAt(expenseEntity.getCreatedAt())
                .updatedAt(expenseEntity.getUpdatedAt())
                .build();
    }
}
