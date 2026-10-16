package com.NoenThen.MoneyExpense.Controller;

import com.NoenThen.MoneyExpense.DTO.ApiResponse;
import com.NoenThen.MoneyExpense.DTO.CategoryRequestDTO;
import com.NoenThen.MoneyExpense.DTO.CategoryResponseDTO;
import com.NoenThen.MoneyExpense.Model.Categories;
import com.NoenThen.MoneyExpense.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> createCategory(
            @RequestBody CategoryRequestDTO dto) {

        CategoryResponseDTO result = categoryService.createCategory(dto);

        ApiResponse<CategoryResponseDTO> response =
                new ApiResponse<>(
                        true,
                        "Category created successfully!",
                        result
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getCategoriesByUser(
            @PathVariable UUID userId) {

        List<CategoryResponseDTO> data = categoryService.getCategoriesByUser(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Categories loaded", data)
        );
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable UUID id,
            @RequestBody CategoryRequestDTO dto) {          //  RequestDTO, not ResponseDTO
        return ResponseEntity.ok(categoryService.updateCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();          // ✅ 204 No Content
    }
}