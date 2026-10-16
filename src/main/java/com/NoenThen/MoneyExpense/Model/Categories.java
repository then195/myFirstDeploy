package com.NoenThen.MoneyExpense.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Categories {

@Id
@GeneratedValue(strategy = GenerationType.UUID)
@Column(columnDefinition = "UNIQUEIDENTIFIER")
private UUID id;

// Category → User (NULL = Default Category)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = true)
@JsonIgnore
private User user;

@Column(name = "name", nullable = false, length = 100)
private String name;

@Column(name = "icon", length = 50)
private String icon;

@Column(name = "color", length = 10)
private String color;

@Column(name = "is_default")
private Boolean isDefault = false;

// Category(1) → Expenses(∞)
@OneToMany(mappedBy = "category")  // ← ប្ដូរ "categories" → "category"
@JsonIgnore
private List<Expense> expenses;
}