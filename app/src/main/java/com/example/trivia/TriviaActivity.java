//Assignment Inclass 07
//File Name: Group12_InClass07
//Sanika Pol
//Snehal Kekane
package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TriviaActivity extends AppCompatActivity implements OptionsAdapter.InteractWithTriviaActivity {



    private static final String TAG = "demo";
    public static final String SCORE_KEY = "SCORE";
    ArrayList<QuizItem> quizItems = new ArrayList<QuizItem>();
    TextView tv_Question,tv_id,tv_timer;
    ImageView iv_Image;
    Button btn_Next,btn_Quit;
    int questionNo = 0;
    int score;

    ProgressDialog progressDialog;


    private RecyclerView recyclerView;
    private  RecyclerView.Adapter mAdapter = null;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void getAnswer(int position) {
        if(position + 1 == quizItems.get(questionNo).answer){
            Log.d(TAG,"answered correctly");
            quizItems.get(questionNo).correct = true;
        }
        else {
            quizItems.get(questionNo).correct = false;
        }
    }

    String baseURL = "http://dev.theappsdr.com/apis/trivia_json/index.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        tv_Question = findViewById(R.id.tv_Question);
        tv_id = findViewById(R.id.tv_questionNo);
        iv_Image = findViewById(R.id.img_quesImage);
        btn_Next = findViewById(R.id.btn_next);
        btn_Quit = findViewById(R.id.btn_quit);
        tv_timer = findViewById(R.id.tv_Timer);


        if (isConnected()) {

            new GetQuestions().execute(baseURL);
            new CountDownTimer(120000, 1000) { // adjust the milli seconds here

                public void onTick(long millisUntilFinished) {
                    tv_timer.setText(""+String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }

                public void onFinish() {
                    tv_timer.setText("done!");
                    score = calculateScore();
                    Log.d(TAG,"score b4 final: " + score);
                    double finalScore = (double) score/quizItems.size();
                    finalScore = finalScore * 100;
                    Log.d(TAG,"% " + finalScore);
                    Intent statActivity = new Intent(TriviaActivity.this, StatActivity.class);
                    statActivity.putExtra(SCORE_KEY,(int) finalScore);
                    startActivity(statActivity);
                }
            }.start();

            btn_Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(questionNo == quizItems.size()-1){
                        score = calculateScore();
                        Log.d(TAG,"score b4 final: " + score);
                        double finalScore = (double) score/quizItems.size();
                        finalScore = finalScore * 100;
                        Log.d(TAG,"% " + finalScore);
                        Intent statActivity = new Intent(TriviaActivity.this, StatActivity.class);
                        statActivity.putExtra(SCORE_KEY,(int) finalScore);
                        startActivity(statActivity);
                    }else{
                        questionNo++;
                        Log.d(TAG,"quizItem: " + quizItems.get(questionNo).toString());
                        tv_Question.setText(quizItems.get(questionNo).question);
                        int id = quizItems.get(questionNo).id + 1;
                        tv_id.setText("Q" + id);
                        if(quizItems.get(0).imgURL!=null)
                            Picasso.get().load(quizItems.get(questionNo).imgURL).fit().into(iv_Image);
                        mAdapter = new OptionsAdapter(quizItems.get(questionNo).choices,TriviaActivity.this);
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }
                }
            });

            btn_Quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        else {
            Log.d(TAG,"Not connected");
            Toast.makeText(TriviaActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    private int calculateScore(){
        int count =0 ;
        for(QuizItem q:quizItems){
            Log.d(TAG,"correct = " + q.correct);
            if(q.correct == true){
                count++;
            }
        }
        Log.d(TAG,"score: " + count);
        return count;
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !((NetworkInfo) networkInfo).isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    class GetQuestions extends AsyncTask<String,Void,ArrayList<QuizItem>> {
        @Override
        protected ArrayList<QuizItem> doInBackground(String... strings) {
            HttpURLConnection connection = null;

            try{
                String url =strings[0];
                Log.d(TAG,"URL = " + url);

                URL urlB = new URL(url);

                connection = (HttpURLConnection) urlB.openConnection();
                connection.connect();


                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK) {
                    Log.d(TAG,"status ok");
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray questions = root.getJSONArray("questions");
                    Log.d(TAG,"questions size: " + questions.length());
                    for (int i = 0; i < questions.length(); i++) {
                        JSONObject quizQuestion = questions.getJSONObject(i);
                        QuizItem quizItem = new QuizItem();
                        quizItem.id = quizQuestion.getInt("id");
                        quizItem.question = quizQuestion.getString("text");
                        Log.d(TAG ,"Image URL: " + quizQuestion.getString("image"));
                        if(quizQuestion.getString("image")!=null)
                            quizItem.imgURL = quizQuestion.getString("image");
                        JSONObject choices = quizQuestion.getJSONObject("choices");
                        JSONArray choiceArray = choices.getJSONArray("choice");
                        ArrayList<String> options = new ArrayList<String>();
                        for(int j=0;j<choiceArray.length();j++){
                            options.add(choiceArray.getString(j));
                        }
                        quizItem.choices = options;
                        quizItem.answer = choices.getInt("answer");
                        quizItems.add(quizItem);
                    }

                }
                else {
                    Log.d(TAG, "Status not ok");
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return quizItems;
        }

        @Override
        protected void onPostExecute(ArrayList<QuizItem> quizItems) {
            tv_Question = findViewById(R.id.tv_Question);
            tv_id = findViewById(R.id.tv_questionNo);
            iv_Image = findViewById(R.id.img_quesImage);
            super.onPostExecute(quizItems);
            Log.d(TAG, "size: " + quizItems.size() + "");
            if(quizItems.size() > 0){
                Log.d(TAG,"quizItem: " + quizItems.get(0).toString());
                tv_Question.setText(quizItems.get(0).question);
                int id = quizItems.get(0).id + 1;
                tv_id.setText("Q" + id);
                Log.d(TAG,"Image url: " + quizItems.get(0).imgURL);
                if(quizItems.get(0).imgURL!=null)
                    Picasso.get().load(quizItems.get(0).imgURL).fit().into(iv_Image);
                mAdapter = new OptionsAdapter(quizItems.get(0).choices,TriviaActivity.this);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }
        }

    }
}
