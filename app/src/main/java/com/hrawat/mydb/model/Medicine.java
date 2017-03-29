package com.hrawat.mydb.model;

/**
 * Created by hrawat on 3/29/2017.
 */
public class Medicine {

    private String name;
    private String url;
    private String manufacturer;
    private String price;
    private String quantity;
    private String unit;

    public Medicine(String name, String url, String manufacturer, String price, String quantity, String unit) {
        this.name = name;
        this.url = url;
        this.manufacturer = manufacturer;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

