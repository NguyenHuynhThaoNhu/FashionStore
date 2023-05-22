package com.fashionstore.fashion.model;

public class Order {
        private long id;
        private int amount;
        private String products;
        private String payment;


        public Order() {
        }

    public Order(long id, int amount, String products, String payment) {
        this.id = id;
        this.amount = amount;
        this.products = products;
        this.payment = payment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}