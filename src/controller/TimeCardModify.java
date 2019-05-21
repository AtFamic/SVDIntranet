// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   TimeCardModify.java

package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.Account;
import dao.TimeCard;
import dao.TimeCardDAO;
import model.TimeCardLogic;
@WebServlet("/TimeCardModify")
public class TimeCardModify extends HttpServlet
{

    public TimeCardModify()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account user = (Account)session.getAttribute("user");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String date = request.getParameter("date");
        String timecardid = request.getParameter("timecardID");
        String arrivalTime = TimeCardLogic.creteInputForTimecardModify(user.getUserID(), year, month, date, "arrivalTime", timecardid);
        String leaveTime = TimeCardLogic.creteInputForTimecardModify(user.getUserID(), year, month, date, "leaveTime", timecardid);
        //String goOutTime = TimeCardLogic.creteInputForTimecardModify(user.getUserID(), year, month, date, "goOutTime", timecardid);
        //String goBackTime = TimeCardLogic.creteInputForTimecardModify(user.getUserID(), year, month, date, "goBackTime", timecardid);
        request.setAttribute("arrivalTime", arrivalTime);
        request.setAttribute("leaveTime", leaveTime);
        //request.setAttribute("goOutTime", goOutTime);
        //request.setAttribute("goBackTime", goBackTime);
        String timecardID = TimeCardDAO.getTimeCardID(user.getUserID(), year, month, date);
        request.setAttribute("timecardID", timecardID);
        date = (new StringBuilder(String.valueOf(year))).append("/").append(month).append("/").append(date).toString();
        request.setAttribute("date", date);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/timecardModify.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        String arrivalTimeHour = request.getParameter("arrivalTimeHour");
        String arrivalTimeMin = request.getParameter("arrivalTimeMin");
        System.out.println((new StringBuilder("Arrival hour:")).append(arrivalTimeHour).toString());
        System.out.println((new StringBuilder("arrival Min:")).append(arrivalTimeMin).toString());
        String arrivalTime = arrivalTimeHour.concat(":").concat(arrivalTimeMin);
        if(arrivalTimeHour.equals("--") || arrivalTimeMin.equals("--"))
            arrivalTime = "";
        String leaveTimeHour = request.getParameter("leaveTimeHour");
        String leaveTimeMin = request.getParameter("leaveTimeMin");
        System.out.println((new StringBuilder("leave hour:")).append(leaveTimeHour).toString());
        System.out.println();
        String leaveTime = leaveTimeHour.concat(":").concat(leaveTimeMin);
        if(leaveTimeHour.equals("--") || leaveTimeMin.equals("--"))
            leaveTime = "";
        /*
         * String goOutTimeHour = request.getParameter("goOutTimeHour");
        String goOutTimeMin = request.getParameter("goOutTimeMin");
        String goOutTime = goOutTimeHour.concat(":").concat(goOutTimeMin);
        if(goOutTimeHour.equals("--") || goOutTimeMin.equals("--"))
            goOutTime = "";
        String goBackTimeHour = request.getParameter("goBackTimeHour");
        String goBackTimeMin = request.getParameter("goBackTimeMin");
        String goBackTime = goBackTimeHour.concat(":").concat(goBackTimeMin);
        if(goBackTimeHour.equals("--") || goBackTimeMin.equals("--"))
            goBackTime = "";
         *
         */
        String timecardID = request.getParameter("timecardID");
        HttpSession session = request.getSession();
        Account user = (Account)session.getAttribute("user");
        String userID = user.getUserID();
        TimeCard timeCard = null;
        if(timecardID.equalsIgnoreCase("null"))
        {
            timeCard = new TimeCard(userID, arrivalTime, "", "", leaveTime);
            String date = request.getParameter("date");
            if(!date.equalsIgnoreCase("null"))
            {
                String temp[] = date.split("/");
                String year = temp[0];
                String month = temp[1];
                String day = temp[2];
                TimeCardDAO.newTimeCard(timeCard, year, month, day);
            }
        } else
        {
            timeCard = new TimeCard(timecardID, userID, arrivalTime, "", "", leaveTime);
            TimeCardDAO.updateTimeCard(timeCard);
        }
        response.sendRedirect("/SVD_IntraNet/TimeCardServlet");
    }

    private static final long serialVersionUID = 1L;
}
