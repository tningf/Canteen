package com.example.canteen.enums;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    UNKNOWN( "Oops!", HttpStatus.INTERNAL_SERVER_ERROR),

    USER_NOT_FOUND("User không tồn tại!", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("User đã tồn tại!", HttpStatus.CONFLICT),
    UNAUTHENTICATED("Bạn chưa xác thực!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("Bạn không có quyền truy cập!", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_ROLE_MODIFICATION("Bạn không có đủ quyền!", HttpStatus.FORBIDDEN),

    PRODUCT_NOT_FOUND("Sản phẩm không tồn tại!", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS("Sản phẩm đã tồn tại!", HttpStatus.CONFLICT),

    CATEGORY_NOT_FOUND("Không tìm thấy danh mục sản phẩm!" ,HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS("Danh mục sản phẩm này đã tồn tại!" ,HttpStatus.CONFLICT),

    IMAGE_NOT_FOUND("Không tìm thấy hình ảnh!" ,HttpStatus.NOT_FOUND),
    FAIL_TO_UPLOAD_IMAGE("Không thể tải ảnh lên!" ,HttpStatus.INTERNAL_SERVER_ERROR),

    CART_NOT_FOUND("Không tìm thấy giỏ hàng!", HttpStatus.NOT_FOUND),
    CART_ALREADY_EXISTS("Giỏ hàng đã tồn tại!", HttpStatus.CONFLICT),
    CART_ITEM_NOT_FOUND("Không tìm thấy sản phẩm trong giỏ hàng!", HttpStatus.NOT_FOUND),
    CART_ITEM_ALREADY_EXISTS("Sản phẩm đã tồn tại trong giỏ hàng!", HttpStatus.CONFLICT),
    INVALID_QUANTITY("Số lượng sản phẩm phải lớn hơn 0!" ,HttpStatus.BAD_REQUEST),
    CART_UPDATE_FAILED("Cập nhật giỏ hàng thất bại!" ,HttpStatus.INTERNAL_SERVER_ERROR),

    ORDER_NOT_FOUND("Không tìm thấy Order", HttpStatus.NOT_FOUND ),
    INVALID_STATUS_TRANSITION_CANCELED("Không thể cập nhật đơn hàng đã bị hủy!" , HttpStatus.BAD_REQUEST),
    INVALID_STATUS_TRANSITION_CONFIRMED_TO_PENDING("Không thể cập nhật đơn hàng từ Đã xác nhận sang Đang chờ xác nhận!", HttpStatus.BAD_REQUEST),
    INVALID_STATUS_TRANSITION_EXPIRED("Không thể hủy đơn hàng Đã xác nhận quá 15 phút!" , HttpStatus.BAD_REQUEST),
    INVALID_STATUS_TRANSITION_CANCELED_TO_CANCELED("Đơn hàng đã ở trạng thái Đã hủy bỏ!" , HttpStatus.BAD_REQUEST),
    
    PATIENT_NOT_FOUND("Không tìm thấy bệnh nhân!", HttpStatus.NOT_FOUND),
    INSUFFICIENT_BALANCE("Không đủ số dư!", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK("Sản phẩm đã hết hàng hoặc số lượng mua lớn hơn sản phẩm còn lại!" , HttpStatus.BAD_REQUEST),

    INVALID_DATE_RANGE("Khoảng thời gian không hợp lệ", HttpStatus.BAD_REQUEST),
    FUTURE_DATE_NOT_ALLOWED("Không thể thống kê cho ngày trong tương lai", HttpStatus.BAD_REQUEST),

    INVALID_REQUEST("Request không hợp lệ" , HttpStatus.BAD_REQUEST),
    DEPARTMENT_NOT_FOUND("Khoa không tồn tại!" ,HttpStatus.NOT_FOUND );

    private final Boolean success = false;
    private String message;
    private HttpStatusCode httpStatusCode;
}
