package com.selenua.scheduler;

import java.util.Calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MonthlyFragment extends Fragment {
    List<ScdlData> dataList = new ArrayList<>();
    ScheduleMonthlyAdapter adapter;
    RoomDB database;
    List<ScdlData> selectedDate = new ArrayList<>();
    TextView textView;
    Calendar selected;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("test", "onCreate");
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_monthly, container, false);

        RecyclerView recyclerView = relativeLayout.findViewById(R.id.scdl_monthlyrecycler);
        CalendarView calendarView = relativeLayout.findViewById(R.id.scdl_monthlycal);
        textView = relativeLayout.findViewById(R.id.scdl_monthlydatetxt);
        database = RoomDB.getInstance(relativeLayout.getContext());
        dataList = database.scdlDao().getAll();
        recyclerView.setLayoutManager(new LinearLayoutManager(relativeLayout.getContext()));

        selected = Calendar.getInstance();
        selectedDateSet(selected);
        adapter = new ScheduleMonthlyAdapter(getActivity(), selectedDate);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                selected.set(year, month, day);
                selectedDateSet(selected);
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }
                else{
                    Log.d("test","null");
                }
            }
        });
        recyclerView.setAdapter(adapter);


        return relativeLayout;
    }

    public void refresh(Calendar refreshDate) {
        if(adapter != null){
            Log.d("test", "List refresh(M)");
            dataList.clear();
            dataList.addAll(database.scdlDao().getAll());
            selectedDateSet(refreshDate);
            Log.d("test", dataList.size() + "");
            adapter.notifyDataSetChanged();
        }

        //((MainActivity)getActivity()).callFragment();
    }

    public void selectedDateSet(Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar cur = Calendar.getInstance();
        cur.set(year, month, day);
        textView.setText(Util.calendarToString(cur));
        selectedDate.clear();
        Log.d("test","List set(M)");
        for (int i = 0; i < dataList.size(); i++) {
            if (Util.checkDate(cur, Util.stringToCalendar(dataList.get(i).getStartDate()),
                    Util.stringToCalendar(dataList.get(i).getEndDate()))) {
                selectedDate.add(dataList.get(i));
            }
        }
    }
}
