package com.bde_supgalilee.bde_questions;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Altherius
 * @version 1.0
 */
public class RetrieveChecksumTask extends AsyncTask<Void, Void, String> {

    private MainActivity myActivity;
    RetrieveChecksumTask(MainActivity a) {
        myActivity = a;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void... voids) {
        String API_URL = "http://questions.bde-sup-galilee.fr/api/questions/checksum";

        try {
            /* Seeks data from the API */
            URL url = new URL(API_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                /* Reads data into a buffered reader */
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
}
