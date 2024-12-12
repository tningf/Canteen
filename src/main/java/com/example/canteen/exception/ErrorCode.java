package com.example.canteen.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    UNKNOWN(9999, "Lỗi không xác định!", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_EXITED(1001,"User đã tồn tại!", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1002,"User không tồn tại!", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1003, "Bạn chưa xác thực!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1004, "Bạn không có quyền truy cập!", HttpStatus.FORBIDDEN),

    PRODUCT_NOT_FOUND(2001, "Sản phẩm không tồn tại!", HttpStatus.NOT_FOUND),

    CATEGORY_NOT_FOUND(3001,"Không tìm thấy danh mục sản phẩm!" ,HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(3002,"Danh mục sản phẩm này đã tồn tại!" ,HttpStatus.CONFLICT),

    IMAGE_NOT_FOUND(4001,"Không tìm thấy hình ảnh" ,HttpStatus.NOT_FOUND),
    FAIL_TO_UPLOAD_IMAGE(4002,"Không thể tải ảnh lên" ,HttpStatus.INTERNAL_SERVER_ERROR);


    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
}
