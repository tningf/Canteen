package com.example.canteen.service;


import com.example.canteen.entity.Cart;
import com.example.canteen.entity.Patient;
import com.example.canteen.exception.AppException;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.repository.CartItemRepository;
import com.example.canteen.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    public Cart getCart(Long id){
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long id){
        Cart cart = getCart(id);
        if (cart.getItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    public BigDecimal getTotalPrice(Long id){
        Cart cart = getCart(id);
        if (cart.getItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        return cart.getTotalAmount();
    }

    public Cart initializeNewCart(Patient patient) {
        return Optional.ofNullable(cartRepository.findByPatientId(patient.getId()))
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setPatient(patient);
                    return cartRepository.save(cart);
                });
    }

    public Cart getCartByPatientId(Long patientId) {
        Cart cart = cartRepository.findByPatientId(patientId);
        if (cart == null) {
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        }
        return cart;
    }
}
