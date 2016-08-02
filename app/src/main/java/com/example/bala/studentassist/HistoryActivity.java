package com.example.bala.studentassist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

public class HistoryActivity extends Activity {
    private  List<MyPlace> historyList;
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;

    public List<MyPlace> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<MyPlace> historyList) {
        this.historyList = historyList;
    }


    public static final String EXTRA_INTENT = "HistoryList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_history);

        populateHistoryList();

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_history);
        historyAdapter = new HistoryAdapter();
        historyAdapter.setHistoryList(Singleton.getInstance().historyList);

        historyAdapter.setListener(new HistoryAdapter.OnHistoryClickListener() {
            @Override
            public void onHistoryClick(MyPlace place) {
                Intent intent = new Intent(HistoryActivity.this,MapsActivity.class);
                intent.putExtra(HistoryActivity.HISTORY_EXTRA,place.getId());

                //startActivity(intent);
            }
        });

        recyclerView.setAdapter(historyAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public static final String HISTORY_EXTRA = "HistoryExtra";


    public void populateHistoryList()
    {
        //historyList = (List) getIntent().getParcelableArrayListExtra(EXTRA_INTENT);
        historyList = Singleton.getInstance().historyList;
        Log.d(MapsActivity.TAG,"SecondList Activity");
        Log.d(MapsActivity.TAG, historyList ==null?"null": historyList.toString());
        //historyAdapter.notifyDataSetChanged();
    }
}
