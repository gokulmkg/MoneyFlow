package com.gokul.finance.MoneyManager.Services;



import com.gokul.finance.MoneyManager.Dto.ExpensDto;
import com.gokul.finance.MoneyManager.Dto.IncomeDto;
import com.gokul.finance.MoneyManager.Entities.CategoryEntity;

import com.gokul.finance.MoneyManager.Entities.ExpenseEntity;
import com.gokul.finance.MoneyManager.Entities.IncomeEntity;
import com.gokul.finance.MoneyManager.Entities.ProfileEntity;
import com.gokul.finance.MoneyManager.Repository.CategoryRepository;
import com.gokul.finance.MoneyManager.Repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;

    public IncomeDto addIncomes(IncomeDto dto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity categoryEntity = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()->new RuntimeException("Category Not found: "));

        IncomeEntity newExpense =   toEntity(dto,categoryEntity,profile);
        newExpense = incomeRepository.save(newExpense);
        return toDto(newExpense);
    }


    // to Retrieves all income from current moth / start date and end date
    public List<IncomeDto> getIncomeForCurrentMoth() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now =  LocalDate.now();
        LocalDate startDate =  now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

   List<IncomeEntity> list = incomeRepository.findByProfile_IdAndDateBetween(profile.getId(),startDate,endDate);
     return list.stream().map(this::toDto).toList();
    }

    // to delete the income by id for current user
    public void deleteIncomeBasedonId(Long incomeId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity entity = incomeRepository.findById(incomeId)
                .orElseThrow(()->new RuntimeException("income not Present"));
        if (!entity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }

        incomeRepository.deleteById(incomeId);
    }

    // to get top 5 lastest income
    public List<IncomeDto> latestTopFiveExpense() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list=   incomeRepository.findTop5ByProfile_IdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDto).toList();
    }

    // total income for current user
    public BigDecimal getTotalIncome() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total =  incomeRepository.findTotalIncomeByProfileId(profile.getId());
        return total!=null?total:BigDecimal.ZERO;
    }

    // filter income
    public List<IncomeDto> filterIncome(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
                return  list.stream().map(this::toDto).toList();
    }

    // helper method
    private IncomeEntity toEntity(IncomeDto incomeDto, CategoryEntity categoryEntity, ProfileEntity profileEntity) {
        return IncomeEntity.builder()
                .name(incomeDto.getName())
                .icon(incomeDto.getIcon())
                .amount(incomeDto.getAmount())
                .date(incomeDto.getDate())
                .profile(profileEntity)
                .category(categoryEntity)
                .build();
    }

    private IncomeDto toDto(IncomeEntity incomeEntity) {
        return IncomeDto.builder()
                .id(incomeEntity.getId())
                .name(incomeEntity.getName())
                .icon(incomeEntity.getIcon())
                .amount(incomeEntity.getAmount())
                .categoryId(incomeEntity.getCategory() != null ? incomeEntity.getCategory().getId():null)
                .categoryName(incomeEntity.getCategory() != null ? incomeEntity.getCategory().getName():"N/A")
                .date(incomeEntity.getDate())
                .createdAt(incomeEntity.getCreatedAt())
                .updatedAt(incomeEntity.getUpdatedAt())
                .build();
    }
}
