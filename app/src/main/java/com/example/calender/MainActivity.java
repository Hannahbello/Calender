package com.example.calender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.provider.CalendarContract.*;

public class MainActivity extends AppCompatActivity {

    private DataHelper dataHelper;
    private String selectedDate;
    private SQLiteDatabase sqLiteDatabase;

    CalendarView calendarView;
    TextView sDaysText,sMonthText,sYearText;
    EditText textInput;
    Button saveEventButton;
    View eventContent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        calendarView = findViewById(R.id.calenderView);
        sDaysText = findViewById(R.id.selectedDays);
        sMonthText = findViewById(R.id.selectedMonth);
        sYearText = findViewById(R.id.selectedYear);
        textInput = findViewById(R.id.textInput);
        saveEventButton = findViewById(R.id.saveButton);
        eventContent = findViewById(R.id.dayEvent);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                sDaysText.setText("Selected Day: "+dayOfMonth);

                sMonthText.setText("Selected Month: "+month);

                sYearText.setText("Selected Year: "+year);

                selectedDate = Integer.toString(year) +Integer.toString(month) + Integer.toString(dayOfMonth);

                if (eventContent.getVisibility() == View.GONE){
                    eventContent.setVisibility(View.VISIBLE);
                }

                getEvent(view);
            }
        });

        try {
            dataHelper = new DataHelper(this,"CalenderDatabase",null,1);
            sqLiteDatabase = dataHelper.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE EVENTCALENDER( DATE TEXT,EVENT TEXT)");
        }catch (Exception e){
            e.printStackTrace();
        }

    } 

    public void insertEvent(View view){
        ContentValues values = new ContentValues();
        values.put("DATE",selectedDate);
        values.put("EVENT",textInput.getText().toString());
        sqLiteDatabase.insert("EVENTCALENDER",null,values);
    }

    public void getEvent(View view){
        String query = "SELECT EVENT FROM EVENTCALENDER WHERE DATE =" + selectedDate;

        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query,null);
            cursor.moveToNext();
            textInput.setText(cursor.getString(0));
        }catch (Exception e){
            e.printStackTrace();
            textInput.setText("");
        }
    }
}