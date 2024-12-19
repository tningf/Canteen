package com.example.canteen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
    @Builder.Default
    private List<T> data = Collections.emptyList();
}
