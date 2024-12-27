package com.example.canteen.exception;

import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(errorCode.getSuccess());
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleGenericException(Exception exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(ErrorCode.UNKNOWN.getSuccess());
        apiResponse.setMessage(ErrorCode.UNKNOWN.getMessage()+" " + exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
