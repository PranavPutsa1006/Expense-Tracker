package com.example.savss.expensetracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DashboardFragment extends Fragment {
    private LocalDatabaseHelper localDatabaseHelper;
    private View dashboardView;
    private TextView fromDayTextView;
    private TextView toDayTextView;
    private ListView transactionListView;
    private Spinner categorySpinner;
    private TransactionListViewAdapter transactionListViewAdapter;
    private BarChart customDatesBarChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        localDatabaseHelper = new LocalDatabaseHelper(dashboardView.getContext(), null, null, 1);

        initialise();
        setDatePicker();
        setLastMonthPieChart();
        displayTransactionlistview();
        setCustomDatesBarChart();

        return dashboardView;
    }

    private void initialise(){
        categorySpinner = dashboardView.findViewById(R.id.categorySpinner);

        ArrayList<String> categories = new ArrayList<>(UserData.categories);
        categories.add(0, "All");
        ArrayAdapter arrayAdapter = new ArrayAdapter(dashboardView.getContext(), R.layout.category_spinner_layout, categories.toArray());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);
    }

    private void setCustomDatesBarChart() {
        customDatesBarChart = dashboardView.findViewById(R.id.customDatesBarChart);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = simpleDateFormat.parse(fromDayTextView.getText().toString());
            toDate = simpleDateFormat.parse(toDayTextView.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        BarChartExpenseData barChartExpenseData = localDatabaseHelper.getCustomDateTransactionData(UserData.userID, fromDate, toDate);

        float barWidth = 0.3f;
        float barSpace = 0f;
        float groupSpace = 0.4f;

        customDatesBarChart.setData(barChartExpenseData.getBarData());
       //customDatesBarChart.getData().setHighlightEnabled(false);
        customDatesBarChart.setDescription(null);
        customDatesBarChart.setPinchZoom(true);
        customDatesBarChart.setScaleEnabled(false);
        customDatesBarChart.setDrawBarShadow(false);
        customDatesBarChart.setDrawGridBackground(false);
        customDatesBarChart.animateXY(500, 500);
        //customDatesBarChart.setExtraBottomOffset(30);
        customDatesBarChart.groupBars(0, groupSpace, barSpace);
        customDatesBarChart.animate();
        customDatesBarChart.animateX(1500);

        Legend barChartLegend = customDatesBarChart.getLegend();
        barChartLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        barChartLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        barChartLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        barChartLegend.setDrawInside(true);
        barChartLegend.setYOffset(20f);
        barChartLegend.setXOffset(0f);
        barChartLegend.setYEntrySpace(0f);
        barChartLegend.setTextSize(8f);
        barChartLegend.setTextColor(Color.BLACK);

        XAxis xAxis = customDatesBarChart.getXAxis();
        xAxis.setLabelRotationAngle(45);//add1
        //xAxis.setAxisLineColor(Color.BLACK);
        //xAxis.setGridColor(Color.BLACK);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(14f);
        //xAxis.setDrawLabels(true);
       // xAxis.setAvoidFirstLastClipping(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(barChartExpenseData.getCategories()));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        //xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        //xAxis.setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(barChartExpenseData.getCategories()));
        //xAxis.setLabelCount(barChartExpenseData.getBarData().getEntryCount());
        //LineChart l = new LineChart(getContext());
        //l.getXAxis().setLabelCount(barChartExpenseData.count());

        customDatesBarChart.getAxisRight().setEnabled(false);
        YAxis yAxis = customDatesBarChart.getAxisLeft();
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setGridColor(Color.BLACK);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setValueFormatter(new LargeValueFormatter());
        yAxis.setDrawGridLines(true);
        yAxis.setTextSize(12f);
        yAxis.setSpaceTop(35f);
        yAxis.setAxisMinimum(0f);

        customDatesBarChart.groupBars(0f, groupSpace, barSpace);
        //customDatesBarChart.getBarData().setBarWidth(barWidth);
        customDatesBarChart.getXAxis().setAxisMinimum(0);
        customDatesBarChart.getXAxis().setAxisMaximum(0 + customDatesBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * barChartExpenseData.count());
        // customDatesBarChart.getXAxis().setAxisMaximum(barChartExpenseData.count() - 1.1f);

        //customDatesBarChart.invalidate();
    }

    private void refreashListItemsAndChart() {
        //localDatabaseHelper.getLastMonthExpenses(UserData.userID);
        setLastMonthPieChart();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = simpleDateFormat.parse(fromDayTextView.getText().toString());
            toDate = simpleDateFormat.parse(toDayTextView.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        localDatabaseHelper.getLastMonthExpenses(UserData.userID);

        BarChartExpenseData barChartExpenseData = localDatabaseHelper.getCustomDateTransactionData(UserData.userID, fromDate, toDate);

        customDatesBarChart.setData(barChartExpenseData.getBarData());
        customDatesBarChart.groupBars(0f, 0.5f, 0f);
        customDatesBarChart.getData().setHighlightEnabled(false);
        customDatesBarChart.setDescription(null);
        customDatesBarChart.setPinchZoom(false);
        customDatesBarChart.setScaleEnabled(false);
        customDatesBarChart.setDrawBarShadow(false);
        customDatesBarChart.setDrawGridBackground(false);
        customDatesBarChart.animateXY(500, 500);

        //customDatesBarChart.invalidate();

        transactionListViewAdapter = new TransactionListViewAdapter(localDatabaseHelper.getTransactionData(UserData.userID, fromDate , toDate));
        transactionListView.setAdapter(transactionListViewAdapter);
    }

    private void setDatePicker() {
        fromDayTextView = dashboardView.findViewById(R.id.fromDayTextView);
        toDayTextView = dashboardView.findViewById(R.id.toDayTextView);

        Calendar today = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        toDayTextView.setText(simpleDateFormat.format(today.getTime()));
        today.set(Calendar.DAY_OF_MONTH, 1);
        fromDayTextView.setText(simpleDateFormat.format(today.getTime()));

        fromDayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(dashboardView.getContext(), R.style.Theme_AppCompat_Light_Dialog, fromDatePickerDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });

        toDayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(dashboardView.getContext(), R.style.Theme_AppCompat_Light_Dialog, toDatePickerDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener fromDatePickerDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            month++;
            String pickedDate = day + "/" + month + "/" + year;
            fromDayTextView.setText(pickedDate);
            refreashListItemsAndChart();
        }
    };

    private DatePickerDialog.OnDateSetListener toDatePickerDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            month++;
            String pickedDate = day + "/" + month + "/" + year;
            toDayTextView.setText(pickedDate);
            refreashListItemsAndChart();
        }
    };

    private AdapterView.OnItemClickListener transactionListViewItemClickListener = new AdapterView.OnItemClickListener() {
        private Dialog transactionDataPopUp;
        private TextView transactionIDTextView;
        private Spinner messageBox;//del
        private TextView transactionTypeTextView;
        private TextView transactionTypeEdit;
        private TextView transactionAmountTextView;
        private EditText transactionAmountEditText;
        private TextView transactionCategoryTextView;
        private Spinner transactionCategorySpinner;
        private TextView transactionDateTextView;
        private TextView transactionDateEdit;
        //private TextView transactionDescriptionTextView;
        private EditText transactionDescriptionEditText;
        private Button closeButton;
        private Button editButton;
        private ViewSwitcher transactionTypeViewSwitcher;
        private ViewSwitcher transactionAmountViewSwitcher;
        private ViewSwitcher transactionCategoryViewSwitcher;
        private ViewSwitcher transactionDateViewViewSwitcher;
        private ViewSwitcher transactionDescriptionViewSwitcher;
        private TransactionData transactionData;
        private boolean isEdited = false;
        private Button deleteButton;

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            transactionData = (TransactionData)transactionListViewAdapter.getItem(i);

            initialisePopUp();
            displayTransactionDetails();
        }

        private void initialisePopUp() {
            transactionDataPopUp = new Dialog(dashboardView.getContext());
            transactionDataPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
            transactionDataPopUp.setContentView(R.layout.transaction_details_popup);



            transactionIDTextView = transactionDataPopUp.findViewById(R.id.transactionIDTextView);
            transactionTypeTextView = transactionDataPopUp.findViewById(R.id.transactionTypeTextView);
            transactionTypeEdit = transactionDataPopUp.findViewById(R.id.transactionTypeEdit);
            transactionAmountTextView = transactionDataPopUp.findViewById(R.id.transactionAmountTextView);
            transactionAmountEditText = transactionDataPopUp.findViewById(R.id.transactionAmountEditText);
            transactionCategoryTextView = transactionDataPopUp.findViewById(R.id.transactionCategoryTextView);
            transactionCategorySpinner = transactionDataPopUp.findViewById(R.id.transactionCategorySpinner);

            messageBox= transactionDataPopUp.findViewById(R.id.MessageDropdown);


            transactionDateTextView = transactionDataPopUp.findViewById(R.id.transactionDateTextView);
            transactionDateEdit = transactionDataPopUp.findViewById(R.id.transactionDateEdit);
           // transactionDescriptionTextView = transactionDataPopUp.findViewById(R.id.transactionDescriptionTextView);
            transactionDescriptionEditText = transactionDataPopUp.findViewById(R.id.transactionDescriptionEditText);
            closeButton = transactionDataPopUp.findViewById(R.id.closeButton);
            deleteButton = transactionDataPopUp.findViewById(R.id.deleteButton);
            editButton = transactionDataPopUp.findViewById(R.id.editButton);
            transactionTypeViewSwitcher = transactionDataPopUp.findViewById(R.id.transactionTypeViewSwitcher);
            transactionAmountViewSwitcher = transactionDataPopUp.findViewById(R.id.transactionAmountViewSwitcher);
            transactionCategoryViewSwitcher = transactionDataPopUp.findViewById(R.id.transactionCategoryViewSwitcher);
            transactionDateViewViewSwitcher = transactionDataPopUp.findViewById(R.id.transactionDateViewViewSwitcher);
            transactionDescriptionViewSwitcher = transactionDataPopUp.findViewById(R.id.transactionDescriptionViewSwitcher);



            //messageBox.setOnItemSelectedListener(onItemSelected());

            // Spinner Drop down elements
            List<String> categories = new ArrayList<String>();


            String ShopName[]={"Megha Bite N Slurp","Sachu Dine Spot","Sahana Aahaar","Shururu Curd Rice","Shreyas Salad paradise","Pra Pra Pizza Hut"};
            Random rand = new Random();
            String s=ShopName[rand.nextInt(6)];
            categories.add("Shopname:\n"+s+"\nMessage: Have patients message will appear ");


            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(transactionDataPopUp.getContext(), R.layout.category_spinner_layout, categories);
            messageBox.setEnabled(false);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            // attaching data adapter to spinner
            messageBox.setAdapter(dataAdapter);


            closeButton.setOnClickListener(closeButtonClickListener);
            deleteButton.setOnClickListener(deleteButtonClickListener);
            editButton.setOnClickListener(editButtonClickListener);
            transactionTypeEdit.setOnClickListener(transactionTypeEditClickListener);
            transactionDateEdit.setOnClickListener(transactionDateEditClickListener);

            String id = "#" + String.valueOf(transactionData.getId());
            transactionIDTextView.setText(id);
            isEdited = false;

            transactionDataPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(180,255,255,255)));
            transactionDataPopUp.show();
        }

        private void displayTransactionDetails() {
            setViewSwitcherView(0);

            deleteButton.setVisibility(View.INVISIBLE);

            transactionTypeTextView.setText(transactionData.getTransactionType());
            transactionAmountTextView.setText(transactionData.getAmount());
            transactionCategoryTextView.setText(transactionData.getCategory());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String transactionDateString = simpleDateFormat.format(transactionData.getDateTime());

            transactionDateTextView.setText(transactionDateString);
            //transactionDescriptionTextView.setText(transactionData.getDescription());
        }


//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            // On selecting a spinner item
//            String item = parent.getItemAtPosition(position).toString();
//
//            // Showing selected spinner item
//            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
//        }
//        public void onNothingSelected(AdapterView<?> arg0) {
//            // TODO Auto-generated method stub
//        }

        private void editTransactionDetails() {
            isEdited = true;

            setViewSwitcherView(1);

            deleteButton.setVisibility(View.VISIBLE);

            transactionTypeEdit.setText(transactionData.getTransactionType());
            transactionAmountEditText.setText(transactionData.getAmount());

            ArrayAdapter arrayAdapter = new ArrayAdapter(transactionDataPopUp.getContext(), R.layout.category_spinner_layout, UserData.categories.toArray());
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            transactionCategorySpinner.setAdapter(arrayAdapter);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String transactionDateString = simpleDateFormat.format(transactionData.getDateTime());

            transactionDateEdit.setText(transactionDateString);
            transactionDescriptionEditText.setText(transactionData.getDescription());

        }

        private void setViewSwitcherView(int viewIntex) {
            transactionTypeViewSwitcher.setDisplayedChild(viewIntex);
            transactionAmountViewSwitcher.setDisplayedChild(viewIntex);
            transactionCategoryViewSwitcher.setDisplayedChild(viewIntex);
            transactionDateViewViewSwitcher.setDisplayedChild(viewIntex);
            transactionDescriptionViewSwitcher.setDisplayedChild(viewIntex);
        }

        private View.OnClickListener transactionTypeEditClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String transactionType = transactionTypeEdit.getText().toString().toLowerCase().equals("income") ? "Expense" : "Income";
                transactionTypeEdit.setText(transactionType);
            }
        };

        private View.OnClickListener transactionDateEditClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(transactionDataPopUp.getContext(), R.style.Theme_AppCompat_Light_Dialog, transactionDatePickerDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
            }
        };

        private DatePickerDialog.OnDateSetListener transactionDatePickerDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String pickedDate = day + "/" + month + "/" + year;
                transactionDateEdit.setText(pickedDate);
            }
        };

        private View.OnClickListener closeButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionDataPopUp.cancel();
            }
        };

        private View.OnClickListener deleteButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localDatabaseHelper.deleteTransaction(transactionData.getId());
                //localDatabaseHelper.getLastMonthExpenses(UserData.userID);
                //setLastMonthPieChart();
                refreashListItemsAndChart();
                transactionDataPopUp.cancel();
            }
        };

        private View.OnClickListener editButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdited) {
                    isEdited = false;
                    transactionDataPopUp.cancel();

                    TransactionType transactionType = transactionTypeEdit.getText().toString().toLowerCase().equals("income") ? TransactionType.Income : TransactionType.Expense;

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date transactionDate = null;
                    try {
                        transactionDate = simpleDateFormat.parse(transactionDateEdit.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    localDatabaseHelper.updateTransactionDetails(transactionData.getId(), transactionType, transactionAmountEditText.getText().toString(), UserData.categories.indexOf(transactionCategorySpinner.getSelectedItem().toString()) + 1, transactionDate, transactionDescriptionEditText.getText().toString());

                    refreashListItemsAndChart();

                    //localDatabaseHelper.getLastMonthExpenses(UserData.userID);
                }
                else {
                    editButton.setText("Accept");
                    editTransactionDetails();
                }
                //localDatabaseHelper.getLastMonthExpenses(UserData.userID);
                //setLastMonthPieChart();
            }
        };
    };

    private void setLastMonthPieChart() {
        TextView lastMonthAmountTextView = dashboardView.findViewById(R.id.lastMonthAmountTextView);
        PieChart todayPieChart = dashboardView.findViewById(R.id.lastMonthPieChart);
        todayPieChart.setDescription(null);
        todayPieChart.setRotationEnabled(true);
        todayPieChart.setHoleRadius(15f);
        todayPieChart.setTransparentCircleAlpha(0);
        todayPieChart.setDrawEntryLabels(true);
        //todayPieChart.setHoleColor(R.color.transparent);

        PieChartExpenseData pieChartExpenseData = localDatabaseHelper.getLastMonthExpenses(UserData.userID);

        lastMonthAmountTextView.setText(String.valueOf(pieChartExpenseData.getTotalExpenseAmount()));

        Legend legend = todayPieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);

        PieData pieData = new PieData(pieChartExpenseData.getPieDataSet());
        todayPieChart.setData(pieData);
        //todayPieChart.animateXY(500, 500);
        todayPieChart.invalidate();
    }

    private void displayTransactionlistview() {
        transactionListView = dashboardView.findViewById(R.id.transactionListView);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.DAY_OF_MONTH, 1);

        transactionListViewAdapter = new TransactionListViewAdapter(localDatabaseHelper.getTransactionData(UserData.userID, today.getTime(), Calendar.getInstance().getTime()));
        transactionListView.setAdapter(transactionListViewAdapter);

        transactionListView.setOnItemClickListener(transactionListViewItemClickListener);
    }

    class TransactionListViewAdapter extends BaseAdapter {

        private ArrayList<TransactionData> transactionData;

        private TransactionListViewAdapter(ArrayList<TransactionData> transactionData) {
            this.transactionData = transactionData;
        }

        @Override
        public int getCount() {
            return transactionData.size();
        }

        @Override
        public Object getItem(int i) {
            return transactionData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.transaction_listviewitem_template, null);
            TextView dateTextView = view.findViewById(R.id.dateTextView);
            TextView amountTextView = view.findViewById(R.id.amountTextView);
            TextView catagoryTextView = view.findViewById(R.id.catagoryTextView);

            TransactionData transactionData = this.transactionData.get(i);

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String transactionDateString = simpleDateFormat.format(transactionData.getDateTime());

            dateTextView.setText(transactionDateString);
            String amount = (transactionData.getTransactionType().toLowerCase().equals("income") ? "+" : "-") + " " + transactionData.getAmount();
            amountTextView.setText(amount);
            catagoryTextView.setText(transactionData.getCategory());
            return view;
        }
    }
}
