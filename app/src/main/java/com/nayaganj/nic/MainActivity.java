package com.nayaganj.nic;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.security.PrivateKey;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TableLayout Payment_Table;
    TableLayout Receive_Table;
    EditText CreditDate;
    EditText Amount;
    EditText DebitDate;
    EditText Rate_Of_Interest;
    EditText BufferDays;
    TextView Interest;
    TextView Dis_PAY;
    TextView Dis_Rec;
    double TIntPay = 0, TPay = 0;
    double TIntRec = 0, TRec = 0;
    private NumberPicker dayPicker, monthPicker, yearPicker;
    private double TotalAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.AppBar);
        setSupportActionBar(toolbar);

        EditText CD1 = findViewById(R.id.CD1);
        CD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(CD1);
            }
        });

        EditText DD1 = findViewById(R.id.DD1);
        DD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(DD1);
            }
        });

        EditText CDR1 = findViewById(R.id.CDR1);
        CDR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(CDR1);
            }
        });

        EditText DDR1 = findViewById(R.id.DDR1);
        DDR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(DDR1);
            }
        });

        Payment_Table = findViewById(R.id.Payment_Table);
        Receive_Table = findViewById(R.id.Receive_Table);

        ImageButton Payment_Add = findViewById(R.id.Payment_Add);
        Payment_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow(5);
            }
        });

        ImageButton Receive_Add = findViewById(R.id.Receive_Add);
        Receive_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRow(4);
            }
        });

        ImageButton Payment_Del = findViewById(R.id.Payment_Del);
        Payment_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Payment_Table.getChildCount() == 2) {
                    Toast.makeText(MainActivity.this, "All EXTRA Rows in Payment\n Table Are already Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the last row of the table
                    TableRow lastRow = (TableRow) Payment_Table.getChildAt(Payment_Table.getChildCount() - 1);
                    // Remove the last row from the table
                    Payment_Table.removeView(lastRow);
                }
            }
        });

        ImageButton Receive_Del = findViewById(R.id.Receive_Del);
        Receive_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Receive_Table.getChildCount() == 2) {
                    Toast.makeText(MainActivity.this, "All EXTRA Rows in Receive\n Table Are already Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the last row of the table
                    TableRow lastRow = (TableRow) Receive_Table.getChildAt(Receive_Table.getChildCount() - 1);
                    // Remove the last row from the table
                    Receive_Table.removeView(lastRow);
                }
            }
        });

        Button CalculatePay = findViewById(R.id.SumTotal_Payment);
        CalculatePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatePAY();
            }
        });

        Button CalculateReceive = findViewById(R.id.SumTotal_Received);
        CalculateReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculateReceive();
            }
        });

        Button Hisab = findViewById(R.id.Hisaab);
        Hisab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatePAY();
                CalculateReceive();
                TextView Dis_Hisaab = findViewById(R.id.Display_Hisab);
                TotalAmt = TPay - TRec;
                DecimalFormat df = new DecimalFormat("##00.00");
                TotalAmt = Double.parseDouble(df.format(TotalAmt));
                String Result = "NET Balance Amount:  " + TotalAmt;
                Dis_Hisaab.setText(Result);
            }
        });
    }

    private void setDate(EditText dateEditText) {
        // Create a new layout for the date picker
        View datePickerLayout = getLayoutInflater().inflate(R.layout.date_picker_layout, null);
        dayPicker = datePickerLayout.findViewById(R.id.dayPicker);
        monthPicker = datePickerLayout.findViewById(R.id.monthPicker);
        yearPicker = datePickerLayout.findViewById(R.id.yearPicker);
        Button okButton = datePickerLayout.findViewById(R.id.ok_button);

        // Set max and min values for year picker
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int maxYear = currentYear + 5;
        int minYear = currentYear - 25;
        yearPicker.setMaxValue(maxYear);
        yearPicker.setMinValue(minYear);

        // Set max and min values for month picker
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        // Set max and min values for day picker
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);

        // Set on value change listener for day picker
        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateDate(dateEditText);
            }
        });

        // Set on value change listener for month picker
        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateDate(dateEditText);
            }
        });

        // Set on value change listener for year picker
        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateDate(dateEditText);
            }
        });

        // Set the default date to the current date
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));


        // Set the default values for day, month, and year pickers
        dayPicker.setValue(calendar.get(Calendar.DAY_OF_MONTH));
        monthPicker.setValue(calendar.get(Calendar.MONTH) + 1);
        yearPicker.setValue(calendar.get(Calendar.YEAR));

        // Show the date picker dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(datePickerLayout);
        final AlertDialog alertDialog = alertDialogBuilder.show();

        // Set the on click listener for the OK button
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate(dateEditText);
                alertDialog.dismiss();
            }
        });
    }

    private void updateDate(EditText dateEditText) {
        int day = dayPicker.getValue();
        int month = monthPicker.getValue() - 1;
        int year = yearPicker.getValue();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String selectedDate = dateFormat.format(calendar.getTime());

        dateEditText.setText(selectedDate);
    }

    private void addNewRow(int choice) {
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        tableRow.setBackgroundColor(getResources().getColor(R.color.NEW_ROW));
        // Get the first row of the table to use as a reference for the layout parameters
        TableRow firstRow;
        if (choice == 5) {
            firstRow = (TableRow) ((TableLayout) findViewById(R.id.Payment_Table)).getChildAt(0);
        } else {
            firstRow = (TableRow) ((TableLayout) findViewById(R.id.Receive_Table)).getChildAt(0);
        }
        // Set the layout parameters of the new row to match the layout parameters of the first row
        TableRow.LayoutParams columnLayoutParams = (TableRow.LayoutParams) firstRow.getChildAt(0).getLayoutParams();

        EditText CreditDate = new EditText(this);
        CreditDate.setLayoutParams(columnLayoutParams);
        CreditDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        CreditDate.setTypeface(Typeface.create("sans-serif-black", Typeface.NORMAL));
        CreditDate.setHint(R.string.DateFormat);
        CreditDate.setTextSize(20);
        CreditDate.setTextColor(Color.WHITE);
        CreditDate.setHintTextColor(getResources().getColor(R.color.hint_color));
        CreditDate.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        CreditDate.setGravity(Gravity.CENTER);
        CreditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(CreditDate);
            }
        });

//        // Set the layout parameters for the EditText view
//        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(141, TableRow.LayoutParams.MATCH_PARENT, 2f);

        tableRow.addView(CreditDate, 0);

        columnLayoutParams = (TableRow.LayoutParams) firstRow.getChildAt(1).getLayoutParams();

        EditText amt = new EditText(this);
        amt.setInputType(InputType.TYPE_CLASS_NUMBER);
        amt.setHint(R.string.Amount);
        amt.setTextSize(20);
        amt.setTypeface(Typeface.create(String.valueOf(R.font.amaranth_bold_italic), Typeface.NORMAL));
        amt.setTextColor(Color.WHITE);
        amt.setGravity(Gravity.CENTER);
        amt.setHintTextColor(getResources().getColor(R.color.hint_color));
        amt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        amt.setLayoutParams(columnLayoutParams);

        tableRow.addView(amt, 1);

        columnLayoutParams = (TableRow.LayoutParams) firstRow.getChildAt(2).getLayoutParams();

        EditText DebitDate = new EditText(this);
        DebitDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        DebitDate.setHint(R.string.DateFormat);
        DebitDate.setTextSize(20);
        DebitDate.setTypeface(Typeface.create("sans-serif-black", Typeface.NORMAL));
        DebitDate.setTextColor(Color.WHITE);
        DebitDate.setGravity(Gravity.CENTER);
        DebitDate.setHintTextColor(getResources().getColor(R.color.hint_color));
        DebitDate.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        DebitDate.setGravity(Gravity.CENTER);
        DebitDate.setLayoutParams(columnLayoutParams);

        DebitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(DebitDate);
            }
        });

        tableRow.addView(DebitDate, 2);

        columnLayoutParams = (TableRow.LayoutParams) firstRow.getChildAt(3).getLayoutParams();

        EditText RoI = new EditText(this);
        RoI.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        RoI.setHint(R.string.Common_Rate);
        RoI.setTextSize(20);
        RoI.setTypeface(Typeface.create(String.valueOf(R.font.amaranth_bold_italic), Typeface.BOLD_ITALIC));
        RoI.setTextColor(Color.WHITE);
        RoI.setGravity(Gravity.CENTER);
        RoI.setHintTextColor(getResources().getColor(R.color.hint_color));
        RoI.setLayoutParams(columnLayoutParams);
        tableRow.addView(RoI);

        TextView Interest = new TextView(this);
        Interest.setTextSize(20);
        Interest.setTypeface(Typeface.create(String.valueOf(R.font.amaranth_bold_italic), Typeface.BOLD_ITALIC));
        Interest.setTextColor(getResources().getColor(R.color.Interest_Color));
        Interest.setGravity(Gravity.CENTER);

        if (choice == 5) {

            // Set the layout parameters for the EditText view
            columnLayoutParams = (TableRow.LayoutParams) firstRow.getChildAt(4).getLayoutParams();

            EditText buff = new EditText(this);
            buff.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            buff.setHint(R.string.Buff_Hint);
            buff.setTextSize(20);
            buff.setTextColor(Color.WHITE);
            buff.setGravity(Gravity.CENTER);
            buff.setHintTextColor(getResources().getColor(R.color.hint_color));
            buff.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
            // Set the layout parameters for the buff view
            buff.setLayoutParams(columnLayoutParams);
            tableRow.addView(buff, 4);

            // Set the layout parameters for the Interest view
            columnLayoutParams = (TableRow.LayoutParams) firstRow.getChildAt(5).getLayoutParams();
            Interest.setLayoutParams(columnLayoutParams);
            tableRow.addView(Interest, 5);

            Payment_Table.addView(tableRow);
        }
        if (choice == 4) {
            // Set the layout parameters for the EditText view
            columnLayoutParams = (TableRow.LayoutParams) firstRow.getChildAt(4).getLayoutParams();
            Interest.setLayoutParams(columnLayoutParams);
            tableRow.addView(Interest, 4);

            Receive_Table.addView(tableRow);
        }
    }

    private void CalculatePAY() {
        int rowCount = Payment_Table.getChildCount();
        Dis_PAY = findViewById(R.id.Display_Total_PAY_Interest);
        String Result;
        TPay = 0;
        TIntPay = 0;
        for (int i = 1; i < rowCount; i++) {
            TableRow row = (TableRow) Payment_Table.getChildAt(i);
            CreditDate = (EditText) row.getChildAt(0);
            Amount = (EditText) row.getChildAt(1);
            DebitDate = (EditText) row.getChildAt(2);
            Rate_Of_Interest = (EditText) row.getChildAt(3);
            BufferDays = (EditText) row.getChildAt(4);
            Interest = (TextView) row.getChildAt(5);
            if (i == 1)
                Calculate(1);
            else Calculate(2);//for index greater than 1 -->2,3,4
        }
        if (TPay != 0 || TIntPay != 0) {
            DecimalFormat df = new DecimalFormat("##00.00");
            TIntPay = Double.parseDouble(df.format(TIntPay));
            TPay = Double.parseDouble(df.format(TPay));
            Result = "Total Pay Interest --> " + TIntPay + "\nTotal Pay Amount :  " + TPay;
        } else {
            Result = "Total Pay Interest -->\tNil\nTotal Pay Amount :\tNil";
            System.out.println(Result);
        }
        Dis_PAY.setText(Result);
    }

    private void CalculateReceive() {
        int rowCount = Receive_Table.getChildCount();
        Dis_Rec = findViewById(R.id.Display_Total_RECEIVE_Interest);
        String Result;
        TRec = 0;
        TIntRec = 0;
        for (int i = 1; i < rowCount; i++) {
            TableRow row = (TableRow) Receive_Table.getChildAt(i);
            CreditDate = (EditText) row.getChildAt(0);
            Amount = (EditText) row.getChildAt(1);
            DebitDate = (EditText) row.getChildAt(2);
            Rate_Of_Interest = (EditText) row.getChildAt(3);
            Interest = (TextView) row.getChildAt(4);
            Calculate(3);
        }
        if (TRec != 0 || TIntRec != 0) {
            DecimalFormat df = new DecimalFormat("##00.00");
            TIntRec = Double.parseDouble(df.format(TIntRec));
            TRec = Double.parseDouble(df.format(TRec));
            Result = "Total Receive Interest --> " + TIntRec + "\nTotal Receive Amount :  " + TRec;
        } else {
            Result = "Total Receive Interest -->\tNil\nTotal Receive Amount :\tNil ";
        }
        System.out.println(Result);
        Dis_Rec.setText(Result);
    }

    private void Calculate(int ch) {
        if (TextUtils.isEmpty(Amount.getText().toString()) || TextUtils.isEmpty(CreditDate.getText().toString())) {
            Toast.makeText(MainActivity.this, "Please Give Necessary Data", Toast.LENGTH_SHORT).show();
        } else {
            int Buff = 0;
            String S1 = CreditDate.getText().toString();
            String S2;
            double roi;
            if (ch == 1) {
                if (!(TextUtils.isEmpty(BufferDays.getText().toString())))
                    Buff = Integer.parseInt(BufferDays.getText().toString());
                else
                    BufferDays.setText("0");
            } else if (ch == 2) {
                if (!(TextUtils.isEmpty(BufferDays.getText().toString())))
                    Buff = Integer.parseInt(BufferDays.getText().toString());
                else {
                    TableRow row = (TableRow) Payment_Table.getChildAt(1);
                    EditText Buffer = (EditText) row.getChildAt(4);
                    Buff = Integer.parseInt(Buffer.getText().toString());
                    BufferDays.setText(Buffer.getText().toString());
                }
            }

            if ((TextUtils.isEmpty(DebitDate.getText().toString())) || ch == 2) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                S2 = sdf.format(new Date());
                DebitDate.setText(S2);
            } else {
                S2 = DebitDate.getText().toString();
            }

            if (TextUtils.isEmpty(Rate_Of_Interest.getText().toString())) {
                roi = 2.0;
                Rate_Of_Interest.setText("2.0");
            } else {
                roi = Double.parseDouble(Rate_Of_Interest.getText().toString());
            }

            roi = roi / 100;
            double CredAmt = Double.parseDouble(Amount.getText().toString());
            long Days = getDays(S1, S2) - Buff;
            System.out.println(Days);
            DecimalFormat df = new DecimalFormat("##00.00");
            double interest;
            if (Days <= 0) {
                Interest.setText(R.string.nil);
                interest = 0;
//                    TAmtView.setText("Total Amount : " + df.format(CredAmt));
            } else {
                interest = (CredAmt * roi) / 30.0;
                interest = interest * Days;
                Interest.setText(df.format(interest));
//                    TAmtView.setText("Total Amount : " + df.format((CredAmt + interest)));
            }
            if (ch <= 2) {
                TIntPay += interest;
                TPay += CredAmt + interest;
            } else if (ch == 3) {
                TIntRec += interest;
                TRec += CredAmt + interest;
            }
        }
    }

    private void getDalaaLi(){

    }
    private long getDays(String S1, String S2) {
        try {
            long days;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date dateBefore = sdf.parse(S1);
            Date dateAfter = sdf.parse(S2);

            // Calculate the number of days between dates
            long timeDiff = (dateAfter.getTime() - dateBefore.getTime());
            if (timeDiff < 0) {
                System.out.println("Days are less than after date");
                return 0L;
            } else {
                days = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
                System.out.println("The number of days between dates: " + days);
                return days;
            }
        } catch (Exception e) {
            Log.e("parse Error", "getDays : parse ki problem  days=-1");
            return -1;
        }
    }

}