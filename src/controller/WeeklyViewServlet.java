// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   WeeklyViewServlet.java

package controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.Account;
import dao.AccountDAO;
import model.CalendarLogic;
@WebServlet("/WeeklyViewServlet")
public class WeeklyViewServlet extends HttpServlet
{

    public WeeklyViewServlet()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        Date date = null;
        long oneDay = 0x5265c00L;
        Calendar calendar = Calendar.getInstance();
        String action = request.getParameter("action");
        if(action != null)
            date = new Date(Long.parseLong(request.getParameter("date")));
        if(action == null)
            date = new Date();
        else
        if(action.equals("yesterday"))
            date = new Date(date.getTime() - oneDay);
        else
        if(action.equals("oneWeekBefore"))
            date = new Date(date.getTime() - oneDay * 7L);
        else
        if(action.equals("tomorrow"))
            date = new Date(date.getTime() + oneDay);
        else
        if(action.equals("oneWeekAfter"))
            date = new Date(date.getTime() + oneDay * 7L);
        else
        if(action.equals("today"))
            date = new Date();
        calendar.setTime(date);
        StringBuffer weeklyTask = new StringBuffer("");
        weeklyTask.append("<table class=\"weeklyTop\">\r\n");
        weeklyTask.append("<tr><td class = \"benchmark\">");
        weeklyTask.append((new StringBuilder(String.valueOf(calendar.get(1)))).append("\u5E74").append(calendar.get(2) + 1).append("\u6708").append(calendar.get(5)).append("\u65E5").toString());
        switch(calendar.get(7))
        {
        case 1: // '\001'
            weeklyTask.append("\uFF08\u65E5\uFF09");
            break;

        case 2: // '\002'
            weeklyTask.append("\uFF08\u6708\uFF09");
            break;

        case 3: // '\003'
            weeklyTask.append("\uFF08\u706B\uFF09");
            break;

        case 4: // '\004'
            weeklyTask.append("\uFF08\u6C34\uFF09");
            break;

        case 5: // '\005'
            weeklyTask.append("\uFF08\u6728\uFF09");
            break;

        case 6: // '\006'
            weeklyTask.append("\uFF08\u91D1\uFF09");
            break;

        case 7: // '\007'
            weeklyTask.append("\uFF08\u571F\uFF09");
            break;
        }
        weeklyTask.append("</td>\r\n");
        weeklyTask.append((new StringBuilder("<td class=\"action\"><div class=\"action\"><a href = \"/SVD_IntraNet/WeeklyViewServlet?action=oneWeekBefore&date=")).append(date.getTime()).append("\">\u5148\u9031</a></div></td>\r\n").toString());
        weeklyTask.append((new StringBuilder("<td class=\"action\"><div class=\"action\"><a href = \"/SVD_IntraNet/WeeklyViewServlet?action=yesterday&date=")).append(date.getTime()).append("\">\u6628\u65E5</a></div></td>\r\n").toString());
        weeklyTask.append((new StringBuilder("<td class=\"action\"><div class=\"action\"><a href = \"/SVD_IntraNet/WeeklyViewServlet?action=today&date=")).append(date.getTime()).append("\">\u4ECA\u65E5</a></div></td>\r\n").toString());
        weeklyTask.append((new StringBuilder("<td class=\"action\"><div class=\"action\"><a href = \"/SVD_IntraNet/WeeklyViewServlet?action=tomorrow&date=")).append(date.getTime()).append("\">\u660E\u65E5</a></div></td>\r\n").toString());
        weeklyTask.append((new StringBuilder("<td class=\"action\"><div class=\"action\"><a href = \"/SVD_IntraNet/WeeklyViewServlet?action=oneWeekAfter&date=")).append(date.getTime()).append("\">\u6765\u9031</a></div></td></tr></tabale>\r\n").toString());
        weeklyTask.append("<table class=\"weekly\">\r\n");
        String firstRow = CalendarLogic.createFirstWeeklyRow(date);
        weeklyTask.append(firstRow);
        HttpSession session = request.getSession();
        Account user = (Account)session.getAttribute("user");
        List accounts = AccountDAO.getAllAccounts();
        for(Iterator iterator = accounts.iterator(); iterator.hasNext();)
            if(user.getUserID().equals(((Account)iterator.next()).getUserID()))
                iterator.remove();

        int size = accounts == null ? 0 : accounts.size();
        weeklyTask.append(CalendarLogic.createWeeklyRow(date, user.getUserID()));
        for(int i = 0; i < size; i++)
            weeklyTask.append(CalendarLogic.createWeeklyRow(date, ((Account)accounts.get(i)).getUserID()));

        weeklyTask.append("</table>\r\n");
        session.setAttribute("weekly", weeklyTask.toString());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/weekly.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }

    private static final long serialVersionUID = 1L;
}
