package com.example.savss.expensetracker;

import java.util.Date;

public class CategoryData {
    private int id;
    private String amount;
    private Date dateTime;
    private String category;
    private String description;
    private String transactionType;

    public  CategoryData(int id, String amount, Date dateTime, String category, String description, String transactionType) {
        this.id = id;
        this.amount = amount;
        this.dateTime = dateTime;
        this.category = category;
        this.description = description;
        this.transactionType = transactionType.substring(0,1).toUpperCase() + transactionType.substring(1).toLowerCase();
    }

    public int getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getTransactionType() {
        return transactionType;
    }
}