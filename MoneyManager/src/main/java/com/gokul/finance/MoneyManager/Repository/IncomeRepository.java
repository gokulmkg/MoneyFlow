package com.gokul.finance.MoneyManager.Repository;


import com.gokul.finance.MoneyManager.Entities.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity,Long> {


    // 1️⃣ All income by profile, latest first
    List<IncomeEntity> findByProfile_IdOrderByDateDesc(Long profileId);

    // 2️⃣ Top 5 latest incomes
    List<IncomeEntity> findTop5ByProfile_IdOrderByDateDesc(Long profileId);

    // 3️⃣ Search by name + date range + profile
//    List<IncomeEntity> findByProfile_IdAndNameContainingIgnoreCaseAndDateBetween(
//            Long profileId,
//            String name,
//            LocalDate startDate,
//            LocalDate endDate,
//            Sort sort
//    );

    List<IncomeEntity> findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );


    // 4️⃣ Date range only
    List<IncomeEntity> findByProfile_IdAndDateBetween(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate
    );

    // 5️⃣ Total income
    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM IncomeEntity i WHERE i.profile.id = :profileId")
    BigDecimal findTotalIncomeByProfileId(@Param("profileId") Long profileId);
}
