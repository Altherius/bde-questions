package com.bde_supgalilee.bde_questions;

import android.content.Intent;
import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingField = findViewById(R.id.activity_main_loading);
        loadingField.setText(R.string.connection_placeholder);

        loadQuestions();
    }


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

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
