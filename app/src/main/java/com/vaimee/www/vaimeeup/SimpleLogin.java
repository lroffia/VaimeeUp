package com.vaimee.www.vaimeeup;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class SimpleLogin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayList<String> users = new ArrayList<>();
    public static ArrayAdapter<String> usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_login);

        //users.add("Luca");
        //users.add("Carlo");

        //usersAdapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_list_item_1, users);
        //Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(usersAdapter);
        //spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Snackbar.make(findViewById(R.id.loginLayout),"Position: "+position+" Id: "+id,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onLogin(View view) {

    }
}
