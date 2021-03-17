package com.example.savss.expensetracker;

import android.graphics.Color;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

public class PieChartExpenseData {
    private ArrayList<PieEntry> expenseAmounts = new ArrayList<>();
    private int totalExpenseAmount = 0;

    public void add(String category, int expenseAmount) {
        totalExpenseAmount += expenseAmount;
        expenseAmounts.add(new PieEntry(expenseAmount, category));
    }

    public PieDataSet getPieDataSet() {
        PieDataSet pieDataSet = new PieDataSet(expenseAmounts, "Income");
        pieDataSet.setSliceSpace(10);
        pieDataSet.setValueTextSize(20f);
        pieDataSet.setFormSize(20f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        return pieDataSet;
    }

    public int getTotalExpenseAmount() {
        return totalExpenseAmount;
    }
}
