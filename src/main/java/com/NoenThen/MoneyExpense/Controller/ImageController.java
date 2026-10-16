package com.NoenThen.MoneyExpense.Controller;

import com.NoenThen.MoneyExpense.DTO.ApiResponse;
import com.NoenThen.MoneyExpense.Model.ExpenseImage;
import com.NoenThen.MoneyExpense.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses/{expenseId}/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    // POST — Upload រូបភាព
    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseImage>> upload(
            @PathVariable UUID expenseId,
            @RequestParam("file") MultipartFile file) {
        try {
            ExpenseImage image = imageService.uploadImage(expenseId, file);
            return ResponseEntity.ok(
                    ApiResponse.success("Upload រូបភាពដោយជោគជ័យ!", image)
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    ApiResponse.fail(e.getMessage())
            );
        }
    }

    // GET — List រូបភាព
    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseImage>>> getImages(
            @PathVariable UUID expenseId) {
        List<ExpenseImage> images = imageService.getImagesByExpense(expenseId);
        return ResponseEntity.ok(
                ApiResponse.success("ទទួលបានរូបភាពដោយជោគជ័យ!", images)
        );
    }

    // DELETE — លុបរូបភាព
    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(
            @PathVariable UUID expenseId,
            @PathVariable UUID imageId) {
        try {
            imageService.deleteImage(imageId);
            return ResponseEntity.ok(
                    ApiResponse.success("លុបរូបភាពដោយជោគជ័យ!")
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    ApiResponse.fail(e.getMessage())
            );
        }
    }
}