package com.example.bala.studentassist;

import android.app.FragmentManager;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, PlaceSelectionListener
        , GoogleMap.OnMyLocationButtonClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener , com.google.android.gms.location.LocationListener{

    private GoogleMap mMap;
    private PlaceAutocompleteFragment autocompleteFragment;
    public static final String REQ_TAG = "NearbyTag";
    private StringRequest stringRequest;
    private RequestQueue mRequestQueue;
    private Button nextButton;
    private volatile List<MyPlace> placeList;
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static  final String RESPONSE_TAG = "RESPONSE_TAG";
    private CoordinatorLayout coordinatorLayout;
    private String emailAddress;
    private String history_place_id ;
    private Bundle savedData;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    private Button historyButton;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 121;
    public static final String LOC_TAG = "LOC_TAG";

    @Override
    protected  void  onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("Location",location==null?"null":location.toString());
        outState.putString("EmailAddress",emailAddress==null?"null":emailAddress.toString());
    }
    private void checkGooglePlayServices() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);
        if(code != ConnectionResult.SUCCESS && api.isUserResolvableError(code))
                Toast.makeText(this, api.getErrorString(code), Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        this.savedData = savedInstanceState;
        setContentView(R.layout.activity_maps);

        placeList = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        // Initializing Toolbar and setting it as the actionbar
        /*
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */
        /* History retrieval*/
        history_place_id = getIntent().getStringExtra(HistoryActivity.HISTORY_EXTRA);
        if(history_place_id != null)
        {
            final List<MyPlace> his = Singleton.getInstance().historyList;
            for(final MyPlace hisId : his) {
                if(history_place_id.equals(hisId.getId())) {
                    Place place = new Place() {
                        @Override
                        public String getId() {
                            return hisId.getId();
                        }

                        @Override
                        public List<Integer> getPlaceTypes() {
                            return null;
                        }

                        @Override
                        public CharSequence getAddress() {
                            return null;
                        }

                        @Override
                        public Locale getLocale() {
                            return null;
                        }

                        @Override
                        public CharSequence getName() {
                            return hisId.getName();
                        }

                        @Override
                        public LatLng getLatLng() {
                            return new LatLng(hisId.getLat(),hisId.getLng());
                        }

                        @Override
                        public LatLngBounds getViewport() {
                            return null;
                        }

                        @Override
                        public Uri getWebsiteUri() {
                            return null;
                        }

                        @Override
                        public CharSequence getPhoneNumber() {
                            return null;
                        }

                        @Override
                        public float getRating() {
                            return (float)Double.parseDouble(hisId.getRating());
                        }

                        @Override
                        public int getPriceLevel() {
                            return 0;
                        }

                        @Override
                        public CharSequence getAttributions() {
                            return null;
                        }

                        @Override
                        public Place freeze() {
                            return null;
                        }

                        @Override
                        public boolean isDataValid() {
                            return false;
                        }
                    };
                    onPlaceSelected(place);
                    break;
                }
            }
        }
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView)findViewById(R.id.nav_view);





        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        nextButton = (Button) findViewById(R.id.recycler_view_button);
        nextButton.setText("ListView");
        nextButton.setOnClickListener(new NextButtonClickListener(this,ListActivity.class,placeList));

        historyButton = (Button) findViewById(R.id.history_button);
        historyButton.setText("History");
        historyButton.setOnClickListener(new HistoryButtonClickListener(this));

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setRetainInstance(true);

                        /* Extra retrieval from Login Activity*/
        emailAddress = getIntent().getStringExtra(LoginActivity.EMAIL_ADDRESS_EXTRA);
        if(savedInstanceState != null)
        {
            String loc = savedInstanceState.getString("Location");
            if(loc!=null && loc.equals("null"))
            {
                final Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Location Services Disabled....", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("RETRY", new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                        onMyLocationButtonClick();
                    }
                });
                snackbar.show();

            }
            emailAddress = savedInstanceState.getString("EmailAddress");
        }

        retrieveHistory();
        Menu navigationViewMenu = mNavigationView.getMenu();
        navigationViewMenu.getItem(0).setTitle(emailAddress);

        setNavViewListeners();

    }

    private void retrieveHistory()
    {
        //Singleton.historyList.clear();
        Firebase.setAndroidContext(this);
        //Firebase ref = new Firebase("https://nanochat-8050a.firebaseio.com/RealEstate");
        Query query = new Firebase("https://nanochat-8050a.firebaseio.com/history").orderByChild("email").equalTo(getEmailAddress());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MyPlace place = new MyPlace();
                double lat = (Double)dataSnapshot.child("lat").getValue();
                double lng = (Double)dataSnapshot.child("lng").getValue();
                String id = (String)dataSnapshot.child("id").getValue();
                String name = (String)dataSnapshot.child("name").getValue();
                String email = (String)dataSnapshot.child("email").getValue();
                String rating = (String)dataSnapshot.child("rating").getValue();
                place.setId(id); place.setLng(lng);place.setLat(lat);
                place.setEmail(email); place.setName(name); place.setRating(Double.parseDouble(rating));
                Singleton.getInstance().historyList.add(place);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    private void setNavViewListeners()
    {

        mDrawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Drawer layout clicked");
            }
        });
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                          @Override
                                          public boolean onNavigationItemSelected(MenuItem menuItem) {
                                              //Checking if the item is in checked state or not, if not make it in checked state
                                              if(menuItem.isChecked()) menuItem.setChecked(false);
                                              else menuItem.setChecked(true);
                                                mDrawerLayout.closeDrawers();
                                              switch (menuItem.getItemId())
                                              {
                                                  case R.id.nav_home:
                                                      Toast.makeText(getApplicationContext(),"Home Selected",Toast.LENGTH_SHORT).show();
                                                      Log.d(TAG,"Home selected");
                                                      break;
                                                  case R.id.nav_history:
                                                      Toast.makeText(getApplicationContext(),"History Selected",Toast.LENGTH_SHORT).show();
                                                      Log.d(TAG,"History selected");
                                                      break;
                                                  case R.id.nav_sign_out:
                                                      Toast.makeText(getApplicationContext(),"Sign Out Selected",Toast.LENGTH_SHORT).show();
                                                      Log.d(TAG,"Sign Out selected");
                                                      break;
                                              }
                                              return true;
                                          }
                              }
        );
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        Log.d(TAG, "")
        if (id == R.id.action_settings) {
            return true;

        return super.onOptionsItemSelected(item);
    }
*/
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setMyLocationEnabled(true);
/*
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)  ;
            mGoogleApiClient.connect();
*/
            // Add a marker in san jose and move the camera
            if(savedData==null) {
                LatLng sanJose = new LatLng(37.3299096, -121.9046956);
                mMap.addMarker(new MarkerOptions().position(sanJose).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sanJose, 13));
            }

//            handleNewLocation(location);

        } catch (SecurityException se) {
            Log.d(TAG, se.getMessage());
        }
    }

    public static final String TAG = "MapsActivity";

    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {
        try {
            Log.d("Firebase", "Id:"+place.getId());
            Log.d("Firebase", "Place Selected:" + place.getName());
            clearAll();
            // Format the returned place's details and display them in the TextView.
            LatLng selection = place.getLatLng();
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(selection).title(place.getName().toString())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selection, 13));
            /* Insertion code */
            //insertData(place);
            insertHistory(place);
            retrieveNearbyApartments(place.getLatLng().latitude,place.getLatLng().longitude);
            //addNearbyCafes(selection);
        }

        catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    private void insertHistory(Place place)
    {
        Firebase.setAndroidContext(this);
        //Firebase ref = new Firebase("https://nanochat-8050a.firebaseio.com/RealEstate");
        Firebase ref = new Firebase("https://nanochat-8050a.firebaseio.com/history");
        MyPlace selectPlace = new MyPlace();
        selectPlace.setLat(place.getLatLng().latitude);
        selectPlace.setLng(place.getLatLng().longitude);
        selectPlace.setRating(place.getRating());
        selectPlace.setId(place.getId());
        selectPlace.setName(place.getName().toString());
        selectPlace.setEmail(emailAddress);
        ref.push().setValue(selectPlace);
    }



    private void retrieveNearbyApartments(final double mylat, final double mylng)
    {
        Firebase.setAndroidContext(this);
        //Firebase ref = new Firebase("https://nanochat-8050a.firebaseio.com/RealEstate");
        Firebase ref = new Firebase("https://nanochat-8050a.firebaseio.com/placeDetails");
        Query latRef = ref.orderByChild("lat").startAt(mylat-MyRequest.LAT_OFFSET).endAt(mylat+MyRequest.LAT_OFFSET);

        latRef.addChildEventListener(new ChildEventListener() {
               @Override
               public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                   if(dataSnapshot.hasChild("lng"))
                   {
                       double lng = (Double)dataSnapshot.child("lng").getValue();
                       double lat = (Double)dataSnapshot.child("lat").getValue();
                       String placeName = (String)dataSnapshot.child("placeName").getValue();
                        if(lng<= mylng+MyRequest.LNG_OFFSET && lng>=mylng-MyRequest.LNG_OFFSET )
                        {
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng(lat,lng))
                                    .snippet(placeName)
                                    .title(placeName)
                                   // .icon(BitmapDescriptorFactory.fromResource(R.mipmap.apt))
                                    //.anchor((float)lat, (float)lng)
                                    ;
                            mMap.addMarker(markerOptions);
                            RealEstate re = getRE(dataSnapshot);

                            //to uncomment after testing
                            addNearbyPlaces(lat,lng,re);
                            Singleton.placeMap.clear();
                            Singleton.getInstance().realEstateList.add(re);
                        }
                       Log.d("Places","End of Retrive Apts");
                   }
               }

                private RealEstate getRE(DataSnapshot dataSnapshot)
                {
                    double lat = (Double)dataSnapshot.child("lat").getValue();
                    double lng = (Double)dataSnapshot.child("lng").getValue();
                    String id = (String)dataSnapshot.child("id").getValue();
                    String name = (String)dataSnapshot.child("placeName").getValue();
                    String email = (String)dataSnapshot.child("email").getValue();
                    String phoneNumber = (String)dataSnapshot.child("phoneNumber").getValue();
                    String rent = (String)dataSnapshot.child("rent").getValue();
                    String unitNumber = (String)dataSnapshot.child("unitNumber").getValue();

                    RealEstate re = new RealEstate();
                    re.setId(id);
                    re.setLat(lat);re.setLng(lng);re.setUnitNumber(unitNumber);re.setPhoneNumber(phoneNumber);
                    re.setRent(rent); re.setEmail(email); re.setName(name);
                    return re;
                }


               @Override
               public void onChildChanged(DataSnapshot dataSnapshot, String s)
               {

               }

               @Override
               public void onChildRemoved(DataSnapshot dataSnapshot) {

               }

               @Override
               public void onChildMoved(DataSnapshot dataSnapshot, String s) {

               }

               @Override
               public void onCancelled(FirebaseError firebaseError) {
                   Log.d("Firebase",firebaseError.toString());
               }
           });

    }
/*
    private void insertData(Place place)
    {
        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://nanochat-8050a.firebaseio.com/RealEstate");

        RealEstate re = new RealEstate();
        re.setId(place.getId());
        re.setLat(place.getLatLng().latitude);
        re.setLng(place.getLatLng().longitude);
        re.setName(place.getName().toString());
        re.setEmail("bala.murugan.nitt@gmail.com");
        re.setPhoneNumber("+16692437106");
        re.setRent("2654.0");
        re.setUnitNumber("212");
        ref.push().setValue(re);
    }
*/
    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    /* Request permission for error handling purposes */
    /*
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }
    */

    public void clearAll()
    {
        Singleton.realEstateList.clear();
        Singleton.placeMap.clear();
        Singleton.placeList.clear();
    }
    @Override
    public boolean onMyLocationButtonClick() {
        try {
            clearAll();
            Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
            // Return false so that we don't consume the event and the default behavior still occurs
            // (the camera animates to the user's current position).

            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            handleNewLocation(location);
            if(location!=null) {
                LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
                placeList = new ArrayList<>();
                //addNearbyCafes(myLoc);
                retrieveNearbyApartments(location.getLatitude(),location.getLongitude());
            }

        } catch (SecurityException se) {
            Log.d(LOC_TAG,"Security exception in my location button click");
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(REQ_TAG);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOC_TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            Log.d(LOC_TAG, location.toString());
        }
    }
    private Location location;
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOC_TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(LOC_TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }
    @Override
    protected  void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)  ;
        checkGooglePlayServices();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    private void handleNewLocation(Location location) {
        if(location == null)
        {
            final Snackbar snackbar = Snackbar
                     .make(coordinatorLayout, "Location Services Disabled....", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RETRY", new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                    onMyLocationButtonClick();
                }
            });
            snackbar.show();
        }
        else {
            Log.d(LOC_TAG, location.toString());

            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();
            LatLng latLng = new LatLng(currentLatitude, currentLongitude);

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title("You Are Here..")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.clear();
            mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        }
    }

    private void addNearbyCafes(LatLng selection) {
        try {
            if (mRequestQueue == null)
                mRequestQueue = Volley.newRequestQueue(this);
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + selection.latitude + "%2C"
                    + selection.longitude
                    + "&radius=" + MyRequest.RADIUS
                    + "&type=" + MyRequest.TYPE
                    //+ "&rankby=" + MyRequest.RANKBY
                    + "&key=" + MyRequest.KEY;

            stringRequest = new StringRequest(Request.Method.GET
                    , url
                    , new NearbyResponseListener<String>(mMap, placeList)
                    , new NearbyErrorListener(coordinatorLayout));
            stringRequest.setTag(REQ_TAG);
            mRequestQueue.add(stringRequest);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

    }

    private void addNearbyPlaces(double lat , double lng, RealEstate re)
    {
        Singleton.getInstance().placeMap.clear();
        String[] requests= {MyRequest.AIRPORT,MyRequest.ATM,MyRequest.BUS_STATION
    ,MyRequest.CAFE,MyRequest.HOSPITAL, MyRequest.TRAIN_STATION
    };
        for(String req:requests)
        {
            addNearbyPlacesByType(lat,lng,re,req);
        }

    }

    private void addNearbyPlacesByType(double lat, double lng, RealEstate re,String type)
    {
        try{
            if(mRequestQueue == null)
                mRequestQueue = Volley.newRequestQueue(this);
            PlacesResponseListener<String> placesResponseListener = new PlacesResponseListener<>();
            placesResponseListener.setRequest_type(type);
            placesResponseListener.setRealEstate(re);
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + lat + "%2C"
                    + lng
                    + "&radius=" + MyRequest.RADIUS
                    + "&type=" + type
                    + "&key=" + MyRequest.KEY;
            // Request a string response from the provided URL.
            stringRequest = new StringRequest(Request.Method.GET
                    , url
                    , placesResponseListener
                    , new PlacesErrorListener());
            stringRequest.setTag(REQ_TAG);
            mRequestQueue.add(stringRequest);
        }
        catch (Exception ex) {
            Log.d(TAG,ex.getMessage());
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
