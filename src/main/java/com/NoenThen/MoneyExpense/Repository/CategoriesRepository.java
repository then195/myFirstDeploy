// CategoriesRepository.java
package com.NoenThen.MoneyExpense.Repository;

import com.NoenThen.MoneyExpense.Model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoriesRepository extends JpaRepository<Categories, UUID> {
    List<Categories> findByUserId(UUID userId);

    List<Categories> findByUserIdOrUserIsNull(UUID id);

    void deleteByUserId(UUID id);

    Categories findByUserIdAndName(UUID userId, String name);

    boolean existsByNameAndUserId(String name, UUID userId);
}