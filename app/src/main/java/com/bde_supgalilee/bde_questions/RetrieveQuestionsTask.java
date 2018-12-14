package com.bde_supgalilee.bde_questions;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RetrieveQuestionsTask extends AsyncTask<Void, Void, ArrayList<Question>> {

    private MainActivity myActivity;
    RetrieveQuestionsTask(MainActivity a) {
        myActivity = a;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ArrayList<Question> doInBackground(Void... voids) {
        String API_URL = "http://questions.bde-sup-galilee.fr/api/questions";

        try {
            URL url = new URL(API_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                JSONArray json = new JSONArray(stringBuilder.toString());
                ArrayList<Question> questions = new ArrayList<>();

                for (int i = 0 ; i < json.length() ; i++ ) {
                    JSONObject jsonObject = json.getJSONObject(i);
                    String question = jsonObject.optString("question");
                    String answer = jsonObject.optString("answer");
                    int difficulty = jsonObject.optInt("difficulty");

                    Question q = new Question(question, answer, difficulty);
                    questions.add(q);
                }

                return questions;
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Question> response) {

    }
}
