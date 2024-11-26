package com.example.canteen.service;

import com.example.canteen.entity.Product;
import com.example.canteen.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Thêm mới sản phẩm
    public Product createProduct(Product product) {
        // Kiểm tra thông tin sản phẩm trước khi lưu
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        return productRepository.save(product);
    }

    // Chỉnh sửa thông tin sản phẩm
    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Cập nhật các trường thông tin
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setUnit(updatedProduct.getUnit());
        existingProduct.setSellPrice(updatedProduct.getSellPrice());
        existingProduct.setStatus(updatedProduct.getStatus());
        existingProduct.setCategoryId(updatedProduct.getCategoryId());

        return productRepository.save(existingProduct);
    }

    // Xóa sản phẩm (ẩn sản phẩm)
    public Product deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Ẩn sản phẩm thay vì xóa
        //product.setDeleted(true);
        return productRepository.save(product);
    }
}