package com.example.canteen.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXITED(1001,"User đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002,"User không tồn tại", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(1006, "Bạn chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),

    PRODUCT_NOT_FOUND(2001, "Sản phẩm không tồn tại", HttpStatus.NOT_FOUND);


    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
}
