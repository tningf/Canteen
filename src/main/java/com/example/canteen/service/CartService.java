package com.example.canteen.service;

import com.example.canteen.entity.Cart;
import com.example.canteen.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Cart addProductToCart(int cartId, int productId, int quantity) {
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    public Optional<Cart> getCartById(int cartId) {
        return cartRepository.findById(cartId);
    }
}