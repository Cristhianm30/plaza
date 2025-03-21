package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.EmployeeRanking;
import com.pragma.powerup.domain.model.OrderEfficiency;
import com.pragma.powerup.domain.model.Traceability;

import java.util.List;

public interface ITraceabilityFeignPort {

    Traceability sendTraceability (Traceability traceability);

    List<Traceability> getTraceabilityByClient(Long clientId);

    List<OrderEfficiency> getOrderEfficiency (List<Long> orderId);

    List<EmployeeRanking> getEmployeeRanking (List<Long> orderId);
}
