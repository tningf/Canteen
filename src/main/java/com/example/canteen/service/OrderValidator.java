package com.example.canteen.service;

import com.example.canteen.entity.Order;
import com.example.canteen.enums.OrderStatus;
import com.example.canteen.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.example.canteen.enums.ErrorCode;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
class OrderValidator {

    public void validateConfirmation(Order order) {
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new AppException(
                    ErrorCode.INVALID_STATUS_TRANSITION_CANCELED
            );
        }
    }

    public void validateCancellation(Order order) {
        if (order.getOrderStatus() == OrderStatus.CANCELED) {
            throw new AppException(
                    ErrorCode.INVALID_STATUS_TRANSITION_CANCELED_TO_CANCELED
            );
        }

        if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime confirmedAt = order.getConfirmedAt();

//            if (confirmedAt == null) {
//                throw new AppException(
//                        ErrorCode.INVALID_ORDER_STATE
//                );
//            }

            Duration timeFromConfirmation = Duration.between(confirmedAt, currentTime);
            long minutesPassed = timeFromConfirmation.toMinutes();

            if (minutesPassed > 15) {
                throw new AppException(
                        ErrorCode.INVALID_STATUS_TRANSITION_EXPIRED
                );
            }
        }
    }
}