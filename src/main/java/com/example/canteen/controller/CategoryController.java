package com.example.canteen.controller;


import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.entity.Category;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllActiveCategories();
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(categories)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.UNKNOWN.getHttpStatusCode())
                    .body(new ApiResponse(ErrorCode.UNKNOWN.getSuccess(), ErrorCode.UNKNOWN.getMessage(), null));
        }
    }

    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
        Category newCategory = categoryService.addCategory(name);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Thêm danh mục sản phẩm thành công!")
                .data(newCategory)
                .build());
    }

    @GetMapping("/category/{id}/get-by-id")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .data(category)
                .build());
    }

    @GetMapping("category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(ApiResponse.builder()
                .data(category)
                .build());
    }
    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @PutMapping("category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory( @PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(category, id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Cập nhật danh mục sản phẩm thành công!")
                .data(updatedCategory)
                .build());
    }
    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @DeleteMapping("category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.builder()
                .message("Xóa danh mục sản phẩm thành công!")
                .build());
    }
}
