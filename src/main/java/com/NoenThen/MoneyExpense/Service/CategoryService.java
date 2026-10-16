package com.NoenThen.MoneyExpense.Service;

import com.NoenThen.MoneyExpense.DTO.CategoryRequestDTO;
import com.NoenThen.MoneyExpense.DTO.CategoryResponseDTO;
import com.NoenThen.MoneyExpense.Model.Categories;
import com.NoenThen.MoneyExpense.Model.User;
import com.NoenThen.MoneyExpense.Repository.CategoriesRepository;
import com.NoenThen.MoneyExpense.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        Categories category = new Categories();

        // User is nullable (null = default/system category)
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));
            category.setUser(user);
        }
        if (dto.getName() != null) {
            boolean exists = categoriesRepository.existsByNameAndUserId(dto.getName(), dto.getUserId());

            if (exists) {
                throw new RuntimeException(
                        "Category name already exists for this user: " + dto.getName()
                );
            }
        }
        category.setName(dto.getName());


        category.setIcon(dto.getIcon());
        category.setColor(dto.getColor());
        category.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false);

        return toResponseDTO(categoriesRepository.save(category));
    }

    // Get categories for a specific user
    public List<CategoryResponseDTO> getCategoriesByUser(UUID userId) {
        return categoriesRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Get all categories (admin)
    public List<CategoryResponseDTO> getAllCategories() {
        return categoriesRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public CategoryResponseDTO updateCategory(UUID id, CategoryRequestDTO dto) {  // ✅ use RequestDTO
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found: " + id));

        if (dto.getName() != null && !dto.getName().isBlank())
            category.setName(dto.getName());
        if (dto.getIcon() != null && !dto.getIcon().isBlank())
            category.setIcon(dto.getIcon());
        if (dto.getColor() != null && !dto.getColor().isBlank())
            category.setColor(dto.getColor());

        return toResponseDTO(categoriesRepository.save(category));
    }

    public void deleteCategory(UUID id) {
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found: " + id));
        categoriesRepository.delete(category);
    }


    private CategoryResponseDTO toResponseDTO(Categories c) {
        return CategoryResponseDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .icon(c.getIcon())
                .color(c.getColor())
                .isDefault(c.getIsDefault())
                .userId(c.getUser() != null ? c.getUser().getId() : null)
                .build();
    }
}