package com.example.canteen.controller;

import com.example.canteen.constant.PaginationConstants;
import com.example.canteen.dto.dtos.ImageDto;
import com.example.canteen.dto.dtos.ProductDto;
import com.example.canteen.dto.dtos.StockDto;
import com.example.canteen.dto.request.AddProductRequest;
import com.example.canteen.dto.request.ProductUpdateRequest;
import com.example.canteen.dto.response.ApiResponse;

import com.example.canteen.dto.response.PageResponse;
import com.example.canteen.entity.Image;
import com.example.canteen.entity.Product;
import com.example.canteen.exception.AppException;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.service.ImageService;
import com.example.canteen.service.ProductService;
import com.example.canteen.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
@Tag(name = "Product", description = "APIs for managing products in the canteen system")
public class ProductController {
    private final ProductService productService;
    private final ImageService imageService;
    private final StockService stockService;

    // GET
    // Xem tất cả sản phẩm
    @Operation(summary = "Get all products", description = "Retrieve all active products with pagination and sorting")
    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_DIRECTION) String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Product> productPage = productService.getAllActiveProductsPaginated(pageable);
        List<ProductDto> convertedProducts = productService.getConvertProducts(productPage.getContent());

        PageResponse<ProductDto> pageResponse = PageResponse.of(convertedProducts, productPage);

        return ResponseEntity.ok(ApiResponse.builder()
                .message("Lấy dữ liệu thành công")
                .data(pageResponse)
                .build());
    }

    // Xem sản phẩm theo ID
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        ProductDto productDto = productService.covertToDto(product);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Lấy dữ liệu thành công")
                .data(productDto)
                .build());
    }
    // Xem sản phẩm theo category
    @Operation(summary = "Get products by category", description = "Retrieve products by category with pagination")
    @GetMapping("/category/{category}/all")
    public ResponseEntity<ApiResponse> getProductByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_DIRECTION) String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Product> products = productService.getProductByCategoryPaginated(category, pageable);
        List<ProductDto> convertedProducts = productService.getConvertProducts(products.getContent());

        PageResponse<ProductDto> pageResponse = PageResponse.of(convertedProducts, products);

        return ResponseEntity.ok(ApiResponse.builder()
                .message("Lấy dữ liệu thành công")
                .data(pageResponse)
                .build());
    }
    // POST
    // Tạo mới sản phẩm
    @Operation(summary = "Create a new product", description = "Add a new product with images and initial stock")
    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@ModelAttribute AddProductRequest product) {
        // Thêm sản phẩm
        Product savedProduct = productService.addProduct(product);

        // Chuyển đổi sang DTO
        ProductDto productDto = productService.covertToDto(savedProduct);

        // Thêm kho

        StockDto stock = stockService.addStock(savedProduct.getId(), product.getQuantity());

        productDto.setStock(stock);

        List<MultipartFile> images = product.getImages();
        if (images != null && !images.isEmpty()) {
            try {
                // Lưu danh sách ảnh
                List<ImageDto> imageDtos = imageService.saveImages(images, savedProduct.getId());
                productDto.setImages(imageDtos);
            } catch (RuntimeException e) {
                log.error("Failed to upload image: {}", e.getMessage());
                throw new AppException(ErrorCode.FAIL_TO_UPLOAD_IMAGE);
            }
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Thêm sản phẩm thành công")
                .data(productDto)
                .build());
    }

    // PUT
    // Cập nhật sản phẩm theo ID
    @Operation(summary = "Update a product", description = "Update an existing product and its stock or images")
    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@ModelAttribute ProductUpdateRequest request, @PathVariable Long productId) {
        Product theProduct = productService.updateProduct(request, productId);
        ProductDto productDto = productService.covertToDto(theProduct);

        StockDto stock = stockService.updateStock(productId, request.getQuantity());
        productDto.setStock(stock);

        MultipartFile imageFile = request.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Cập nhật ảnh mới
                Image currentImage = imageService.getImageByProductId(productId); // Lấy ảnh hiện tại
                if (currentImage != null) {
                    imageService.updateImage(imageFile, currentImage.getId());
                } else {
                    // Nếu chưa có ảnh, thêm ảnh mới
                    imageService.saveImages(List.of(imageFile), productId);
                }
            } catch (RuntimeException e) {
                throw new AppException(ErrorCode.FAIL_TO_UPLOAD_IMAGE);
            }
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Cập nhật sản phẩm thành công")
                .data(productDto)
                .build());
    }
    // DELETE
    // Xóa mềm sản phẩm
    @Operation(summary = "Delete a product", description = "Soft delete a product by its ID")
    @PreAuthorize("hasAnyRole('KETOAN', 'ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Xóa sản phẩm thành công")
                .build());
    }
}