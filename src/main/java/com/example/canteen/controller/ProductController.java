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

    // GET
    // Xem tất cả sản phẩm
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts().stream()
                //.filter(Product::isStatus)
                .map(product -> new Product(
                        product.getProductId(),
                        product.getProductName(),
                        product.getUnit(),
                        product.getSellPrice(),
                        product.isStatus(),
                        product.getCategoryId()
                ))
                .collect(Collectors.toList());
    }

    // POST
    // Tạo mới sản phẩm
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // PUT
    // Chỉnh sửa sản phẩm theo ID
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        Product updated = productService.updateProduct(productId, updatedProduct);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
    // DELETE
    // Xóa mềm sản phẩm
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // HTTP Code 204: No Content
    }
}