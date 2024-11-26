package com.example.canteen.controller;


import com.example.canteen.entity.Product;
import com.example.canteen.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
    public class ProductController {
        @Autowired
        private ProductService productService;

        @GetMapping
        public List<Product> getAllProducts() {
            return productService.getAllProducts().stream()
                    .map(product -> new Product(
                            product.getProductId(),
                            product.getProductName(),
                            product.getUnit(),
                            product.getSellPrice(),
                            product.getStatus(),
                            product.getCategoryId()
                    ))
                    .collect(Collectors.toList());
        }

    // Thêm mới sản phẩm
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // Chỉnh sửa sản phẩm
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        Product updated = productService.updateProduct(productId, updatedProduct);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Xóa sản phẩm (ẩn sản phẩm)
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Trả về 204 No Content khi thành công
    }
}
