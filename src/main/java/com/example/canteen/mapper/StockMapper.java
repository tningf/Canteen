package com.example.canteen.mapper;

import com.example.canteen.dto.StockDto;
import com.example.canteen.entity.Stock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {
    StockDto toStockDto(Stock stock);
    Stock toStock(StockDto stockDto);
}
