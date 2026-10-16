package com.NoenThen.MoneyExpense.Service;


import com.NoenThen.MoneyExpense.Model.Expense;
import com.NoenThen.MoneyExpense.Model.ExpenseImage;
import com.NoenThen.MoneyExpense.Repository.ExpenseImageRepository;
import com.NoenThen.MoneyExpense.Repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
public class ImageService {

        @Autowired
        private ExpenseRepository expenseRepository;

        @Autowired
        private ExpenseImageRepository imageRepository;

        @Value("${file.upload-dir}")
        private String uploadDir;

        //  Upload រូបភាព

    public ExpenseImage uploadImage(UUID expenseId, MultipartFile file) throws IOException {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense រកមិនឃើញ!"));

        //  Check Extension ជំនួស mimeType
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isEmpty()) {
            throw new RuntimeException("File name មិនត្រឹមត្រូវ!");
        }

        String extension = originalName
                .substring(originalName.lastIndexOf(".") + 1)
                .toLowerCase();

        List<String> allowedExtensions = List.of("jpg", "jpeg", "png", "gif", "webp");
        if (!allowedExtensions.contains(extension)) {
            throw new RuntimeException("File ត្រូវតែជារូបភាព! (jpg, png, gif, webp)");
        }

        // Set mimeType ដោយខ្លួនឯង
        String mimeType = switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png"         -> "image/png";
            case "gif"         -> "image/gif";
            case "webp"        -> "image/webp";
            default            -> "image/jpeg";
        };

        // Save File
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String newFileName = UUID.randomUUID() + "." + extension;
        Path filePath = uploadPath.resolve(newFileName);
        Files.copy(file.getInputStream(), filePath);

        // Save Database
        ExpenseImage image = new ExpenseImage();
        image.setExpense(expense);
        image.setImageUrl("/uploads/expenses/" + newFileName);
        image.setFileSize((int) file.getSize());
        image.setMimeType(mimeType);

        return imageRepository.save(image);
    }

        //  Get រូបភាពទាំងអស់ របស់ Expense
        public List<ExpenseImage> getImagesByExpense(UUID expenseId) {
            return imageRepository.findByExpenseId(expenseId);
        }

        //  លុបរូបភាព
        public void deleteImage(UUID imageId) throws IOException {
            ExpenseImage image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Image រកមិនឃើញ!"));

            // លុប File ពី Folder
            String fileName = image.getImageUrl().replace("/uploads/expenses/", "");
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(filePath);

            // លុបពី Database
            imageRepository.delete(image);
        }
    }
