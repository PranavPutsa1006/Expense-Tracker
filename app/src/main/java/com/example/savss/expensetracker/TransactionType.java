package com.example.savss.expensetracker;

public enum TransactionType {
    Income("income"),
    Expense("expense");

    private final String stringValue;

    TransactionType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String toString() {
        return this.stringValue;
    }
}
