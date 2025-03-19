package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.OrderOtp;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderOtpEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {IOrderDishEntityMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderOtpEntityMapper {

    OrderOtp toModel (OrderOtpEntity orderOtpEntity);
    OrderOtpEntity toEntity (OrderOtp orderOtp);

}
