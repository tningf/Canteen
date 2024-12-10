package com.example.canteen.controller;

import com.example.canteen.entity.Cart;
import com.example.canteen.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addProductToCart(@RequestParam int cartId, @RequestParam int productId, @RequestParam int quantity) {
        Cart cart = cartService.addProductToCart(cartId, productId, quantity);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    @GetMapping("/view")
    public ResponseEntity<Cart> viewCart(@RequestParam int cartId) {
        Optional<Cart> cart = cartService.getCartById(cartId);
        return cart.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}