package com.example.test;

public class QuestionComments
{
    public String question_comment_uId;
    public String question_comment_uFullName;
    public String question_comment_time;
    public String question_comment_date;
    public String question_comments_description;
    public String question_comment_uPP;

    public QuestionComments()
    {

    }

    public QuestionComments(String question_comment_uFullName, String question_comment_time, String question_comment_date, String question_comments_description, String question_comment_uPP)
    {
        this.question_comment_uFullName = question_comment_uFullName;
        this.question_comment_time = question_comment_time;
        this.question_comment_date = question_comment_date;
        this.question_comments_description = question_comments_description;
        this.question_comment_uPP = question_comment_uPP;
    }

    public QuestionComments(String question_comment_uId, String question_comment_uFullName, String question_comment_time, String question_comment_date, String question_comments_description, String question_comment_uPP)
    {
        this.question_comment_uId = question_comment_uId;
        this.question_comment_uFullName = question_comment_uFullName;
        this.question_comment_time = question_comment_time;
        this.question_comment_date = question_comment_date;
        this.question_comments_description = question_comments_description;
        this.question_comment_uPP = question_comment_uPP;
    }

    public String getQuestion_comment_uId()
    {
        return question_comment_uId;
    }

    public void setQuestion_comment_uId(String question_comment_uId)
    {
        this.question_comment_uId = question_comment_uId;
    }

    public String getQuestion_comment_uFullName()
    {
        return question_comment_uFullName;
    }

    public void setQuestion_comment_uFullName(String question_comment_uFullName)
    {
        this.question_comment_uFullName = question_comment_uFullName;
    }

    public String getQuestion_comment_time()
    {
        return question_comment_time;
    }

    public void setQuestion_comment_time(String question_comment_time)
    {
        this.question_comment_time = question_comment_time;
    }

    public String getQuestion_comment_date()
    {
        return question_comment_date;
    }

    public void setQuestion_comment_date(String question_comment_date)
    {
        this.question_comment_date = question_comment_date;
    }

    public String getQuestion_comments_description()
    {
        return question_comments_description;
    }

    public void setQuestion_comments_description(String question_comments_description)
    {
        this.question_comments_description = question_comments_description;
    }

    public String getQuestion_comment_uPP()
    {
        return question_comment_uPP;
    }

    public void setQuestion_comment_uPP(String question_comment_uPP)
    {
        this.question_comment_uPP = question_comment_uPP;
    }
}
