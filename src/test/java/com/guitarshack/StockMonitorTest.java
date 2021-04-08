package com.guitarshack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockMonitorTest {

    @Test
    void productSold() {
        StockMonitor stockMonitor = new StockMonitor(null);
        Assertions.assertThrows(NullPointerException.class, () -> stockMonitor.productSold(0, 0));
    }
}