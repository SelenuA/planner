package com.selenua.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class CreateActivity extends AppCompatActivity {

    int datePick = 0;
    RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        database = RoomDB.getInstance(this);

        boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
        String title = getIntent().getStringExtra("title");
        String info = getIntent().getStringExtra("info");
        String startDateStr = getIntent().getStringExtra("startDate");
        String endDateStr = getIntent().getStringExtra("endDate");


        EditText titleInput = findViewById(R.id.scdl_edittitle);
        EditText infoInput = findViewById(R.id.scdl_editinfo);
        TextView startDateView = findViewById(R.id.scdl_startdate);
        TextView endDateView = findViewById(R.id.scdl_enddate);
        CalendarView dateInput = findViewById(R.id.scdl_datepicker);
        TimePicker timeInput = findViewById(R.id.scdl_timepicker);

        Button cancelBtn = findViewById(R.id.scdl_cancel);
        Button createBtn = findViewById(R.id.scdl_create);

        dateInput.setVisibility(View.GONE);
        timeInput.setVisibility(View.GONE);



        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        if(isEdit){
            titleInput.setText(title);
            infoInput.setText(info);
            startDate.setTimeInMillis(Util.stringToCalendar(startDateStr).getTimeInMillis());
            endDate.setTimeInMillis(Util.stringToCalendar(endDateStr).getTimeInMillis());
            createBtn.setText(getString(R.string.str_edit));
        }
        startDateView.setText(Util.calendarToString(startDate));
        endDateView.setText(Util.calendarToString(endDate));

        int selectedTextColor = ResourcesCompat.getColor(getResources(), R.color.secondary, null);
        int basicTextColor = ResourcesCompat.getColor(getResources(), R.color.text, null);

        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datePick == 1) {
                    datePick = 0;
                    dateInput.setVisibility(View.GONE);
                    startDateView.setTextColor(basicTextColor);
                    endDateView.setTextColor(basicTextColor);
                } else {
                    dateInput.setDate(startDate.getTimeInMillis());
                    datePick = 1;
                    dateInput.setVisibility(View.VISIBLE);
                    startDateView.setTextColor(selectedTextColor);
                    endDateView.setTextColor(basicTextColor);
                    Log.d("test", "dateInput Visible (for startDate)");
                }
                Log.d("test", "startDateView Clicked");
            }
        });
        endDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datePick == 2) {
                    datePick = 0;
                    dateInput.setVisibility(View.GONE);
                    startDateView.setTextColor(basicTextColor);
                    endDateView.setTextColor(basicTextColor);
                } else {
                    dateInput.setDate(endDate.getTimeInMillis());
                    datePick = 2;
                    dateInput.setVisibility(View.VISIBLE);
                    startDateView.setTextColor(basicTextColor);
                    endDateView.setTextColor(selectedTextColor);
                    Log.d("test", "dateInput Visible (for endDate)");
                }
                Log.d("test", "endDateView Clicked");
            }
        });


        infoInput.addTextChangedListener(new TextWatcher() {
            String prevStr = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevStr = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (infoInput.getLineCount() >= 4) {
                    infoInput.setText(prevStr);
                    infoInput.setSelection(infoInput.length());
                }
            }
        });
        dateInput.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                if (datePick == 1) {
                    startDate.set(year, month, day);
                    if (!Util.isDateQuicker(startDate, endDate)) {
                        endDate.set(year, month, day);
                        Log.d("test", "endDate quicker");
                    }
                }
                if (datePick == 2) {
                    endDate.set(year, month, day);
                    if (!Util.isDateQuicker(startDate, endDate)) {
                        startDate.set(year, month, day);
                        Log.d("test", "endDate quicker");
                    }
                }
                startDateView.setText(Util.calendarToString(startDate));
                endDateView.setText(Util.calendarToString(endDate));
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    String sTitle = titleInput.getText().toString();
                    String sInfo = infoInput.getText().toString();
                    String cStartDate = Util.calendarToString(startDate);
                    String cEndDate = Util.calendarToString(endDate);
                    int sID = getIntent().getIntExtra("id",0);
                    if (!sTitle.equals("")) {
                        ScdlData data = new ScdlData();
                        data.setTitle(sTitle);
                        data.setInfo(sInfo);
                        data.setStartDate(cStartDate);
                        data.setEndDate(cEndDate);
                        database.scdlDao().update(sID, sTitle, sInfo, cStartDate, cEndDate);

                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    String sTitle = titleInput.getText().toString();
                    String sInfo = infoInput.getText().toString();
                    String cStartDate = Util.calendarToString(startDate);
                    String cEndDate = Util.calendarToString(endDate);
                    if (!sTitle.equals("")) {
                        ScdlData data = new ScdlData();
                        data.setTitle(sTitle);
                        data.setInfo(sInfo);
                        data.setStartDate(cStartDate);
                        data.setEndDate(cEndDate);
                        database.scdlDao().insert(data);

                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }
        });
    }
}