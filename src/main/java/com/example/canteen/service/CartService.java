package com.example.canteen.service;


import com.example.canteen.entity.Cart;
import com.example.canteen.entity.CartItem;
import com.example.canteen.exception.AppExeception;
import com.example.canteen.exception.ErrorCode;
import com.example.canteen.repository.CartItemRepository;
import com.example.canteen.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    public Cart getCart(Long id){
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new AppExeception(ErrorCode.CART_NOT_FOUND));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    public void clearCart(Long id){
        Cart cart = getCart(id);
        if (cart.getItems().isEmpty()) {
            throw new AppExeception(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    public BigDecimal getTotalPrice(Long id){
        Cart cart = getCart(id);
        if (cart.getItems().isEmpty()) {
            throw new AppExeception(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        return cart.getTotalAmount();
    }

    public Long initializeNewCart() {
        Cart newCart = new Cart();
//        Long newCartId = cartIdGenerator.incrementAndGet();
//        newCart.setId(newCartId);
        return cartRepository.save(newCart).getId();

    }
}
