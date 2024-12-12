package com.example.canteen.controller;


import com.example.canteen.dto.respone.ApiResponse;
import com.example.canteen.entity.Category;
import com.example.canteen.exception.ErrorCode;
import com.example.canteen.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse(1000, "Thành công!", categories));
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.UNKNOWN.getHttpStatusCode()).body(new ApiResponse(ErrorCode.UNKNOWN.getCode(), ErrorCode.UNKNOWN.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
        try {
            Category newCategory = categoryService.addCategory(name);
            return ResponseEntity.ok(new ApiResponse(1000, "Thành công!", newCategory));
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.CATEGORY_ALREADY_EXISTS.getHttpStatusCode()).body(new ApiResponse(ErrorCode.CATEGORY_ALREADY_EXISTS.getCode(), ErrorCode.CATEGORY_ALREADY_EXISTS.getMessage(), null));
        }
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse(1000, "Tìm thấy!", category));
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.CATEGORY_NOT_FOUND.getHttpStatusCode()).body(new ApiResponse(ErrorCode.CATEGORY_NOT_FOUND.getCode(), e.getMessage(), null));
        }
    }

    @GetMapping("category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse(1000, "Tìm thấy!", category));
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.CATEGORY_NOT_FOUND.getHttpStatusCode()).body(new ApiResponse(ErrorCode.CATEGORY_NOT_FOUND.getCode(), e.getMessage(), null));
        }
    }

    @DeleteMapping("category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse(1000, "Xóa thành !", null));
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.CATEGORY_NOT_FOUND.getHttpStatusCode()).body(new ApiResponse(ErrorCode.CATEGORY_NOT_FOUND.getCode(), e.getMessage(), null));
        }
    }

    @PutMapping("category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory( @PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse(1000, "Cập nhật thành công!", updatedCategory));
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.CATEGORY_NOT_FOUND.getHttpStatusCode()).body(new ApiResponse(ErrorCode.CATEGORY_NOT_FOUND.getCode(), e.getMessage(), null));
        }
    }
}
