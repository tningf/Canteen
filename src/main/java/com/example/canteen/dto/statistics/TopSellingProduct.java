package com.example.canteen.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopSellingProduct {
    private Long productId;
    private String productName;
    private int quantitySold;
    private BigDecimal revenue;

    // Inner Key class for grouping
    public static class Key {
        private final Long productId;
        private final String productName;

        public Key(Long productId, String productName) {
            this.productId = productId;
            this.productName = productName;
        }

        public Long getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(productId, key.productId) &&
                    Objects.equals(productName, key.productName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productId, productName);
        }
    }
}