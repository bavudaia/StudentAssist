package com.example.bala.studentassist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bala on 8/1/16.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private List<MyPlace> historyList;
    public List<MyPlace> getHistoryList() {
        return historyList;
    }
    private OnHistoryClickListener listener;
    public void setHistoryList(List<MyPlace> historyList) {
        this.historyList = historyList;
    }

    public interface OnHistoryClickListener {
        void onHistoryClick(MyPlace place);
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        public TextView name, rating;
        private View view;
        public HistoryViewHolder(View view)
        {
            super(view);
            this.view = view;
            name = (TextView)view.findViewById(R.id.place_name);
            rating = (TextView)view.findViewById(R.id.place_rent);
        }

        public void setListener(final MyPlace re)
        {
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view)
                {
                    listener.onHistoryClick(re);
                }
            });
        }
    }

    public HistoryAdapter() {}

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_low, parent, false);
        return new HistoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int i) {
        MyPlace re = Singleton.historyList.get(i);
        holder.name.setText(re.getName());
        holder.rating.setText("Rating: "+re.getRating());
        holder.setListener(re);
    }

    @Override
    public int getItemCount() {
        return Singleton.historyList.size();
    }

    public OnHistoryClickListener getListener() {
        return listener;
    }

    public void setListener(OnHistoryClickListener listener) {
        this.listener = listener;
    }

}
