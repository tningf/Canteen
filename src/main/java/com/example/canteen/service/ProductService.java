package com.example.canteen.service;

import com.example.canteen.dto.ImageDto;
import com.example.canteen.dto.ProductDto;
import com.example.canteen.dto.StockDto;
import com.example.canteen.dto.request.AddProductRequest;
import com.example.canteen.dto.request.UpdateProductRequest;
import com.example.canteen.entity.Category;
import com.example.canteen.entity.Image;
import com.example.canteen.entity.Product;
import com.example.canteen.entity.Stock;
import com.example.canteen.exception.AppExeception;
import com.example.canteen.exception.ErrorCode;
import com.example.canteen.mapper.ImageMapper;
import com.example.canteen.mapper.ProductMapper;
import com.example.canteen.mapper.StockMapper;
import com.example.canteen.repository.CategoryRepository;
import com.example.canteen.repository.ImageRepository;
import com.example.canteen.repository.ProductRepository;
import com.example.canteen.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final StockRepository stockRepository;
    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;
    private final StockMapper stockMapper;

    //Add a new product
    public Product addProduct(AddProductRequest request) {
        // Check if the category is found in the database
        // If Yes, set it as the new product category
        // If No, save it as a new category
        // The set as the new product category

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    newCategory.setStatus(true);
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getUnit(),
                request.getPrice(),
                true,
                category
        );
    }

    // Get a product by id
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new AppExeception(ErrorCode.PRODUCT_NOT_FOUND));
    }

    // Update product
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new AppExeception(ErrorCode.PRODUCT_NOT_FOUND));

    }
    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setUnit(request.getUnit());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStatus(true);

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
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
//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }
    //Get all products with status true
    public List<Product> getAllActiveProducts() {
        return productRepository.findAllByStatusTrue();
    }

    // Get all products by category
    public List<Product> getProductByCategory(String category) {
        List<Product> products = productRepository.findByCategoryName(category);
        if (products.isEmpty()) {
            throw new AppExeception(ErrorCode.PRODUCT_NOT_FOUND);
        }
    return products;
    }

    public List<ProductDto> getConvertProducts(List<Product> products) {
        return products.stream()
                .map(this::covertToDto)
                .toList();
    }

    public ProductDto covertToDto(Product product) {
        ProductDto productDto = productMapper.toProductDto(product);

        Stock stock = stockRepository.findByProductId(product.getId());
        StockDto stockDto = stockMapper.toStockDto(stock);
        productDto.setStock(stockDto);

        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(imageMapper::toImageDto)
                .toList();
        productDto.setImages(imageDtos);

        return productDto;
    }
}