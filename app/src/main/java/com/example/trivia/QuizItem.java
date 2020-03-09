//Assignment Inclass 07
//File Name: Group12_InClass07
//Sanika Pol
//Snehal Kekane
package com.example.trivia;

import java.util.ArrayList;

public class QuizItem {
    int id;
    String question,imgURL;
    ArrayList<String> choices;
    int answer;
    boolean correct;

    public QuizItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "QuizItem{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", imgURL='" + imgURL + '\'' +
                ", choices=" + choices +
                ", answer=" + answer +
                '}';
    }
}
