package com.yinghuizou.placesnearme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nome_page);
    }

    public void entermap(View view){


        Intent UserPage= new Intent(NomePage.this,MapsActivity.class);
        startActivity(UserPage);
    }
}
