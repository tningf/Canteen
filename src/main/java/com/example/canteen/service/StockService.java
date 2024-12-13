package com.example.canteen.service;

import com.example.canteen.dto.StockDto;
import com.example.canteen.entity.Product;
import com.example.canteen.entity.Stock;
import com.example.canteen.exception.AppExeception;
import com.example.canteen.exception.ErrorCode;
import com.example.canteen.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductService productService;
    private final StockRepository stockRepository;

    // Get stock by id
    public Stock getStockById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new AppExeception(ErrorCode.UNKNOWN));
    }

    // Add quantity to stock
    public StockDto addStock(Long productId, int quantity) {
        Product product = productService.getProductById(productId);
            if (stockRepository.existsByProduct(product)) {
                throw new AppExeception(ErrorCode.PRODUCT_ALREADY_EXISTS);
            }
            Stock stock = new Stock();
            stock.setProduct(product);
            stock.setQuantity(quantity);
            stockRepository.save(stock);
        return new StockDto(stock.getQuantity());
    }

    // Update stock quantity by product id
    public StockDto updateStock(Long productId, int quantity) {
        Stock stock = stockRepository.findByProductId(productId);
        stock.setQuantity(quantity);
        stockRepository.save(stock);
        return new StockDto(stock.getQuantity());
    }

    //Get all stocks
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
}
