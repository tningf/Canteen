package com.example.canteen.controller;


import com.example.canteen.dto.request.CreateStockRequest;
import com.example.canteen.dto.dtos.StockDto;
import com.example.canteen.dto.request.StockUpdateRequest;
import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.mapper.StockMapper;
import com.example.canteen.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;
    private final StockMapper stockMapper;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllStocks() {
        List<StockDto> stocks = stockService.getAllStock().stream()
                .map(stockMapper::toStockDto)
                .toList();
        return ResponseEntity.ok(ApiResponse.builder().data(stocks).build());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_KETOAN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addStock(@RequestBody CreateStockRequest request) {
        StockDto stock =  stockService.addStock(request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(ApiResponse.builder().data(stock).build());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_KETOAN')")
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateStock(@RequestBody StockUpdateRequest request, @PathVariable Long id) {
        StockDto stock = stockService.updateStock(id, request.getQuantity());
        return ResponseEntity.ok(ApiResponse.builder().data(stock).build());
    }

}