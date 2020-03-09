//Assignment Inclass 07
//File Name: Group12_InClass07
//Sanika Pol
//Snehal Kekane
package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {


    Button btn_Start,btn_Exit;
    ImageView iv_Ttivia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_Ttivia = findViewById(R.id.imageView);
        iv_Ttivia.setImageDrawable(getDrawable(R.drawable.trivia));

        btn_Start = findViewById(R.id.btn_StartTrivia);
        btn_Exit = findViewById(R.id.btn_Exit);

            btn_Start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent triviaActivity = new Intent(MainActivity.this, TriviaActivity.class);
                    startActivity(triviaActivity);

                }
            });

            btn_Exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });



    }
}
