package com.example.bala.studentassist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bala on 7/22/16.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {


    private List<MyPlace> placeList;
    public List<MyPlace> getPlaceList() {
        return placeList;
    }
    private OnPlaceClickListener listener;
    public void setPlaceList(List<MyPlace> placeList) {
        this.placeList = placeList;
    }

    public interface OnPlaceClickListener {
        void onPlaceClick(RealEstate place);
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder{
        public TextView name,rent;
        private View view;
        public PlaceViewHolder(View view)
        {
            super(view);
            this.view = view;
            name = (TextView)view.findViewById(R.id.place_name);
            rent = (TextView)view.findViewById(R.id.place_rent);
        }

        public void setListener(final RealEstate re)
        {
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view)
                {
                    listener.onPlaceClick(re);
                }
            });
        }
    }

    public PlaceAdapter() {}

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_low, parent, false);
        return new PlaceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int i) {
        RealEstate re = Singleton.realEstateList.get(i);
        holder.name.setText(re.getName());
        holder.rent.setText("Rent: "+re.getRent());
        holder.setListener(re);
    }

    @Override
    public int getItemCount() {
        return Singleton.realEstateList.size();
    }

    public OnPlaceClickListener getListener() {
        return listener;
    }

    public void setListener(OnPlaceClickListener listener) {
        this.listener = listener;
    }
}
