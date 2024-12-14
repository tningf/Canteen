package com.example.canteen.controller;

import com.example.canteen.dto.respone.ApiResponse;
import com.example.canteen.entity.Cart;
import com.example.canteen.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        Cart cart = cartService.getCart(cartId);
        return ResponseEntity.ok(ApiResponse
                .builder()
                .message("Lấy dữ liệu thành công")
                .data(cart)
                .build());
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok(ApiResponse
                .builder()
                .message("Xoá Cart thành công")
                .build());
    }

    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        BigDecimal totalPrice = cartService.getTotalPrice(cartId);
        return ResponseEntity.ok(ApiResponse
                .builder()
                .message("Lấy dữ liệu thành công")
                .data(totalPrice)
                .build());
    }
}
