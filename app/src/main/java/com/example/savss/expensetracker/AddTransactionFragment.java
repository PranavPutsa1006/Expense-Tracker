package com.example.savss.expensetracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddTransactionFragment extends Fragment {

    private String transactionType = "+";
    private View addTransactionView;
    private LocalDatabaseHelper localDatabaseHelper;
    private Button income;
    private Button expense;
    private TextView incomeOrExpense;
    private EditText value;
    private Button clear;
    private EditText description;
    private Button add;
    private Spinner categorySpinner;
    private TextView dateTextView;
    private float defaultTextSize = 80;
    private float changedTextSize = 80;
    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH);
    int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    Toast toast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addTransactionView = inflater.inflate(R.layout.fragment_add_transaction, container, false);
        localDatabaseHelper = new LocalDatabaseHelper(addTransactionView.getContext(), null, null, 1);

        initialise();
        setListener();

        return addTransactionView;
    }

    @SuppressLint("ShowToast")
    private void initialise() {
        income = addTransactionView.findViewById(R.id.income_button);
        expense = addTransactionView.findViewById(R.id.expense_button);
        incomeOrExpense = addTransactionView.findViewById(R.id.income_or_expense);
        value = addTransactionView.findViewById(R.id.valueDisplay);
        clear = addTransactionView.findViewById(R.id.clearButton);
        add = addTransactionView.findViewById(R.id.addButton);
        toast = Toast.makeText(addTransactionView.getContext(), "", Toast.LENGTH_SHORT);
        description = addTransactionView.findViewById(R.id.descriptionView);
        categorySpinner = addTransactionView.findViewById(R.id.categorySpinner);
        dateTextView = addTransactionView.findViewById(R.id.dateTextView);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTextView.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));

        ArrayList<String> categories = new ArrayList<>(UserData.categories);
        categories.add(0, "Choose a category");
        ArrayAdapter arrayAdapter = new ArrayAdapter(addTransactionView.getContext(), R.layout.category_spinner_layout, categories.toArray());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);
    }

    private void setListener() {
        value.addTextChangedListener(valueTextViewWatcher);
        clear.setOnClickListener(clearOnClickListener);
        add.setOnClickListener(addOnClickListener);
        income.setOnClickListener(incomeOnClickListener);
        expense.setOnClickListener(expenseOnClickListener);
        dateTextView.setOnClickListener(dateOnClickListener);
    }

    private View.OnClickListener incomeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            transactionType = "+";
            incomeOrExpense.setTextSize(80);
            incomeOrExpense.setText(transactionType);
        }
    };

    private View.OnClickListener expenseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            transactionType = "-";
            incomeOrExpense.setTextSize(100);
            incomeOrExpense.setText(transactionType);
        }
    };

    private View.OnClickListener clearOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            value.setText("0.0");
            transactionType = "+";
            incomeOrExpense.setText(transactionType);
            description.setText("");
        }
    };

    private View.OnClickListener addOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String valueOfTransactionType = transactionType.equals("+") ? "income" : "expense";

            if (Float.parseFloat(value.getText().toString()) == 0.0) {
                displayToast("Transaction amount cannot be 0");
                return;
            }

            if (categorySpinner.getSelectedItem().toString().equals("Choose a category")) {
                displayToast("A category has to be chosen");
                return;
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date transactionDate = null;
            try {
                transactionDate = simpleDateFormat.parse(dateTextView.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String message = "";

            if (valueOfTransactionType.equals("expense")) {
                message = notifyIfExceededLimit(categorySpinner.getSelectedItem().toString(), Float.parseFloat(value.getText().toString()));
            }

            localDatabaseHelper.addTransaction(String.valueOf(UserData.userID), UserData.categories.indexOf(categorySpinner.getSelectedItem().toString()) + 1, valueOfTransactionType, value.getText().toString(), description.getText().toString(), transactionDate);

            clear.callOnClick();
            localDatabaseHelper.getLastMonthExpenses(UserData.userID);
            displayToast(message + "Transaction Added Successfully");
        }
    };

    private String notifyIfExceededLimit(String category, float amount) {
        ArrayList<String> categories = localDatabaseHelper.getAllCategories();
        ArrayList<Integer> budgets = localDatabaseHelper.getAllCategoryBudgets();
        ArrayList<Float> expense = localDatabaseHelper.getCategoryWiseMonthlyExpenses();

        int categoryIndex = categories.indexOf(category);
        float finalAmount = expense.get(categoryIndex) + amount;
        String message = "";

        if (budgets.get(categoryIndex) < (expense.get(categoryIndex) + amount)) {
            /*String NOTIFICATION_CHANNEL_ID = "Limit-Alert";

            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder((addTransaction)View.getContext(), NOTIFICATION_CHANNEL_ID);
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Category " + category + " Exceeded Limit")
                    .setContentText("You have exceeded your budget " + budgets.get(categoryIndex) + ". Your current expenditure is " + finalAmount + ".");

            Intent notificationIntent = new Intent(getContext(), HomeActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(contentIntent);

            notificationManager.notify(1, notificationBuilder.build());
            System.out.println("notify");*/

            message = "You have exceeded your budget " + budgets.get(categoryIndex) + " for " + category + ". Your current expenditure is " + finalAmount + ".\n";
        }

        return message;
    }

    private View.OnClickListener dateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(addTransactionView.getContext(), R.style.Theme_AppCompat_Light_Dialog, datePickerDateSetListener, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            datePickerDialog.show();
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            month++;
            String pickedDate = day + "/" + month + "/" + year;
            dateTextView.setText(pickedDate);
        }
    };

    private void displayToast(String message) {
        Vibrator vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(120);

        toast.setText(message);
        toast.show();
    }

    private TextWatcher valueTextViewWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isTooLarge()) {
                changedTextSize  = defaultTextSize - (value.getText().toString().trim().length() - 8) * 6;
                value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, changedTextSize);
            }
            else {
                value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, defaultTextSize);
                changedTextSize = defaultTextSize;
            }
        }
        private boolean isTooLarge() {
            return value.getText().toString().trim().length() > 7;
        }
    };
}
