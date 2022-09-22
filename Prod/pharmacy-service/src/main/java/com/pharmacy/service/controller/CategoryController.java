package com.pharmacy.service.controller;

import com.pharmacy.service.dto.CategoryDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pharmacy/category")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @GetMapping(value = "/get-category")
    public ResponseEntity<PageableResponse<CategoryDto>> getCategories(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategories(pageNumber, pageSize));

    }

    @GetMapping(value = "/get-all-category")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategories());
    }

    @GetMapping(value = "/get-category/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategory(categoryId));
    }

    @GetMapping(value = "/get-category/name/{name}")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable("name") String categoryName) {
        return ResponseEntity.ok(categoryService.getCategory(categoryService.getCategoryDtoByName(categoryName).getCategoryId()));

    }

    @PostMapping(value = "/create-category")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDto));
    }

    @PutMapping(value = "/update-category")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.updateCategory(categoryDto));
    }

    @DeleteMapping(value = "/delete-category/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().body("Deleted successfully!");
    }

    @DeleteMapping(value = "/delete-category/name/{name}")
    public ResponseEntity<String> deleteCategoryByName(@PathVariable("name") String categoryName) {
        categoryService.deleteCategory(categoryService.getCategoryDtoByName(categoryName).getCategoryId());
        return ResponseEntity.ok().body("Deleted!");
    }
}
