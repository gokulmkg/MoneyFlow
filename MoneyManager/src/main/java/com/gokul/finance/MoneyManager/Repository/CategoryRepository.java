package com.gokul.finance.MoneyManager.Repository;

import com.gokul.finance.MoneyManager.Entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {

    List<CategoryEntity> findByProfile_id(Long profile_id);

    Optional<CategoryEntity>findByIdAndProfile_id(Long id, Long profile_id);

    List<CategoryEntity>findByTypeAndProfile_id(String type,Long profile_id);
    boolean existsByNameAndTypeAndProfile_Id(
            String name,
            String type,
            Long profileId
    );

    Boolean existsByNameAndProfile_Id(String name, Long profileId);

}
