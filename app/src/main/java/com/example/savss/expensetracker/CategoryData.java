package com.example.savss.expensetracker;

public class CategoryData {
    private int id;
    private String name;
    private int budget;
    private String[] shop_names;
    private int[] transaction_ids;
    private int spent;

    public  CategoryData(int id, String name, int budget, String[] shop_names, int[] transaction_ids, int spent) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.shop_names = shop_names;
        this.transaction_ids = transaction_ids;
        this.spent = spent;
    }

    public  CategoryData(int id, String name, int budget) {
        this.id = id;
        this.name = name;
        this.budget = budget;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBudget() { return budget; }

    public String[] getShopnames() {
        return shop_names;
    }

    public int[] getTransactionIds() {
        return transaction_ids;
    }

    public int getSpent() { return spent; }
}