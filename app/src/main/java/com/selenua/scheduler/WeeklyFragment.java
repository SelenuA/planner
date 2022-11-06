package com.selenua.scheduler;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WeeklyFragment extends Fragment {
    List<ScdlData> dataList = new ArrayList<>();
    ScheduleWeeklyAdapter adapter;
    RoomDB database;
    List<ScdlData> list = new ArrayList<>();
    Calendar wklyCalendar;
    int selectedDate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_weekly, container, false);

        TextView dateView[] = {linearLayout.findViewById(R.id.scdl_wkly_yesterday),
                linearLayout.findViewById(R.id.scdl_wkly_today),
                linearLayout.findViewById(R.id.scdl_wkly_tomorrow),
                linearLayout.findViewById(R.id.scdl_wkly_twodlater),
                linearLayout.findViewById(R.id.scdl_wkly_thrdlater)};
        Calendar datePicked[] = {Calendar.getInstance(),
                Calendar.getInstance(), Calendar.getInstance(),
                Calendar.getInstance(), Calendar.getInstance()};
        RecyclerView recyclerView = linearLayout.findViewById(R.id.scdl_wkly_list);
        int selectedTextColor = ResourcesCompat.getColor(getResources(), R.color.secondary, null);
        int basicTextColor = ResourcesCompat.getColor(getResources(), R.color.text, null);
        selectedDate = 1;
        recyclerView.setLayoutManager(new LinearLayoutManager(linearLayout.getContext()));

        database = RoomDB.getInstance(linearLayout.getContext());
        dataList = database.scdlDao().getAll();
        adapter = new ScheduleWeeklyAdapter(getActivity(), list);

        wklyCalendar = Calendar.getInstance();
        wklyCalendar.add(Calendar.DAY_OF_MONTH, -1);
        for (int i = 0; i < 5; i++) {
            dateView[i].setText((wklyCalendar.get(Calendar.MONTH) + 1) + "/" + wklyCalendar.get(Calendar.DAY_OF_MONTH));
            datePicked[i].setTimeInMillis(wklyCalendar.getTimeInMillis());
            wklyCalendar.add(Calendar.DAY_OF_MONTH, 1);
            dateView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < 5; j++) {
                        dateView[j].setTextColor(basicTextColor);
                    }
                    selectedDate = Arrays.asList(dateView).indexOf(view);
                    dateView[selectedDate].setTextColor(selectedTextColor);
                    selectedDateSet(datePicked[selectedDate]);
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    Log.d("test", "wkly date changed");
                }
            });
        }

        selectedDateSet(datePicked[selectedDate]);
        dateView[selectedDate].setTextColor(selectedTextColor);

        recyclerView.setAdapter(adapter);
        return linearLayout;
    }

    public void refresh(Calendar refreshDate) {
        if (adapter != null) {
            Log.d("test", "List refresh(W)");
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
        list.clear();
        Log.d("test", "List set(W)");
        for (int i = 0; i < dataList.size(); i++) {
            if (Util.checkDate(cur, Util.stringToCalendar(dataList.get(i).getStartDate()),
                    Util.stringToCalendar(dataList.get(i).getEndDate()))) {
                list.add(dataList.get(i));
            }
        }
        Log.d("test", list.size() + "");
    }
}
