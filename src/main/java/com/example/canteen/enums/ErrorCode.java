package com.example.canteen.enums;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    UNKNOWN(9999, "Oops!", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_ALREADY_EXISTS(1001,"User đã tồn tại!", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1002,"User không tồn tại!", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1003, "Bạn chưa xác thực!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1004, "Bạn không có quyền truy cập!", HttpStatus.FORBIDDEN),

    PRODUCT_NOT_FOUND(2001, "Sản phẩm không tồn tại!", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS(2002, "Sản phẩm đã tồn tại!", HttpStatus.CONFLICT),
    CATEGORY_NOT_FOUND(3001,"Không tìm thấy danh mục sản phẩm!" ,HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(3002,"Danh mục sản phẩm này đã tồn tại!" ,HttpStatus.CONFLICT),

    IMAGE_NOT_FOUND(4001,"Không tìm thấy hình ảnh" ,HttpStatus.NOT_FOUND),
    FAIL_TO_UPLOAD_IMAGE(4002,"Không thể tải ảnh lên" ,HttpStatus.INTERNAL_SERVER_ERROR),

    CART_NOT_FOUND(5001,"Không tìm thấy giỏ hàng", HttpStatus.NOT_FOUND),
    CART_ALREADY_EXISTS(5002,"Giỏ hàng đã tồn tại", HttpStatus.CONFLICT),
    CART_ITEM_NOT_FOUND(5003,"Không tìm thấy sản phẩm trong giỏ hàng", HttpStatus.NOT_FOUND),
    CART_ITEM_ALREADY_EXISTS(5004,"Sản phẩm đã tồn tại trong giỏ hàng", HttpStatus.CONFLICT),

    ORDER_NOT_FOUND(6001,"Không tìm thấy Order",HttpStatus.NOT_FOUND ),;



    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
}
