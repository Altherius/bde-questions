package com.bde_supgalilee.bde_questions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Altherius
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<Question> questions;
    private TextView loadingField;
    private Button offlineModeButton;


    /**
     *
     * @param savedInstanceState The instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        offlineModeButton = findViewById(R.id.activity_main_offline);

        loadingField = findViewById(R.id.activity_main_loading);
        loadingField.setText(R.string.connection_placeholder);

        offlineModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                Gson gson = new Gson();

                String questionsListJson = preferences.getString("questions_list", "");
                Type type = new TypeToken<ArrayList<Question>>() {}.getType();
                questions = gson.fromJson(questionsListJson, type);

                Intent questionsActivity = new Intent(MainActivity.this, QuestionsActivity.class);
                questionsActivity.putParcelableArrayListExtra("questions", getQuestions());
                startActivity(questionsActivity);

                finish();
            }
        });

        Thread loadingThread = new Thread() {

            public void run() {
                try {
                    super.run();

                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = null;
                    if (cm != null) {
                        netInfo = cm.getActiveNetworkInfo();
                    } else {
                        Log.e("ERROR", "Impossible to get network information.");
                    }


                    while (netInfo == null || !(netInfo.isConnected())) {
                        loadingField.setText(R.string.check_connection);
                        offlineModeButton.setVisibility(View.VISIBLE);
                        sleep(1000);
                        netInfo = cm.getActiveNetworkInfo();
                    }
                    loadingField.setText(R.string.connection_placeholder);
                    if (offlineModeButton.isShown()) {
                        offlineModeButton.setVisibility(View.INVISIBLE);
                    }
                    loadingField.setText(R.string.treating_data);
                    loadQuestions();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        loadingThread.start();

    }


    /**
     * Loads the question from the BDESG API and stores them in an ArratList. Then
     * changes to the game activity.
     */
    private void loadQuestions() {
        RetrieveChecksumTask checksumTask = new RetrieveChecksumTask(this);
        RetrieveQuestionsTask questionsTask = new RetrieveQuestionsTask(this);

        try {
            String checksum = checksumTask.execute().get();




            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            /* Get current last known checksum */
            String memoryChecksum = preferences.getString("questions_checksum", "");
            SharedPreferences.Editor editor = preferences.edit();

            ArrayList<Question> questions;
            Gson gson = new Gson();

            /* Saving Checksum in preferences and getting questions from API if there is a difference
            * the questions are also saved to memory, overwriting any previous set of questions */
            if (!memoryChecksum.equals(checksum)) {
                questions = questionsTask.execute().get();
                String questionsListJson = gson.toJson(questions);
                editor.putString("questions_list", questionsListJson);

                editor.putString("questions_checksum", checksum);
                editor.putString("last_checksum_update", new java.util.Date().toLocaleString());
                editor.apply();
            }

            /* Get questions from memory if checksums are the same */
            else {
                String questionsListJson = preferences.getString("questions_list", "");
                Type type = new TypeToken<ArrayList<Question>>() {}.getType();
                questions = gson.fromJson(questionsListJson, type);
            }


            setQuestions(questions);


        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent questionsActivity = new Intent(this, QuestionsActivity.class);
        questionsActivity.putParcelableArrayListExtra("questions", getQuestions());
        startActivity(questionsActivity);

        finish();
    }

    /**
     * Getter for the questions array list.
     *
     * @return The questions array list.
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * Setter for the questions array list.
     *
     * @param questions The new questions array list.
     */
    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
