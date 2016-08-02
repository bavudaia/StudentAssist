package com.example.bala.studentassist;

import android.content.Intent;
import android.view.View;

/**
 * Created by bala on 8/1/16.
 */
public class HistoryButtonClickListener implements View.OnClickListener{
    private MapsActivity context;

    public HistoryButtonClickListener(MapsActivity context)
    {
        this.context = context;
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context,HistoryActivity.class);
        context.startActivity(intent);
    }

}
