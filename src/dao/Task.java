// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Task.java

package dao;


public class Task
{

    public Task()
    {
    }

    public Task(String taskID, String year, String month, String date, String userID, String title, String startTime, 
            String endTime, String isPublic, String color, String content)
    {
        setTaskID(taskID);
        setYear(year);
        setMonth(month);
        setDate(date);
        setUserID(userID);
        setTitle(title);
        setStartTime(startTime);
        setEndTime(endTime);
        setPublic(isPublic);
        setColor(color);
        setContent(content);
    }

    public Task(String year, String month, String date, String userID, String title, String startTime, String endTime, 
            String isPublic, String color, String content)
    {
        setYear(year);
        setMonth(month);
        setDate(date);
        setUserID(userID);
        setTitle(title);
        setStartTime(startTime);
        setEndTime(endTime);
        setPublic(isPublic);
        setColor(color);
        setContent(content);
    }

    public String getTaskID()
    {
        return taskID;
    }

    public void setTaskID(String taskID)
    {
        this.taskID = taskID;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getMonth()
    {
        return month;
    }

    public void setMonth(String month)
    {
        this.month = month;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public boolean isPublic()
    {
        return isPublic;
    }

    public void setPublic(String isPublic)
    {
        if(isPublic.equalsIgnoreCase("TRUE"))
            this.isPublic = true;
        else
            this.isPublic = false;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    private String taskID;
    private String year;
    private String month;
    private String date;
    private String userID;
    private String title;
    private String startTime;
    private String endTime;
    private boolean isPublic;
    private String color;
    private String content;
}
