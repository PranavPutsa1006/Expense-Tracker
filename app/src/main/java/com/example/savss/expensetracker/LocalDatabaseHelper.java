package com.example.savss.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "expensetrakerDB.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CATEGORY = "categories";
    private static final String TABLE_TRANSACTION = "transactions";

    public static final String USERS_ID = "user_id";
    public static final String USERS_NAME = "name";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_ADDRESS = "address";
    public static final String USERS_DOB = "01/01/2000";
    public static final String USERS_PHONENUMBER = "phonenumber";
    public static final String USERS_PASSWORD = "password";

    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "name";
    public static final String CATEGORY_BUDGET = "budget";

    public static final String TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_FKEY_USERS_ID = "user_id";
    public static final String TRANSACTION_DATE = "tdate";
    public static final String TRANSACTION_FKEY_CATEGORY_ID = "category_id";
    public static final String TRANSACTION_TYPE = "type";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_DESCRIPTION = "description";

    public LocalDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String userTableCreationQuery = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
                TABLE_USERS, USERS_ID, USERS_NAME, USERS_EMAIL, USERS_PHONENUMBER, USERS_PASSWORD);
        String categoryTableCreationQuery = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER);",
                TABLE_CATEGORY, CATEGORY_ID, CATEGORY_NAME, CATEGORY_BUDGET);

        String transactionTableCreationQuery = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s DATETIME, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, FOREIGN KEY(%s) REFERENCES %s(%s), FOREIGN KEY (%s) REFERENCES %s(%s));",
                TABLE_TRANSACTION, TRANSACTION_ID, TRANSACTION_FKEY_USERS_ID, TRANSACTION_DATE, TRANSACTION_FKEY_CATEGORY_ID, TRANSACTION_TYPE, TRANSACTION_AMOUNT, TRANSACTION_DESCRIPTION, TRANSACTION_FKEY_USERS_ID, TABLE_USERS, USERS_ID, TRANSACTION_FKEY_CATEGORY_ID, TABLE_CATEGORY, CATEGORY_ID);

        sqLiteDatabase.execSQL(userTableCreationQuery);
        sqLiteDatabase.execSQL(categoryTableCreationQuery);
        sqLiteDatabase.execSQL(transactionTableCreationQuery);

        //String categoryAddQuery1 = String.format("INSERT INTO %s VALUES(1, 'Travel', 5000);",TABLE_CATEGORY);
        //sqLiteDatabase.compileStatement(categoryAddQuery1);


//        sqLiteDatabase.execSQL(String.format("insert into %s values(2, 'Medical', 10000);",TABLE_CATEGORY));
//        sqLiteDatabase.execSQL(String.format("insert into %s values(3, 'Food', 5000);",TABLE_CATEGORY));
//        sqLiteDatabase.execSQL(String.format("insert into %s values(4, 'Rent', 15000);",TABLE_CATEGORY));
//        sqLiteDatabase.execSQL(String.format("insert into %s values(5, 'Utility bills', 5000);",TABLE_CATEGORY));

        try {

//            sqLiteDatabase.execSQL("insert into transactions values(1, 1, '2018-02-22', 1, 'expense', 1000, 'another');");
//            sqLiteDatabase.execSQL("insert into transactions values(2, 1, '2018-02-22', 1, 'income', 1000, 'another');");
//            sqLiteDatabase.execSQL("insert into transactions values(3, 1, '2018-02-22', 1, 'expense', 2000, 'asdf');");
//            sqLiteDatabase.execSQL("insert into transactions values(4, 1, '2018-02-22', 2, 'expense', 4000, 'asdf');");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean tryAddUser(String name, String email, String phoneNumber, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_NAME, name);
        contentValues.put(USERS_EMAIL, email);
        contentValues.put(USERS_PHONENUMBER, phoneNumber);
        contentValues.put(USERS_PASSWORD, password);
        if (isExisting(phoneNumber)) {
            return false;
        }
        else {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.insert(TABLE_USERS, null, contentValues);
            sqLiteDatabase.close();
            return true;
        }
    }

    private boolean isExisting(String phoneNumber){
        String checkQuery = String.format("SELECT * FROM %s WHERE %s = '%s'", TABLE_USERS, USERS_PHONENUMBER, phoneNumber);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(checkQuery, null);
        if (cursor.getCount() == 0) {
            sqLiteDatabase.close();
            return false;
        }
        else {
            sqLiteDatabase.close();
            return true;
        }
    }

    public boolean isExisting(){
        String checkQuery = String.format("SELECT * FROM %s", TABLE_USERS);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(checkQuery, null);
        if (cursor.getCount() > 0) {
            sqLiteDatabase.close();
            return true;
        }
        else {
            sqLiteDatabase.close();
            return false;
        }
    }

    public String getPassword(String loginID, IDType idType) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String getPasswordQuery = "";

        if (idType == IDType.Email) {
            getPasswordQuery = String.format("SELECT %s FROM %s WHERE %s = '%s'", USERS_PASSWORD, TABLE_USERS, USERS_EMAIL, loginID);
        }
        else if (idType == IDType.PhoneNumber) {
            getPasswordQuery = String.format("SELECT %s FROM %s WHERE %s = '%s'", USERS_PASSWORD, TABLE_USERS, USERS_PHONENUMBER, loginID);
        }

        Cursor cursor = sqLiteDatabase.rawQuery(getPasswordQuery, null);
        String password = "";

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(USERS_PASSWORD)) != null) {
                password = cursor.getString(cursor.getColumnIndex(USERS_PASSWORD));
            }
            cursor.moveToNext();
        }
        sqLiteDatabase.close();

        return password;
    }

    public String getName(String loginID, IDType idType) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String getNameQuery = "";

        if (idType == IDType.Email) {
            getNameQuery = String.format("SELECT %s FROM %s WHERE %s = '%s'", USERS_NAME, TABLE_USERS, USERS_EMAIL, loginID);
        }
        else if (idType == IDType.PhoneNumber) {
            getNameQuery = String.format("SELECT %s FROM %s WHERE %s = '%s'", USERS_NAME, TABLE_USERS, USERS_PHONENUMBER, loginID);
        }

        Cursor cursor = sqLiteDatabase.rawQuery(getNameQuery, null);
        String Name = "";

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(USERS_NAME)) != null) {
                Name = cursor.getString(cursor.getColumnIndex(USERS_NAME));
            }
            cursor.moveToNext();
        }
        sqLiteDatabase.close();

        return Name;
    }

    public int getUserID(String loginID) {
        return getUserID(loginID, IDType.Email);
    }

    public int getUserID(String loginID, IDType idType) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String getUserIDQuery = "";

        if (idType == IDType.Email) {
            getUserIDQuery = String.format("SELECT %s FROM %s WHERE %s = '%s'", USERS_ID, TABLE_USERS, USERS_EMAIL, loginID);
        }
        else if (idType == IDType.PhoneNumber) {
            getUserIDQuery = String.format("SELECT %s FROM %s WHERE %s = '%s'", USERS_ID, TABLE_USERS, USERS_PHONENUMBER, loginID);
        }

        Cursor cursor = sqLiteDatabase.rawQuery(getUserIDQuery, null);
        String userID = "";

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(USERS_ID)) != null) {
                userID = cursor.getString(cursor.getColumnIndex(USERS_ID));
            }
            cursor.moveToNext();
        }
        sqLiteDatabase.close();

        return Integer.parseInt(userID);
    }

    public PieChartExpenseData getTodaysExpenses(int userID) {
        PieChartExpenseData pieChartExpenseData = new PieChartExpenseData();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        TransactionType tType = TransactionType.Income;
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String fetchDataQuery = String.format("SELECT SUM(%s), %s FROM %s, %s WHERE %s != '%s' AND %s.%s = %s.%s AND %s = %s AND %s = '%s' GROUP BY (%s.%s);",
                TRANSACTION_AMOUNT, CATEGORY_NAME, TABLE_TRANSACTION, TABLE_CATEGORY, TRANSACTION_TYPE, tType.toString(),
                TABLE_CATEGORY, CATEGORY_ID, TABLE_TRANSACTION, TRANSACTION_FKEY_CATEGORY_ID, TRANSACTION_FKEY_USERS_ID,
                userID, TRANSACTION_DATE, "2018-02-22", TABLE_TRANSACTION, TRANSACTION_FKEY_CATEGORY_ID);
        //System.out.println(fetchDataQuery);

        Cursor c = sqLiteDatabase.rawQuery(fetchDataQuery, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            pieChartExpenseData.add(c.getString(1), c.getInt(0));
            c.moveToNext();
        }
        sqLiteDatabase.close();
        getLastMonthExpenses(1);
        return pieChartExpenseData;
    }

    public PieChartExpenseData getLastMonthExpenses(int userID) {
        PieChartExpenseData pieChartExpenseData = new PieChartExpenseData();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH ,1);
        Date lastMonthDate = calendar.getTime();

        String strLastMonthDate = simpleDateFormat.format(lastMonthDate);
        strLastMonthDate = strLastMonthDate.substring(0, strLastMonthDate.length() - 2) + "01";

        //System.out.println(strLastMonthDate);
        TransactionType tType = TransactionType.Expense;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String fetchQuery = String.format("select sum(%s), %s from %s, %s where %s = '%s' and %s.%s = %s.%s and %s = %s and %s > '%s' group by(%s.%s);",
                TRANSACTION_AMOUNT, CATEGORY_NAME, TABLE_TRANSACTION, TABLE_CATEGORY, TRANSACTION_TYPE, tType.toString(), TABLE_CATEGORY,
                CATEGORY_ID, TABLE_TRANSACTION, TRANSACTION_FKEY_CATEGORY_ID, TRANSACTION_FKEY_USERS_ID, userID, TRANSACTION_DATE,
                strLastMonthDate, TABLE_TRANSACTION, TRANSACTION_FKEY_CATEGORY_ID);
        //System.out.println(fetchQuery);
        Cursor c = sqLiteDatabase.rawQuery(fetchQuery, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            pieChartExpenseData.add(c.getString(1), c.getInt(0));
            c.moveToNext();
        }
        sqLiteDatabase.close();
        return pieChartExpenseData;
    }

    public ArrayList<Integer> getLastMonthSpending(int userID) {
        ArrayList<Integer> expenses = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date lastMonthDate = calendar.getTime();
        String strCurrentDate = simpleDateFormat.format(lastMonthDate);
        strCurrentDate = strCurrentDate.substring(0, strCurrentDate.length() - 2) + "01";
        Map<String, String> categoryExpenseMap = new HashMap<>();
        ArrayList<String> allCategories = getAllCategories();
        for (String category: allCategories) {
            if (categoryExpenseMap.containsKey((String) category)) {
                String expense = categoryExpenseMap.get((String) category);
                expenses.add(Integer.parseInt(expense));
            }
            else {
                expenses.add(Integer.parseInt("0"));
            }
        }

        /*for (int e:expenses) {
            System.out.println(e);
        }*/
        return expenses;
    }

    public void initializeUserData(int userID) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String tempAddr = UserData.address;
        String DOB = UserData.dateOfBirth;
        String fetchQuery = String.format("select %s, %s, %s, %s from %s where %s = %s;",
                USERS_NAME, USERS_EMAIL, USERS_PHONENUMBER, USERS_PASSWORD, TABLE_USERS, USERS_ID, String.valueOf(userID));
        Cursor c = sqLiteDatabase.rawQuery(fetchQuery, null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            UserData.address = tempAddr;
            UserData.userID = userID;
            UserData.dateOfBirth = DOB;
            UserData.Name = c.getString(0);
            UserData.email = c.getString(1);
            UserData.phoneNumber = c.getString(2);
            UserData.password = c.getString(3);
            c.moveToNext();
        }

        UserData.categories = getAllCategories();
        getCategoryWiseExpenses();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category_id",1);
        contentValues.put("name","Travel");
        contentValues.put("budget",5000);
        db.insert(TABLE_CATEGORY, null, contentValues);
        db.close();
        SQLiteDatabase db1 = getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("category_id",2);
        contentValues1.put("name","Medical");
        contentValues1.put("budget",10000);
        db1.insert(TABLE_CATEGORY, null, contentValues1);
        db1.close();
        SQLiteDatabase db2 = getWritableDatabase();
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("category_id",3);
        contentValues2.put("name","Food");
        contentValues2.put("budget",10000);
        db2.insert(TABLE_CATEGORY, null, contentValues2);
        db2.close();
        SQLiteDatabase db3 = getWritableDatabase();
        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("category_id",4);
        contentValues3.put("name","Utility bills");
        contentValues3.put("budget",8000);
        db3.insert(TABLE_CATEGORY, null, contentValues3);
        db3.close();
        SQLiteDatabase db4 = getWritableDatabase();
        ContentValues contentValues4 = new ContentValues();
        contentValues4.put("category_id",5);
        contentValues4.put("name","Rent");
        contentValues4.put("budget",15000);
        db4.insert(TABLE_CATEGORY, null, contentValues4);
        db4.close();
        sqLiteDatabase.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_USERS;
        sqLiteDatabase.execSQL(dropTableQuery);
        onCreate(sqLiteDatabase);
    }

    public void addTransaction(String userID, int categoryID, String transactionType, String amount, String description, Date tdate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(tdate);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTION_FKEY_USERS_ID, userID);
        contentValues.put(TRANSACTION_DATE, date);
        contentValues.put(TRANSACTION_FKEY_CATEGORY_ID, String.valueOf(categoryID));
        contentValues.put(TRANSACTION_TYPE, transactionType);
        contentValues.put(TRANSACTION_AMOUNT, amount);
        contentValues.put(TRANSACTION_DESCRIPTION, description);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_TRANSACTION, null, contentValues);
        sqLiteDatabase.close();

    }

    public ArrayList<TransactionData> getTransactionData(int id, Date fromDate, Date toDate){
        //id, amount, dateTime, category, desc
        ArrayList<TransactionData> transactionData = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strFromDate = simpleDateFormat.format(fromDate);
        String strToDate = simpleDateFormat.format(toDate);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String fetchQuery = String.format("select %s.%s, %s.%s, %s.%s, %s.%s, %s.%s, %s.%s from %s, %s where %s.%s = %s.%s and %s.%s = %s and %s.%s between '%s' and '%s';",
                TABLE_TRANSACTION, TRANSACTION_ID, TABLE_TRANSACTION, TRANSACTION_AMOUNT, TABLE_TRANSACTION, TRANSACTION_DATE, TABLE_CATEGORY, CATEGORY_NAME, TABLE_TRANSACTION, TRANSACTION_DESCRIPTION,
                TABLE_TRANSACTION, TRANSACTION_TYPE, TABLE_TRANSACTION, TABLE_CATEGORY, TABLE_TRANSACTION, TRANSACTION_FKEY_CATEGORY_ID, TABLE_CATEGORY, CATEGORY_ID, TABLE_TRANSACTION,
                TRANSACTION_FKEY_USERS_ID, String.valueOf(id), TABLE_TRANSACTION, TRANSACTION_DATE, strFromDate, strToDate);
        //System.out.println(fetchQuery);
        Cursor c = sqLiteDatabase.rawQuery(fetchQuery, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Date date = null;
            try {
                date = simpleDateFormat.parse(c.getString(2));
            } catch (Exception e) {
                e.printStackTrace();
            }
            transactionData.add(new TransactionData(Integer.parseInt(c.getString(0)), c.getString(1), date, c.getString(3), c.getString(4), c.getString(5)));
            c.moveToNext();
        }
        sqLiteDatabase.close();
        return transactionData;
    }

    public ArrayList<String> getAllCategories () {
        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String fetchQuery = String.format("select * from %s", TABLE_CATEGORY);
        Cursor c = sqLiteDatabase.rawQuery(fetchQuery, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            categories.add(c.getString(1));
            c.moveToNext();
        }
        sqLiteDatabase.close();
        return categories;
    }

    public BarChartExpenseData getCustomDateTransactionData(int userID, Date fromDate, Date toDate) {
        BarChartExpenseData barChartExpenseData = new BarChartExpenseData();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strFromDate = simpleDateFormat.format(fromDate);
        String strToDate = simpleDateFormat.format(toDate);

        ArrayList<String> categories = getAllCategories();
        for (String category: categories) {
            String fetchQuery = String.format("select sum(%s), %s from %s, %s where %s.%s = %s.%s and %s.%s = '%s' and %s.%s = %s and %s.%s between '%s' and '%s' group by (%s);",
                    TRANSACTION_AMOUNT, TRANSACTION_TYPE, TABLE_TRANSACTION, TABLE_CATEGORY, TABLE_TRANSACTION, TRANSACTION_FKEY_CATEGORY_ID,
                    TABLE_CATEGORY, CATEGORY_ID, TABLE_CATEGORY, CATEGORY_NAME, category, TABLE_TRANSACTION, TRANSACTION_FKEY_USERS_ID, userID,
                    TABLE_TRANSACTION, TRANSACTION_DATE, strFromDate, strToDate, TRANSACTION_TYPE);
            System.out.println(fetchQuery);
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            Cursor c = sqLiteDatabase.rawQuery(fetchQuery, null);
            c.moveToFirst();
            int incomeAmount = 0;
            int expenseAmount = 0;
            while(!c.isAfterLast()) {
                if (c.getString(1).equals("income")) {
                    incomeAmount = Integer.parseInt(c.getString(0));
                }
                else {
                    expenseAmount = Integer.parseInt(c.getString(0));
                }
                c.moveToNext();
            }
            sqLiteDatabase.close();
            barChartExpenseData.add(category, expenseAmount, incomeAmount);
        }
        return barChartExpenseData;
    }

    public void makeNewCategory (String name, int budget) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, name);
        contentValues.put(CATEGORY_BUDGET, budget);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_CATEGORY, null, contentValues);
        sqLiteDatabase.close();
    }

    public ArrayList<Integer> getAllCategoryBudgets() {
        String fetchQuery = String.format("select %s from %s;", CATEGORY_BUDGET, TABLE_CATEGORY);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ArrayList<Integer> budgets = new ArrayList<>();
        Cursor c = sqLiteDatabase.rawQuery(fetchQuery, null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            budgets.add(Integer.parseInt(c.getString(0)));
            c.moveToNext();
        }
        sqLiteDatabase.close();
        return budgets;
    }

    public void updateTransactionDetails(int transactionID, TransactionType type, String amount, int categoryID, Date date, String description) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = simpleDateFormat.format(date);
        String updateQuery = String.format("update %s set %s='%s', %s=%s, %s=%s, %s='%s', %s='%s' where %s = %s;",
                TABLE_TRANSACTION, TRANSACTION_TYPE, type.toString(), TRANSACTION_AMOUNT, amount, TRANSACTION_FKEY_CATEGORY_ID, categoryID,
                TRANSACTION_DATE, strDate, TRANSACTION_DESCRIPTION, description, TRANSACTION_ID, transactionID);
        System.out.println(updateQuery);
        sqLiteDatabase.execSQL(updateQuery);
        sqLiteDatabase.close();
    }

    public void deleteTransaction(int transactionID) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String deleteQuery = String.format("delete from %s where %s = %s;", TABLE_TRANSACTION, TRANSACTION_ID, transactionID);
        System.out.println(deleteQuery);
        sqLiteDatabase.execSQL(deleteQuery);
        sqLiteDatabase.close();
    }

    public void updateUserData(int userID, String name, String email, String address, String dob, String phone, String password) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        SignUpActivity sa = new SignUpActivity();
        UserData.address = address;
        UserData.dateOfBirth = dob;
        String updateQuery = String.format("update %s set %s='%s', %s='%s', %s='%s', %s='%s' where %s = %s;", TABLE_USERS,
                USERS_NAME, name, USERS_EMAIL, email, USERS_PHONENUMBER, phone,
                USERS_PASSWORD, password, USERS_ID, userID);
        sqLiteDatabase.execSQL(updateQuery);
        sqLiteDatabase.close();
    }

    public ArrayList<Float> getCategoryWiseExpenses() {
        ArrayList<Float> expenses = new ArrayList<>();
        String fetchQuery = String.format("select distinct %s, (select sum(%s) from %s where %s = a.%s and %s = 'expense') from %s as a order by (%s);",
                CATEGORY_ID, TRANSACTION_AMOUNT, TABLE_TRANSACTION, CATEGORY_ID, CATEGORY_ID, TRANSACTION_TYPE,
                TABLE_TRANSACTION, CATEGORY_ID);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(fetchQuery, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            if (c.getString(1) == null) {
                expenses.add((float) 0.0);
            }
            else {
                expenses.add(Float.parseFloat(c.getString(1)));
            }
            c.moveToNext();
        }
        sqLiteDatabase.close();
        return  expenses;

    }

    //makeNewCategory("Travel",5000);

}
