package com.example.bala.studentassist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;
import java.util.Map;

/**
 * Created by bala on 7/23/16.
 */
public class DetailFragment extends Fragment {
    private RealEstate realEstate;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    private TextView name;
    private TextView rating;
    private TextView vicinity;
    private ImageView imageView;

    private TextView rent;
    private TextView cafe,hospital,atm,airport,bus_station,train_station;
    private CardView cardView1,cardView2,cardView3,cardView4,cardView5,cardView6;
    private Button callButton;
    private RequestQueue rq;


    public static final String EXTRA_PLACE_ID = "Extra Place Id";
    public static final String EXTRA_POS_INDEX = "PositionIndex";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_detail, container, false);
        if(savedInstanceState!=null)
        {
            realEstate = (RealEstate) savedInstanceState.getSerializable(KEY_PLACE);
        }
        name = (TextView)v.findViewById(R.id.detail_name);
        name.setText(realEstate.getName());

        rent = (TextView)v.findViewById(R.id.rent);
        rent.setText("$ "+ realEstate.getRent());
            
        Map<String,MyPlace> pm  = realEstate.getNearbyPlacesMap();
        cafe = (TextView)v.findViewById(R.id.cafe);
        if(pm.get("cafe") != null)
            cafe.setText("Cafe : "+pm.get("cafe").getName());
        else
            cafe.setText("Cafe : "+"Not in a miles distance" + "");

        hospital = (TextView)v.findViewById(R.id.hospital);
        if(pm.get("hospital") != null)
            hospital.setText("Hospital : "+pm.get("hospital").getName());
        else
            hospital.setText("Hospital : "+"Not in a miles distance" + "");

        atm = (TextView)v.findViewById(R.id.atm);
        if(pm.get("atm") != null)
            atm.setText("ATM : "+pm.get("atm").getName());
        else
            atm.setText("ATM : "+"Not in a miles distance" + "");

        airport = (TextView)v.findViewById(R.id.airport);
        if(pm.get("airport") != null)
            airport.setText("Airport : "+pm.get("airport").getName());
        else
            airport.setText("Airport : "+"Not in a miles distance" + "");

        bus_station = (TextView)v.findViewById(R.id.bus_station);
        if(pm.get("bus_station") != null)
            bus_station.setText("Bus Station : "+pm.get("bus_station").getName());
        else
            bus_station.setText("Bus Station : "+"Not in a miles distance" + "");

        train_station = (TextView)v.findViewById(R.id.train_station);
        if(pm.get("train_station") != null)
            train_station.setText("Train Station : "+pm.get("train_station").getName());
        else
            train_station.setText("Train Station : "+"Not One in a miles distance" + "");

        callButton = (Button)v.findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+ realEstate.getPhoneNumber()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });

        cardView1 = (CardView)v.findViewById(R.id.card_view1);
        cardView2 = (CardView)v.findViewById(R.id.card_view2);
        cardView3 = (CardView)v.findViewById(R.id.card_view3);
        cardView4 = (CardView)v.findViewById(R.id.card_view4);
        cardView5 = (CardView)v.findViewById(R.id.card_view5);
        cardView6 = (CardView)v.findViewById(R.id.card_view6);
        setCardViewListeners();

        return v;
    }


    private void setCardViewListeners()
    {
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AtmActivity.class);
                intent.putExtra(EXTRA_POS_INDEX,position);
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),HospitalActivity.class);
                intent.putExtra(EXTRA_POS_INDEX,position);
                startActivity(intent);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AirportActivity.class);
                intent.putExtra(EXTRA_POS_INDEX,position);
                startActivity(intent);
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),BusStationActivity.class);
                intent.putExtra(EXTRA_POS_INDEX,position);
                startActivity(intent);
            }
        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),TrainStationActivity.class);
                intent.putExtra(EXTRA_POS_INDEX,position);
                startActivity(intent);
            }
        });
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CafeActivity.class);
                intent.putExtra(EXTRA_POS_INDEX,position);
                startActivity(intent);
            }
        });
    }

    private void getImage(MyPlace place )
    {
        List<Photo> photos = place.getPhotos();
        if(photos == null || photos.size() ==0)
            return;
        rq = Volley.newRequestQueue(getContext());
        String url = "https://maps.googleapis.com/maps/api/realEstate/photo?key="
                + MyRequest.KEY
                +"&photoreference="+photos.get(0).getPhoto_reference()
                +"&maxheight="+PhotoAttr.max_height
                +"&maxwidth"  + PhotoAttr.max_width
                ;
        // Request a string response from the provided URL.

        ImageRequest imageRequest = new ImageRequest(
                 url
                , new PhotoResponseListener(imageView)
                , 0,0,null,new PhotoErrorListener());
        /*
        StringRequest stringRequest = new StringRequest(Request.Method.GET
                , url
                , new PhotoResponseListener(imageView)
                , new PhotoErrorListener());
                */
        imageRequest.setTag(P_REQ_TAG);
        rq.add(imageRequest);
    }

  private static final String P_REQ_TAG = "Photo Request Tag";
    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
    private static final String KEY_PLACE = "KeyPlace";
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_PLACE, realEstate);
    }

    public static DetailFragment newInstance(int  pos)
    {
        RealEstate place=  Singleton.getInstance().realEstateList.get(pos);
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PLACE_ID,place.getId());
        detailFragment.setArguments(args);
        detailFragment.setRealEstate(place);
        detailFragment.setPosition(pos);
        return detailFragment;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }
}