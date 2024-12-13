package com.example.canteen.controller;


import com.example.canteen.dto.request.CreateStockRequest;
import com.example.canteen.dto.StockDto;
import com.example.canteen.dto.request.UpdateStockRequest;
import com.example.canteen.dto.respone.ApiResponse;
import com.example.canteen.mapper.StockMapper;
import com.example.canteen.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;
    private final StockMapper stockMapper;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllStocks() {
        List<StockDto> stocks = stockService.getAllStocks().stream()
                .map(stockMapper::toStockDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse(1000, "Success", stocks));
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addStock(@RequestBody CreateStockRequest request) {
        StockDto stock =  stockService.addStock(request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(new ApiResponse(1000, "Success", stock));
    }


    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateStock(@RequestBody UpdateStockRequest request, @PathVariable Long id) {
        StockDto stock = stockService.updateStock(id, request.getQuantity());
        return ResponseEntity.ok(new ApiResponse(1000, "Success", stock));
    }

}