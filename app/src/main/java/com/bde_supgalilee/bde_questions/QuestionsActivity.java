package com.bde_supgalilee.bde_questions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class QuestionsActivity extends AppCompatActivity {

    private static final int EASY = 1;
    private static final int NORMAL = 2;
    private static final int HARD = 3;

    private TextView questionField;
    private TextView answerField;
    private ArrayList<Question> easyQuestions;
    private ArrayList<Question> normalQuestions;
    private ArrayList<Question> hardQuestions;
    private Random randomizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Button easyButton = findViewById(R.id.activity_questions_easy);
        Button normalButton = findViewById(R.id.activity_questions_normal);
        Button hardButton = findViewById(R.id.activity_questions_hard);
        Button randomButton = findViewById(R.id.activity_questions_random);

        questionField = findViewById(R.id.activity_questions_question);
        answerField = findViewById(R.id.activity_questions_answer);

        ArrayList<Question> questions = getIntent().getParcelableArrayListExtra("questions");
        easyQuestions = new ArrayList<>();
        normalQuestions = new ArrayList<>();
        hardQuestions = new ArrayList<>();

        randomizer = new Random();

        if (null != questions) {
            for (Question question : questions) {
                switch (question.getDifficulty()) {
                    case EASY:
                        easyQuestions.add(question);
                        break;
                    case NORMAL:
                        normalQuestions.add(question);
                        break;
                    case HARD:
                        hardQuestions.add(question);
                        break;
                    default:
                        normalQuestions.add(question);
                }
            }
        }


        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Question q = getRandomQuestion(EASY);
                    questionField.setText(q.getQuestion());
                    answerField.setText(q.getAnswer());
                } catch (IllegalArgumentException e) {
                    questionField.setText(e.getMessage());
                    answerField.setText("");
                }

            }
        });

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Question q = getRandomQuestion(NORMAL);
                    questionField.setText(q.getQuestion());
                    answerField.setText(q.getAnswer());
                } catch (IllegalArgumentException e) {
                    questionField.setText(e.getMessage());
                    answerField.setText("");
                }

            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Question q = getRandomQuestion(HARD);
                    questionField.setText(q.getQuestion());
                    answerField.setText(q.getAnswer());
                } catch (IllegalArgumentException e) {
                    questionField.setText(e.getMessage());
                    answerField.setText("");
                }

            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Question q = getRandomQuestion(randomizer.nextInt(HARD+1));
                    questionField.setText(q.getQuestion());
                    answerField.setText(q.getAnswer());
                } catch (IllegalArgumentException e) {
                    questionField.setText(e.getMessage());
                    answerField.setText("");
                }

            }
        });
    }

    /**
     * Gets a random question according to a specific difficulty.
     *
     * @param difficulty The difficulty level of the question.
     * @return A question with the chosen difficulty.
     */
    private Question getRandomQuestion(int difficulty) {
        switch (difficulty) {
            case EASY:
                return getRandomQuestion(easyQuestions);
            case NORMAL:
                return getRandomQuestion(normalQuestions);
            case HARD:
                return getRandomQuestion(hardQuestions);
            default:
                if (BuildConfig.DEBUG) {
                    Log.e("illegal_difficulty", "Called illegal difficulty, assuming normal difficulty.");
                }
                return getRandomQuestion(normalQuestions);
        }
    }

    /**
     * Gets a random question from a specific list of questions.
     *
     * @param questions The list of questions.
     * @return A random question from the list.
     * @throws IllegalArgumentException If there is no question in the list.
     */
    private Question getRandomQuestion(ArrayList<Question> questions) throws IllegalArgumentException {

        if (questions.size() == 0) {
            throw new IllegalArgumentException("La liste de questions est vide pour cette difficulté.");
        }

        Random randomizer = new Random();
        Question q;

        do  {
            int index = randomizer.nextInt(questions.size());
            q = questions.get(index);
        } while (q.getQuestion().contentEquals(questionField.getText()));

        return q;
    }

    public void onBackPressed() {
        /* We don't want to go back to MainActivity with back button */
        moveTaskToBack(true);
    }
}
