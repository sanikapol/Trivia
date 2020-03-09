//Assignment Inclass 07
//File Name: Group12_InClass07
//Sanika Pol
//Snehal Kekane
package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StatActivity extends AppCompatActivity {

    Button btn_Try,btn_quit;
    ProgressBar progress;
    int score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        btn_Try = findViewById(R.id.btn_TryAgain);
        btn_quit = findViewById(R.id.btn_QuitFinal);



        TextView tv_finalScore = (TextView) findViewById(R.id.tv_progressPercent);
        if(getIntent()!=null && getIntent().getExtras()!=null){
            score = getIntent().getExtras().getInt(TriviaActivity.SCORE_KEY);
            Log.d("demo","Score: " + score);
            tv_finalScore.setText(score + "%");
        }

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setProgress(score);

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(StatActivity.this, MainActivity.class);
                startActivity(mainActivity);

            }
        });

        btn_Try.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent triviaActivity = new Intent(StatActivity.this, TriviaActivity.class);
                startActivity(triviaActivity);
            }
        });

    }
}
