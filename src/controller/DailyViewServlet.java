// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   DailyViewServlet.java

package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CalendarLogic;

@WebServlet("/DailyViewServlet")
public class DailyViewServlet extends HttpServlet
{

    public DailyViewServlet()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        String dating = request.getParameter("action");
        String year = dating.substring(0, 4);
        String month = dating.substring(5, 7);
        String day = dating.substring(8, 10);
        month = String.valueOf(Integer.parseInt(month) + 1);
        month = month.length() != 2 ? (new StringBuilder(String.valueOf(String.valueOf(0)))).append(month).toString() : month;
        day = day.length() != 2 ? (new StringBuilder(String.valueOf(String.valueOf(0)))).append(day).toString() : day;
        dating = (new StringBuilder(String.valueOf(year))).append(month).append(day).toString();
        String dailyTable = CalendarLogic.createDaily(dating);
        request.setAttribute("dailyTable", dailyTable);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/daily.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }

    private static final long serialVersionUID = 1L;
}
