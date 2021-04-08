package com.guitarshack;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StockMonitor {
    private final Alert alert;
    private final HttpResponseProvider httpResponseProvider;

    public StockMonitor(Alert alert, HttpResponseProvider httpResponseProvider) {
        this.alert = alert;
        this.httpResponseProvider = httpResponseProvider;
    }

    public StockMonitor(Alert alert) {
        this(alert, new HttpResponseProvider());
    }

    public void productSold(int productId, int quantity) {
        Product product = getProduct(productId);

        SalesTotal total = getSalesTotal(product);

        if(getRemainingStock(quantity, product) <= getLeadPressure(product, total)) // TODO: DEPENDENCY???
            alert.send(product);
    }

    protected int getLeadPressure(Product product, SalesTotal total) {
        return (int) ((double) (total.getTotal() / 30) * product.getLeadTime());
    }

    protected int getRemainingStock(int quantity, Product product) {
        return product.getStock() - quantity;
    }

    protected SalesTotal getSalesTotal(Product product) {
        Calendar calendar = Calendar.getInstance(); // TODO: DEPENDENCY
        calendar.setTime(Calendar.getInstance().getTime()); // TODO: DEPENDENCY
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DATE, -30); // TODO: DEPENDENCY???
        Date startDate = calendar.getTime();
        DateFormat format = new SimpleDateFormat("M/d/yyyy"); // TODO: DEPENDENCY
        Map<String, Object> params1 = new HashMap<>(){{
            put("productId", product.getId());
            put("startDate", format.format(startDate));
            put("endDate", format.format(endDate));
            put("action", "total");
        }};
        String paramString1 = "?";

        for (String key : params1.keySet()) {
            paramString1 += key + "=" + params1.get(key).toString() + "&";
        }
        String uriAsString = "https://gjtvhjg8e9.execute-api.us-east-2.amazonaws.com/default/sales" + paramString1; // TODO: DEPENDENCY
        String result1 = httpResponseProvider.requestFrom(uriAsString);
        SalesTotal total = new Gson().fromJson(result1, SalesTotal.class); // TODO: DEPENDENCY
        return total;
    }

    protected Product getProduct(int productId) {
        String baseURL = "https://6hr1390c1j.execute-api.us-east-2.amazonaws.com/default/product"; // TODO: DEPENDENCY
        Map<String, Object> params = new HashMap<>() {{
            put("id", productId);
        }};
        String paramString = "?";

        for (String key : params.keySet()) {
            paramString += key + "=" + params.get(key).toString() + "&";
        }

        String result = httpResponseProvider.requestFrom(baseURL + paramString);

        Product product = new Gson().fromJson(result, Product.class); // TODO: DEPENDENCY
        return product;
    }

}
