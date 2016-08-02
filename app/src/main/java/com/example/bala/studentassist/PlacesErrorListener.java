package com.example.bala.studentassist;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by bala on 7/29/16.
 */
public class PlacesErrorListener  implements Response.ErrorListener{
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Places",error.toString());
    }
}
