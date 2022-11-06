package com.selenua.scheduler;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    List<ScdlData> dataList = new ArrayList<>();
    ScheduleListAdapter adapter;
    RoomDB database;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        LinearLayout linearLayout=  (LinearLayout)inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerView = linearLayout.findViewById(R.id.scdl_listrecycler);

        database = RoomDB.getInstance(linearLayout.getContext());
        dataList = database.scdlDao().getAll();
        recyclerView.setLayoutManager(new LinearLayoutManager(linearLayout.getContext()));
        adapter = new ScheduleListAdapter(getActivity(), dataList);
        recyclerView.setAdapter(adapter);

        return linearLayout;
    }



    public void refresh(){
        if(adapter != null){
            Log.d("test", "List refresh(L)");
            dataList.clear();
            dataList.addAll(database.scdlDao().getAll());


            Log.d("test", dataList.size() + "");
            adapter.notifyDataSetChanged();
        }
        //((MainActivity)getActivity()).callFragment();
    }
}