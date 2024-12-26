package com.example.canteen.controller;

import com.example.canteen.dto.OrderDto;
import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.entity.Order;
import com.example.canteen.mapper.OrderMapper;
import com.example.canteen.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllOrders() {
        try {
            List<OrderDto> orders = orderService.getAllOrders();
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Lấy tất cả đơn hàng thành công!")
                    .data(orders)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                    .message("Oops!" + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto orders = orderService.getOrder(orderId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Lấy đơn hàng thành công!")
                    .data(orders)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                    .message("Oops!" + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/{patientId}/orders")
    public ResponseEntity<ApiResponse> getPatientOrders(@PathVariable Long patientId) {
        try {
            List<OrderDto> orders = orderService.getPatientOrders(patientId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Lấy đơn hàng thành công!")
                    .data(orders)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                    .message("Oops!" + e.getMessage())
                    .build());
        }
    }

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long patientId) {
        try {
            Order order = orderService.placeOrder(patientId);
            OrderDto orderDto = orderMapper.convertToDto(order);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Đặt hàng thành công!")
                    .data(orderDto)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .message("Đặt hàng thất bại! " + e.getMessage())
                    .build());
        }
    }

    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<ApiResponse> confirmOrder(@PathVariable Long orderId) {
        try {
            OrderDto order = orderService.confirmOrder(orderId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Xác nhận đơn hàng thành công!")
                    .data(order)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .message("Xác nhận đơn hàng thất bại! " + e.getMessage())
                    .build());
        }
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long orderId) {
        try {
            OrderDto order = orderService.cancelOrder(orderId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Hủy đơn hàng thành công!")
                    .data(order)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .message("Hủy đơn hàng thất bại! " + e.getMessage())
                    .build());
        }
    }
}
