package com.selenua.scheduler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.selenua.scheduler.RoomDB;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {
    private List<ScdlData> localDataSet;
    private Activity context;
    private RoomDB database;

    public ScheduleListAdapter(Activity context, List<ScdlData> dataSet) {
        this.context = context;
        localDataSet = dataSet;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView infoView;
        TextView sDateView;
        TextView eDateView;
        RelativeLayout itemLayout;
        RelativeLayout hiddenLayout;
        ImageView editBtn;
        ImageView deleteBtn;

        public ViewHolder(View view) {
            super(view);
            titleView = view.findViewById(R.id.scdl_listitem_title);
            infoView = view.findViewById(R.id.scdl_listitem_info);
            sDateView = view.findViewById(R.id.scdl_listitem_startdate);
            eDateView = view.findViewById(R.id.scdl_listitem_enddate);
            itemLayout = view.findViewById(R.id.scdl_listitem_bg);
            hiddenLayout = view.findViewById(R.id.scdl_listitem_hiddenbg);
            editBtn = view.findViewById(R.id.scdl_listitem_edit);
            deleteBtn = view.findViewById(R.id.scdl_listitem_delete);
            hiddenLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public ScheduleListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_schedule_list, viewGroup, false);
        return new ScheduleListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleListAdapter.ViewHolder viewHolder, final int position) {
        ScdlData schedule = localDataSet.get(position);
        String title = schedule.getTitle();
        String info = schedule.getInfo();
        String startDate = schedule.getStartDate();
        String endDate = schedule.getEndDate();
        viewHolder.titleView.setText(title);
        viewHolder.infoView.setText(info);
        viewHolder.sDateView.setText(startDate);
        viewHolder.eDateView.setText("~" + endDate);
        database = RoomDB.getInstance(context);
        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),CreateActivity.class);
                intent.putExtra("isEdit",true);
                intent.putExtra("title",title);
                intent.putExtra("info",info);
                intent.putExtra("startDate",startDate);
                intent.putExtra("endDate",endDate);
                intent.putExtra("id",schedule.getId());
                context.startActivityForResult(intent,1);
            }
        });
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScdlData scdlData = localDataSet.get(viewHolder.getAdapterPosition());

                database.scdlDao().delete(scdlData);

                int position = viewHolder.getAdapterPosition();
                localDataSet.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, localDataSet.size());
            }
        });
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.hiddenLayout.getVisibility() == View.VISIBLE) {
                    viewHolder.hiddenLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.hiddenLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
