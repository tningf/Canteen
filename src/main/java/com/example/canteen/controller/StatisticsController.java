package com.example.canteen.controller;

import com.example.canteen.dto.statistics.*;
import com.example.canteen.dto.response.ApiResponse;
import com.example.canteen.exception.AppException;
import com.example.canteen.service.StatisticsDashboardService;
import com.example.canteen.service.StatisticsProductService;
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
    private final StatisticsDashboardService statisticsDashboardService;
    private final StatisticsProductService statisticsProductService;



    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse> getOrderStatistics(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {

        try {
            OrderStatistics statistics;

            if (startDate == null || endDate == null) {
                statistics = statisticsDashboardService.getDefaultStatistics();
            } else {
                LocalDateTime startDateTime = LocalDate.parse(startDate).atStartOfDay();
                LocalDateTime endDateTime = LocalDate.parse(endDate).atTime(LocalTime.MAX);
                log.info("Start date: {}, End date: {}", startDateTime, endDateTime);
                statistics = statisticsDashboardService.getOrderStatistics(startDateTime, endDateTime);
            }

            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Lấy thống kê thành công")
                    .data(statistics)
                    .build());

        } catch (AppException e) {
            log.error("Error getting order statistics: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getHttpStatusCode())
                    .body(ApiResponse.builder()
                            .message(e.getErrorCode().getMessage())
                            .build());
        } catch (Exception e) {
            log.error("Unexpected error getting order statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message("Có lỗi xảy ra khi lấy thống kê!")
                            .build());
        }
    }

    @GetMapping("/sale-product")
    public ResponseEntity<ApiResponse> getSaleStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) String categoryName) {
        List<SaleProductStatistics> saleStats = statisticsProductService.generateSaleProductStatistics(startDate, endDate, categoryName);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Lấy thống kê bán hàng thành công")
                .data(saleStats)
                .build());
    }

    @GetMapping("/staff-sale-statistics")
    public ResponseEntity<ApiResponse> getStaffSaleStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) String username) {
        List<StaffSaleStatistics> staffStats = statisticsProductService.generateStaffSaleStatistics(startDate, endDate, username);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Lấy thống kê bán hàng theo nhân viên thành công")
                .data(staffStats)
                .build());
    }

    @GetMapping("/patient-buy-statistics")
    public ResponseEntity<ApiResponse> getPatientSaleStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) String cardNumber) {
        List<PatientBuyStatistics> patientStats = statisticsProductService.generatePatientBuyStatistics(startDate, endDate, cardNumber);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Lấy thống kê bán hàng theo bệnh nhân thành công")
                .data(patientStats)
                .build());
    }

    @GetMapping("/department-sale-statistics")
    public ResponseEntity<ApiResponse> getDepartmentSaleStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) String departmentName) {
        List<DepartmentSaleStatistics> departmentStats = statisticsProductService
                .generateDepartmentSaleStatistics(startDate, endDate, departmentName);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Lấy thống kê bán hàng theo khoa thành công")
                .data(departmentStats)
                .build());
    }
}