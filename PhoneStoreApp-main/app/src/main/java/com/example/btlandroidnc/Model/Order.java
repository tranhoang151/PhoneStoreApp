package com.example.btlandroidnc.Model;

public class Order {
    private String product_id;
    private int quantity;
    private float price;
    private String invoiceId; // Thêm thuộc tính invoiceId

    public Order(String product_id, int quantity, float price, String invoiceId) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
        this.invoiceId = invoiceId; // Khởi tạo thuộc tính invoiceId
    }

    public Order() {

    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }
}
