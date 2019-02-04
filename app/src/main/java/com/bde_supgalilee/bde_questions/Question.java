package com.bde_supgalilee.bde_questions;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A simple POJO describing a question with an answer or a difficulty, available for any quizz app.
 *
 * @author Altherius
 * @version 1.0
 */
public class Question implements Parcelable {

    private String question;
    private String answer;
    private int difficulty;

    /**
     * Builds a question from a question text and an answer text.
     *
     * @param question The question text.
     * @param answer The answer text.
     */
    public Question(String question, String answer) {

            this.question = question;
            this.answer = answer;
            this.difficulty = 2;

    }

    /**
     * Builds a question from a question text, an answer text, and a symbolic
     * difficulty level.
     *
     * @param question The question text.
     * @param answer The answer text.
     * @param difficulty The difficulty level.
     */
    public Question(String question, String answer, int difficulty) {

        this.question = question;
        this.answer = answer;
        this.difficulty = difficulty;

    }


    /**
     * Builds the question in the order of the full constructor.
     *
     * @param in The Parcel input.
     */
    protected Question(Parcel in) {
        question = in.readString();
        answer = in.readString();
        difficulty = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    /**
     * Getter for the question text.
     *
     * @return The question text.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Getter for the answer text.
     *
     * @return The answer text.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Getter for the difficulty level.
     *
     * @return The difficulty level.
     */
    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getQuestion());
        dest.writeString(getAnswer());
        dest.writeInt(getDifficulty());
    }
}
