// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   TimeCardServlet.java

package controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.Account;
import dao.TimeCardDAO;
import model.TimeCardLogic;
@WebServlet("/TimeCardServlet")
public class TimeCardServlet extends HttpServlet
{

    public TimeCardServlet()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account user = (Account)session.getAttribute("user");
        String modify = request.getParameter("action");
        modify = modify == null ? "" : modify;
        if(modify.equals("modify")){
            String timecardID = request.getParameter("timecardID");
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            String date = request.getParameter("date");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(date), 0, 0);
            if(!timecardID.equals(""))
            {
                dao.TimeCard timeCard = TimeCardDAO.findTimeCardByTimeCardID(user.getUserID(), timecardID);
                request.setAttribute("timecard", timeCard);
            }
            request.setAttribute("date", calendar.getTime());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/timecardModify.jsp");
            dispatcher.forward(request, response);
        }
        String action = request.getParameter("date");
        Date date = new Date();
        if(action == null)
            date = new Date();
        else
        if(action != null)
            date = new Date(Long.parseLong(action));
        String timeStamp = request.getParameter("action");
        timeStamp = timeStamp == null ? "" : timeStamp;
        if(timeStamp.equals("arrival"))
        {
            TimeCardLogic.registerTime(null, user.getUserID(), timeStamp);
            date = new Date();
        } else
        if(timeStamp.equals("goOut"))
        {
            TimeCardLogic.registerTime(null, user.getUserID(), timeStamp);
            date = new Date();
        } else
        if(timeStamp.equals("goBack"))
        {
            TimeCardLogic.registerTime(null, user.getUserID(), timeStamp);
            date = new Date();
        } else
        if(timeStamp.equals("leave"))
        {
            TimeCardLogic.registerTime(null, user.getUserID(), timeStamp);
            date = new Date();
        }
        String timeCardHTML = TimeCardLogic.createTimeCardTable(user.getUserID(), date);
        request.setAttribute("timeCard", timeCardHTML);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/timecard.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }

    private static final long serialVersionUID = 1L;
}
