package com.example.simplevideo;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AddActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    public void onBtnTapped(View view){
        if(view.getId() == R.id.btn_save){
            // insert data into the db with content provider
            ContentValues values = new ContentValues();
            values.put(SimpleVideo.TITLE, ((TextView)findViewById(R.id.text_title)).getText().toString());
            values.put(SimpleVideo.DESCRIPTION, ((TextView)findViewById(R.id.text_desc)).getText().toString());
            values.put(SimpleVideo.URI, ((TextView)findViewById(R.id.text_uri)).getText().toString());
            getContentResolver().insert(Uri.parse(SimpleVideo.CONTENT_URI+"/1"), values);
        }
        finish();
    }
    
}
