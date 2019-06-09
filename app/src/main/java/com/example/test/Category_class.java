package com.example.test;

public class Category_class
{
    private String Category_name ;

    private int Category_rate , Category_type ;

    private double Category_latitude , Category_longitude;

    public Category_class()
    {

    }

    public Category_class(String category_name, int category_rate, int category_type, double category_latitude, double category_longitude)
    {
        Category_name = category_name;
        Category_rate = category_rate;
        Category_type = category_type;
        Category_latitude = category_latitude;
        Category_longitude = category_longitude;
    }

    public String getCategory_name()
    {
        return Category_name;
    }

    public void setCategory_name(String category_name)
    {
        Category_name = category_name;
    }

    public int getCategory_rate()
    {
        return Category_rate;
    }

    public void setCategory_rate(int category_rate)
    {
        Category_rate = category_rate;
    }

    public int getCategory_type()
    {
        return Category_type;
    }

    public void setCategory_type(int category_type)
    {
        Category_type = category_type;
    }

    public double getCategory_latitude()
    {
        return Category_latitude;
    }

    public void setCategory_latitude(double category_latitude)
    {
        Category_latitude = category_latitude;
    }

    public double getCategory_longitude()
    {
        return Category_longitude;
    }

    public void setCategory_longitude(double category_longitude)
    {
        Category_longitude = category_longitude;
    }
}
