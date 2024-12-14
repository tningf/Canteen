package com.example.canteen.service;


import com.example.canteen.entity.Cart;
import com.example.canteen.entity.CartItem;
import com.example.canteen.entity.Product;
import com.example.canteen.exception.AppExeception;
import com.example.canteen.exception.ErrorCode;
import com.example.canteen.repository.CartItemRepository;
import com.example.canteen.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartService cartService;


    public void addItemtoCart(Long cartId, Long productId, int quantity){
        //1. Get the cart
        //2. Get the product
        //3. Check if the product is already in the cart
        //4. If Yes, increase the quantity with the requested quantity
        //5. If No, then initiate a new CartItem entry.

        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    public void removeItemFromCart(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    public void updateItemQuantity(Long cartId, Long productId, int quantity){
        Cart cart = cartService.getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    public CartItem getCartItem(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new AppExeception(ErrorCode.PRODUCT_NOT_FOUND));
    }
}