package com.gokul.finance.MoneyManager.Controller;

import com.gokul.finance.MoneyManager.Dto.CategoryDto;
import com.gokul.finance.MoneyManager.Dto.ProfileDto;
import com.gokul.finance.MoneyManager.Entities.ProfileEntity;
import com.gokul.finance.MoneyManager.Repository.CategoryRepository;
import com.gokul.finance.MoneyManager.Services.CategoryService;
import com.gokul.finance.MoneyManager.Services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping("/createnew")
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody CategoryDto categoryDto) {
      CategoryDto savesCategory =  categoryService.saveCategory(categoryDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(savesCategory);
    }

// to get category for current user
    @GetMapping("getAll")
   public ResponseEntity<List<CategoryDto>> getCategory() {
        List<CategoryDto> categoryDtos = categoryService.getCurrentprofilebyid();
        return ResponseEntity.ok(categoryDtos);
    }


    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDto>> getAllCategoryByTypeForCurrentUser(@PathVariable String type) {
       List<CategoryDto> list = categoryService.getByTypeforCurrentUsers(type);
       return ResponseEntity.ok(list);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategoryDto(@PathVariable Long categoryId,@RequestBody CategoryDto categoryDto) {
      CategoryDto updatedCategoryDto = categoryService.updateCategoryDto(categoryId,categoryDto);
    return ResponseEntity.ok(updatedCategoryDto);
    }



}
