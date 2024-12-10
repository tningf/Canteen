package com.example.canteen.service;

import com.example.canteen.entity.Product;
import com.example.canteen.exception.AppExeception;
import com.example.canteen.exception.ErrorCode;
import com.example.canteen.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Add a new product
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Update an existing product
    public Product updateProduct(Long productId, Product updatedProduct) {
        Optional<Product> existingProductOpt = productRepository.findById(productId);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setUnit(updatedProduct.getUnit());
            existingProduct.setSellPrice(updatedProduct.getSellPrice());
            existingProduct.setStatus(updatedProduct.isStatus());
            existingProduct.setCategoryId(updatedProduct.getCategoryId());
            return productRepository.save(existingProduct);
        } else {
            throw new AppExeception(ErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    // Soft delete a product
    public void deleteProduct(Long productId) {
        Optional<Product> existingProductOpt = productRepository.findById(productId);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setStatus(false);
            productRepository.save(existingProduct);
        } else {
            throw new AppExeception(ErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

}