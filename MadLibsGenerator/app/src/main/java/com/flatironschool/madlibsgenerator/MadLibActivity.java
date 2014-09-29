package com.flatironschool.madlibsgenerator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.flatironschool.madlibsgenerator.Models.MadLib;
import com.flatironschool.madlibsgenerator.services.MadLibService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;

public class MadLibActivity extends Activity {

    private MadLibService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mad_lib);

        mService = new MadLibService();

        final Button button = (Button) findViewById(R.id.button);
        final EditText place1 = (EditText) findViewById(R.id.place1);
        final EditText verb1 = (EditText) findViewById(R.id.verb1);
        final EditText verb2 = (EditText) findViewById(R.id.verb2);
        final EditText person1 = (EditText) findViewById(R.id.person1);
        final EditText person2 = (EditText) findViewById(R.id.person2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(MadLibActivity.this);
                dialog.setMessage("Loading....");

                dialog.show();

                List<String>places = new ArrayList<String>();
                places.add(place1.getText().toString());

                List<String>people = new ArrayList<String>();
                people.add(person1.getText().toString());
                people.add(person2.getText().toString());

                List<String>verbs = new ArrayList<String>();
                verbs.add(verb1.getText().toString());
                verbs.add(verb2.getText().toString());

                final MadLib madLib = new MadLib(places, people, verbs);

                mService.loadStory(madLib, new Callback<String>() {
                    @Override
                    public void success(String object, retrofit.client.Response response) {
                        Log.d(getLocalClassName(), response.toString());
                        dialog.dismiss();
                        Intent intent = new Intent(MadLibActivity.this, MadLibStoryActivity.class);
                        intent.putExtra("story", object);

                        startActivity(intent);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(getLocalClassName(), error.getLocalizedMessage());

                        dialog.hide();
                    }
                });
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
       mService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mad_lib, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
