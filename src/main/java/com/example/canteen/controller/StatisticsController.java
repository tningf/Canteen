package com.example.canteen.controller;

import com.example.canteen.dto.OrderStatisticsDto;
import com.example.canteen.dto.SaleItemStatisticsDto;
import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.exception.AppException;
import com.example.canteen.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getOrderStatistics(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {

        try {
            OrderStatisticsDto statistics;

            if (startDate == null || endDate == null) {
                statistics = statisticsService.getDefaultStatistics();
            } else {
                LocalDateTime startDateTime = LocalDate.parse(startDate).atStartOfDay();
                LocalDateTime endDateTime = LocalDate.parse(endDate).atTime(LocalTime.now());
                statistics = statisticsService.getOrderStatistics(startDateTime, endDateTime);
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .code(1000)
                    .message("Lấy thống kê thành công")
                    .data(statistics)
                    .build());

        } catch (AppException e) {
            log.error("Error getting order statistics: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getHttpStatusCode())
                    .body(ApiResponse.builder()
                            .code(e.getErrorCode().getCode())
                            .message(e.getErrorCode().getMessage())
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error getting order statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .code(5000)
                            .message("Có lỗi xảy ra khi lấy thống kê")
                            .build());
        }
    }
    @GetMapping("/detailed")
    public ResponseEntity<ApiResponse> getDetailedStatistics() {
        List<SaleItemStatisticsDto> detailedStats = statisticsService.generateDetailedSalesStatistics();
        return ResponseEntity.ok(ApiResponse.builder()
                .code(1000)
                .message("Lấy thống kê chi tiết thành công")
                .data(detailedStats)
                .build());
    }

}