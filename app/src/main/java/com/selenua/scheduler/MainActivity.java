package com.selenua.scheduler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    ListFragment listFragment;
    WeeklyFragment weeklyFragment;
    MonthlyFragment monthlyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.scdl_viewpager);
        TabLayout tabLayout = findViewById(R.id.scdl_tabs);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.scdl_fabtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                intent.putExtra("isEdit",false);
                startActivityForResult(intent,1);
            }
        });
    }
    class MyPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments = new ArrayList<>();
        private String titles[] = {"List","Weekly","Monthly"};
        public MyPagerAdapter(FragmentManager fm){
            super(fm);
            listFragment = new ListFragment();
            weeklyFragment = new WeeklyFragment();
            monthlyFragment = new MonthlyFragment();
            fragments.add(listFragment);
            fragments.add(weeklyFragment);
            fragments.add(monthlyFragment);
        }
        @Override
        public Fragment getItem(int position){
            return this.fragments.get(position);
        }
        @Override
        public int getCount(){
            return this.fragments.size();
        }
        @Override
        public CharSequence getPageTitle(int position){
            return titles[position];
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Calendar cal = Calendar.getInstance();

        if(resultCode == RESULT_OK){
            listFragment.refresh();
            monthlyFragment.refresh(cal);
            weeklyFragment.refresh(cal);
        }

    }

/*    public void callFragment(){

    }*/

}