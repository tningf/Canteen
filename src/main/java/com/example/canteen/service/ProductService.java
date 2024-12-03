package com.example.canteen.service;

import com.example.canteen.entity.Product;
import com.example.canteen.exception.ResourceNotFoundException;
import com.example.canteen.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Danh sách tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Tạo mới sản phẩm
    public Product createProduct(Product product) {
        // Validate product name before saving
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new RuntimeException("Product name cannot be empty");
        }
        return productRepository.save(product);
    }

    // Cập nhật sản phẩm
    public Product updateProduct(Long productId, Product updatedProduct) {
        // Kiểm tra xem sản phẩm có tồn tại không
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        // Cập nhật các chi tiết của sản phẩm
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setUnit(updatedProduct.getUnit());
        existingProduct.setSellPrice(updatedProduct.getSellPrice());
        existingProduct.setStatus(updatedProduct.isStatus());
        existingProduct.setCategoryId(updatedProduct.getCategoryId());

        // Lưu và trả về sản phẩm đã được cập nhật
        return productRepository.save(existingProduct);
    }

    // Xóa mềm sản phẩm
    public void deleteProduct(Long productId) {
        // Kiểm tra xem sản phẩm có tồn tại không
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        // Đánh dấu trạng thái sản phẩm là "false" để soft delete
        product.setStatus(false);
        productRepository.save(product);  // Lưu thay đổi vào cơ sở dữ liệu
    }
}
