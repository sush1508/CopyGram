package com.example.xeroxapp.model;

public class Model {
    private int image;
    private String status,txn,order,date,docCount;

    public Model(int image, String status, String order,String txn, String date, String docCount) {
        this.image = image;
        this.status = status;
        this.txn = txn;
        this.order = order;
        this.date = date;
        this.docCount = docCount;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDocCount() {
        return docCount;
    }

    public void setDocCount(String docCount) {
        this.docCount = docCount;
    }
}
