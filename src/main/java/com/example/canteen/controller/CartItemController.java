package com.example.canteen.controller;


import com.example.canteen.dto.respone.ApiResponse;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.service.CartItemService;
import com.example.canteen.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cartItems")
public class CartItemController {
    private final CartItemService cartItemService;
    private final CartService cartService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam (required = false) Long cartId,
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            if (cartId == null) {
                cartId = cartService.initializeNewCart();
            }

            cartItemService.addItemtoCart(cartId,productId,quantity);
            return ResponseEntity.ok(ApiResponse
                    .builder()
                    .message("Thêm sản phẩm vào giỏ hàng thành công")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.CART_ITEM_NOT_FOUND.getHttpStatusCode()).body(ApiResponse
                    .builder()
                    .code(ErrorCode.CART_ITEM_NOT_FOUND.getCode())
                    .message(e.getMessage())
                    .build());
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        try {
            cartItemService.removeItemFromCart(cartId,itemId);
            return ResponseEntity.ok(ApiResponse
                    .builder()
                    .message("Xóa sản phẩm khỏi giỏ hàng thành công")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.CART_ITEM_NOT_FOUND.getHttpStatusCode()).body(ApiResponse
                    .builder()
                    .code(ErrorCode.CART_ITEM_NOT_FOUND.getCode())
                    .message(ErrorCode.CART_ITEM_NOT_FOUND.getMessage())
                    .build());
        }
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,@PathVariable Long itemId,@RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId,itemId,quantity);
            return ResponseEntity.ok(ApiResponse
                    .builder()
                    .message("Cập nhật sản phẩm trong giỏ hàng thành công")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.CART_ITEM_NOT_FOUND.getHttpStatusCode()).body(ApiResponse
                    .builder()
                    .code(ErrorCode.CART_ITEM_NOT_FOUND.getCode())
                    .message(ErrorCode.CART_ITEM_NOT_FOUND.getMessage())
                    .build());
        }
    }
}
