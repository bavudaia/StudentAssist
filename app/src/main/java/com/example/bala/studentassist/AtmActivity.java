package com.example.bala.studentassist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;
import java.util.Map;


public class AtmActivity extends Activity {
    private TextView name;
    private TextView rating;
    private TextView vicinity;
    private ImageView imageView;
    private RequestQueue rq;
    private RealEstate realEstate;
    private MyPlace place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm);

        int pos = getIntent().getIntExtra(DetailFragment.EXTRA_POS_INDEX,0);

        realEstate = Singleton.getInstance().realEstateList.get(pos);
        Map<String,MyPlace> map = realEstate.getNearbyPlacesMap();
        MyPlace place = map.get(MyRequest.ATM);
        this.place = place;

        if(savedInstanceState!=null)
        {
            place = (MyPlace) savedInstanceState.getSerializable(KEY_PLACE);
            this.place = place;
        }

        if(place!=null) {
            name = (TextView) findViewById(R.id.detail_name_atm);
            name.setText(place.getName());

            rating = (TextView) findViewById(R.id.rating_atm);
            rating.setText("Rating : " + place.getRating());

            vicinity = (TextView) findViewById(R.id.vicinity_atm);
            vicinity.setText(place.getVicinity());

            imageView = (ImageView) findViewById(R.id.image_atm);

            getImage(place);
        }

    }

    private void getImage(MyPlace place )
    {
        List<Photo> photos = place.getPhotos();
        if(photos == null || photos.size() ==0)
            return;
        rq = Volley.newRequestQueue(this);
        String url = "https://maps.googleapis.com/maps/api/place/photo?key="
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
    private static final String KEY_PLACE = "KeyPlace";
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_PLACE, place);
    }
}

