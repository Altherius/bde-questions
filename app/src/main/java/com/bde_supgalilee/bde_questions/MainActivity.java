package com.bde_supgalilee.bde_questions;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * @author Altherius
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<Question> questions;
    private TextView loadingField;
    private Handler handler;


    /**
     *
     * @param savedInstanceState The instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingField = findViewById(R.id.activity_main_loading);
        loadingField.setText(R.string.connection_placeholder);

        Thread loadingThread = new Thread() {

            public void run() {
                try {
                    super.run();

                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();

                    while (netInfo == null || !(netInfo.isConnected())) {
                        loadingField.setText("Vérifiez votre connexion");
                        sleep(1000);
                        netInfo = cm.getActiveNetworkInfo();
                    }
                    loadingField.setText("Connexion au serveur");

                    loadingField.setText("Traitement des questions");
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

        RetrieveQuestionsTask asynctask = new RetrieveQuestionsTask(this);
        try {
            ArrayList<Question> questions = asynctask.execute().get();

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

    public TextView getLoadingField() {
        return this.loadingField;
    }
}
