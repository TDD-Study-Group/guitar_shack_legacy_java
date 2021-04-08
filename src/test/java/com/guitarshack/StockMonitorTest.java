package com.guitarshack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

class StockMonitorTest {

    @Test
    void productSold() {
        StockMonitor stockMonitor = new StockMonitor(null);
        Assertions.assertThrows(NullPointerException.class, () -> stockMonitor.productSold(0, 0));
    }

    @Test
    void triggerAlertIfOutOfStock() {
        var stockMonitor = new StockMonitor(null) {
            @Override
            protected Product getProduct(int productId) {
                return new Product(1, 1, 3);
            }
        };
        // ToDo: vary inputs, like 1,-5 0,1 2,10
        assertThrows(NullPointerException.class, () -> stockMonitor.productSold(1, 1));
    }

    @Test
    void triggerAlertIfmock() {
        Alert alert = Mockito.mock(Alert.class);
        Product product = new Product(1, 1, 3);
        var stockMonitor = new StockMonitor(alert) {
            @Override
            protected Product getProduct(int productId) {
                return product;
            }
        };
        // ToDo: vary inputs, like 1,-5 0,1 2,10
        stockMonitor.productSold(1, 1);
        verify(alert).send(product);
    }


    /*
    curl "https://gjtvhjg8e9.execute-api.us-east-2.amazonaws
    .com/default/sales?productId=1&endDate=4/8/2021&action=total&startDate=3/9/2021&"
    {"productID":1,"startDate":"3/9/2021","endDate":"4/8/2021","total":0}

     */
}
