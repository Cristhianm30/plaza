package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Traceability;

public interface ITraceabilityFeignPort {
    Traceability sendTraceability (Traceability traceability);
}
