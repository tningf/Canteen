package com.example.canteen.controller;


import com.example.canteen.dto.ProductDto;
import com.example.canteen.dto.request.AddProductRequest;
import com.example.canteen.dto.request.UpdateProductRequest;
import com.example.canteen.dto.respone.ApiResponse;
import com.example.canteen.entity.Product;
import com.example.canteen.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    // GET
    // Xem tất cả sản phẩm
    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertProducts(products);
        return ResponseEntity.ok(new ApiResponse(1000, "Success", convertedProducts));
    }
    // Xem sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        ProductDto productDto = productService.covertToDto(product);
        return ResponseEntity.ok(new ApiResponse(1000, "Success", productDto));
    }
    // Xem sản phẩm theo category
    @GetMapping("/category/{category}/all")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductByCategory(category);
        List<ProductDto> convertedProducts = productService.getConvertProducts(products);
        return ResponseEntity.ok(new ApiResponse(1000, "Success", convertedProducts));
    }

    // POST
    // Tạo mới sản phẩm
    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@RequestBody AddProductRequest product) {
        Product savedProduct = productService.addProduct(product);
        ProductDto productDto = productService.covertToDto(savedProduct);
        return ResponseEntity.ok(new ApiResponse(1000, "Thêm thành công!", productDto));
    }

    // PUT
    // Cập nhật sản phẩm theo ID
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable Long productId) {
        Product theProduct = productService.updateProduct(request, productId);
        ProductDto productDto = productService.covertToDto(theProduct);
        return ResponseEntity.ok(new ApiResponse(1000, "Cập nhật thành công", productDto));
    }
    // DELETE
    // Xóa mềm sản phẩm
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(new ApiResponse(1000, "Xóa thành công!", productId));
    }
}