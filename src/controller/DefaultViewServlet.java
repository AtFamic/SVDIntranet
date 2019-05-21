// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   DefaultViewServlet.java

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
import model.CalendarLogic;
import model.InformationLogic;
@WebServlet("/DefaultViewServlet")
public class DefaultViewServlet extends HttpServlet
{

    public DefaultViewServlet()
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
        weeklyTask.append("<table><tr>");
        weeklyTask.append("<td class = \"benchmark\">");
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
        weeklyTask.append((new StringBuilder("<td class=\"weeklyTop\"><div class=\"action\"><a href = \"/SVD_IntraNet/DefaultViewServlet?action=oneWeekBefore&date=")).append(date.getTime()).append("\">\u5148\u9031</a></div></td>\r\n").toString());
        weeklyTask.append((new StringBuilder("<td class=\"weeklyTop\"><div class=\"action\"><a href = \"/SVD_IntraNet/DefaultViewServlet?action=yesterday&date=")).append(date.getTime()).append("\">\u6628\u65E5</a></div></td>\r\n").toString());
        weeklyTask.append((new StringBuilder("<td class=\"weeklyTop\"><div class=\"action\"><a href = \"/SVD_IntraNet/DefaultViewServlet?action=today&date=")).append(date.getTime()).append("\">\u4ECA\u65E5</a></div></td>\r\n").toString());
        weeklyTask.append((new StringBuilder("<td class=\"weeklyTop\"><div class=\"action\"><a href = \"/SVD_IntraNet/DefaultViewServlet?action=tomorrow&date=")).append(date.getTime()).append("\">\u660E\u65E5</a></div></td>\r\n").toString());
        weeklyTask.append((new StringBuilder("<td class=\"weeklyTop\"><div class=\"action\"><a href = \"/SVD_IntraNet/DefaultViewServlet?action=oneWeekAfter&date=")).append(date.getTime()).append("\">\u6765\u9031</a></div></td>\r\n").toString());
        weeklyTask.append("</tr></table>\r\n");
        weeklyTask.append("<table class=\"weekly\">\r\n");
        String firstRow = CalendarLogic.createFirstWeeklyRow(date);
        weeklyTask.append(firstRow);
        HttpSession session = request.getSession();
        Account user = (Account)session.getAttribute("user");
        weeklyTask.append(CalendarLogic.createWeeklyRow(date, user.getUserID()));
        weeklyTask.append("</table>\r\n");
        session.setAttribute("personalWeek", weeklyTask.toString());
        request.setCharacterEncoding("UTF-8");
        date = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        String paramYear = request.getParameter("Year");
        String paramMonth = request.getParameter("Month");
        if(paramYear != null && paramMonth != null)
        {
            int year = Integer.parseInt(request.getParameter("Year"));
            int month = Integer.parseInt(request.getParameter("Month"));
            calendar.set(year, month, 1);
            date = calendar.getTime();
        }
        String monthView = CalendarLogic.justCreateCalendar(date);
        session = request.getSession();
        session.setAttribute("personalMonthView", monthView);
        String publicInformation = InformationLogic.createPublicInfoHTML(4);
        session.setAttribute("publicInformation", publicInformation);
        String personalInformation = InformationLogic.createPersonalInfoHTML(4, user.getUserID());
        session.setAttribute("personalInformation", personalInformation);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/default.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }

    private static final long serialVersionUID = 1L;
}
