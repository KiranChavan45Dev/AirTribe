package com.smart.system.parking.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StrategyContext {

    private final AllocationStrategy allocationStrategy;
    private final PricingStrategy pricingStrategy;

    @Autowired
    public StrategyContext(
            @Qualifier("nearestFirst") AllocationStrategy allocationStrategy,
            @Qualifier("hourlyPricingStrategy") PricingStrategy pricingStrategy) {
        this.allocationStrategy = allocationStrategy;
        this.pricingStrategy = pricingStrategy;
    }

    public AllocationStrategy getAllocationStrategy() {
        return allocationStrategy;
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }
}

