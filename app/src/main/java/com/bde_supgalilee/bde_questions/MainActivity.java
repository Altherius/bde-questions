package com.bde_supgalilee.bde_questions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
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


                    loadingField.setText("Comparaison de la somme de contrôle");

                    loadingField.setText("Traitement des données");
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

            ArrayList<Question> questions = null;
            Gson gson = new Gson();

            /* Saving Checksum in preferences and getting questions from API if there is a difference
            * the questions are also saved to memory, overwriting any previous set of questions */
            if (!memoryChecksum.equals(checksum)) {
                questions = questionsTask.execute().get();
                String questionsListJson = gson.toJson(questions);
                editor.putString("questions_list", questionsListJson);

                editor.putString("questions_checksum", checksum);
                editor.commit();
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

    public TextView getLoadingField() {
        return this.loadingField;
    }
}
