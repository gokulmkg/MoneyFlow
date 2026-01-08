package com.gokul.finance.MoneyManager.Repository;

import com.gokul.finance.MoneyManager.Entities.ExpenseEntity;
import com.gokul.finance.MoneyManager.Entities.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity,Long> {

    // 1️⃣ Get all expenses by profile, sorted by date desc
    List<ExpenseEntity> findByProfile_IdOrderByDateDesc(Long profileId);

    // 2️⃣ Get top 5 latest expenses
    List<ExpenseEntity> findTop5ByProfile_IdOrderByDateDesc(Long profileId);

//    // 3️⃣ Search by name + date range + profile
//    List<ExpenseEntity> findByProfile_IdAndNameContainingIgnoreCaseAndDateBetween(
//            Long profileId,
//            String name,
//            LocalDate startDate,
//            LocalDate endDate,
//            Sort sort
//    );

    List<ExpenseEntity> findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );

    List<ExpenseEntity> findByProfile_IdAndDate(Long profileId,LocalDate date);


    // 4️⃣ Date range only
    List<ExpenseEntity> findByProfile_IdAndDateBetween(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate
    );

    // 5️⃣ Total expense (JPQL)
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);
}



