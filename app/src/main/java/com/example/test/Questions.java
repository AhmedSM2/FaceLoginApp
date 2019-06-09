package com.example.test;

public class Questions
{
    public String question_uId;
    public String question_uFullName;
    public String question_time;
    public String question_date;
    public String question_description;
    public String question_uPP;

    public Questions()
    {

    }

    public Questions(String question_uId, String question_uFullName, String question_time, String question_date, String question_description, String question_uPP)
    {
        this.question_uId = question_uId;
        this.question_uFullName = question_uFullName;
        this.question_time = question_time;
        this.question_date = question_date;
        this.question_description = question_description;
        this.question_uPP = question_uPP;
    }

    public Questions(String question_uFullName, String question_time, String question_date, String question_description, String question_uPP)
    {
        this.question_uFullName = question_uFullName;
        this.question_time = question_time;
        this.question_date = question_date;
        this.question_description = question_description;
        this.question_uPP = question_uPP;
    }

    public String getQuestion_uId() {
        return question_uId;
    }

    public void setQuestion_uId(String question_uId) {
        this.question_uId = question_uId;
    }

    public String getQuestion_uFullName() {
        return question_uFullName;
    }

    public void setQuestion_uFullName(String question_uFullName) {
        this.question_uFullName = question_uFullName;
    }

    public String getQuestion_time() {
        return question_time;
    }

    public void setQuestion_time(String question_time) {
        this.question_time = question_time;
    }

    public String getQuestion_date() {
        return question_date;
    }

    public void setQuestion_date(String question_date) {
        this.question_date = question_date;
    }

    public String getQuestion_description() {
        return question_description;
    }

    public void setQuestion_description(String question_description) {
        this.question_description = question_description;
    }

    public String getQuestion_uPP() {
        return question_uPP;
    }

    public void setQuestion_uPP(String question_uPP) {
        this.question_uPP = question_uPP;
    }
}
