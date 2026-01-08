package com.gokul.finance.MoneyManager.Services;

import com.gokul.finance.MoneyManager.Dto.CategoryDto;
import com.gokul.finance.MoneyManager.Entities.CategoryEntity;
import com.gokul.finance.MoneyManager.Entities.ProfileEntity;
import com.gokul.finance.MoneyManager.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private  final ProfileService profileService;


    // to save category
    public CategoryDto saveCategory(CategoryDto categoryDto) {
       ProfileEntity profile =  profileService.getCurrentProfile();
        if (categoryRepository.existsByNameAndTypeAndProfile_Id(
                categoryDto.getName(),
                categoryDto.getType(),
                profile.getId())) {

            throw new RuntimeException(
                    categoryDto.getType() +
                            " category with this name already exists"
            );
        }
        CategoryEntity newCategory = toEntity(categoryDto,profile);

        newCategory = categoryRepository.save(newCategory);
          return toDto(newCategory);
    }

    // to get category for current user
    public List<CategoryDto> getCurrentprofilebyid() {
       ProfileEntity profiles =  profileService.getCurrentProfile();
       List<CategoryEntity> categories =  categoryRepository.findByProfile_id(profiles.getId());
       //CategoryDto categoryDto = toDto((CategoryEntity) categories);
     //   return categoryDto;
        return categories.stream().map(this::toDto).toList();
    }

    // get by type for currentUser
    public List<CategoryDto> getByTypeforCurrentUsers(String type) {
        ProfileEntity profile =  profileService.getCurrentProfile();
          List<CategoryEntity> categories = categoryRepository.findByTypeAndProfile_id(type,profile.getId());
             return categories.stream().map(this::toDto).toList();
    }

    // update the category

    public CategoryDto updateCategoryDto(Long categoryId, CategoryDto dto) {
         ProfileEntity profile = profileService.getCurrentProfile();
       CategoryEntity existingCategory =  categoryRepository.findByIdAndProfile_id(categoryId,profile.getId())
               .orElseThrow(() ->new RuntimeException("Category not found or not accessible"));
       existingCategory.setName(dto.getName());
       existingCategory.setType(dto.getType());
       existingCategory.setIcon(dto.getIcon());
       existingCategory =  categoryRepository.save(existingCategory);
         return toDto(existingCategory);

    }


    // helper methods
    private CategoryEntity toEntity(CategoryDto categoryDto, ProfileEntity profileEntity) {
        return CategoryEntity.builder()
                .name(categoryDto.getName())
                .icon(categoryDto.getIcon())
                .profile(profileEntity)
                .type(categoryDto.getType())
                .build();
    }

    private CategoryDto toDto(CategoryEntity categoryEntity) {
        return CategoryDto.builder()
                .id(categoryEntity.getId())
                .profile_id(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .createdAt(categoryEntity.getCreatedAt())
                .updatesAt(categoryEntity.getUpdatedAt())
                .type(categoryEntity.getType())
                .build();
    }

}
