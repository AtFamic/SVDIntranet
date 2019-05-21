// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   ScheduleServlet.java

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
import dao.Task;
import dao.TaskDAO;
@WebServlet("/ScheduleServlet")
public class ScheduleServlet extends HttpServlet
{

    public ScheduleServlet()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        String taskID = request.getParameter("taskID");
        if(taskID == null){
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            String date = request.getParameter("date");
            if(year != null && month != null && date != null)
            {
                month = month.length() != 1 ? month : "0".concat(month);
                date = date.length() != 1 ? date : "0".concat(date);
                String timeChoosed = year.concat("-").concat(month).concat("-").concat(date);
                request.setAttribute("timeChoosed", timeChoosed);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/schedule.jsp");
            dispatcher.forward(request, response);
        }else{
            Task task = TaskDAO.findTaskByTaskID(taskID);
            request.setAttribute("task", task);
            //actionがdeleteの場合そのタスクを削除する
            if(request.getParameter("action").equals("delete")) {
            	Account user = (Account)request.getSession().getAttribute("user");
            	TaskDAO.deleteTaskByTaskID(taskID, user.getUserID());
            	response.sendRedirect("/SVD_IntraNet/MonthViewServlet");
            	return;
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/schedule.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        String title = request.getParameter("title");
        String startDate = request.getParameter("startDate");
        String startHour = request.getParameter("startHour");
        String startMin = request.getParameter("startMin");
        String endHour = request.getParameter("endHour");
        String endMin = request.getParameter("endMin");
        String isPublic = request.getParameter("isPublic");
        String color = request.getParameter("color");
        String content = request.getParameter("content");
        HttpSession session = request.getSession();
        Account user = (Account)session.getAttribute("user");
        String userID = user.getUserID();
        System.out.println((new StringBuilder("Title:")).append(title).toString());
        System.out.println((new StringBuilder("startTime:")).append(startHour).toString());
        System.out.println((new StringBuilder("color:")).append(color).toString());
        if(content == null)
            content = "";
        String temp[] = startDate.split("-");
        String startYear = temp[0];
        String startMonth = temp[1];
        String startDay = temp[2];
        startHour = startHour.length() != 1 ? startHour : "0".concat(startHour);
        System.out.println(startHour);
        startMin = startMin.length() != 1 ? startMin : (new StringBuilder("0")).append(startMin).toString();
        String startTime = (new StringBuilder(String.valueOf(startYear))).append(startMonth).append(startDay).append(startHour).append(startMin).toString();
        endHour = endHour.length() != 1 ? endHour : (new StringBuilder("0")).append(endHour).toString();
        endMin = endMin.length() != 1 ? endMin : (new StringBuilder("0")).append(endMin).toString();
        String endTime = (new StringBuilder(String.valueOf(startYear))).append(startMonth).append(startDay).append(endHour).append(endMin).toString();
        String taskID = request.getParameter("taskID");
        if(taskID != null)
        {
            Task task = new Task(taskID, startYear, startMonth, startDate, userID, title, startTime, endTime, isPublic, color, content);
            TaskDAO.editTask(task);
        } else
        {
            Task task = new Task(startYear, startMonth, startDay, userID, title, startTime, endTime, isPublic, color, content);
            TaskDAO.newTask(task);
        }
        response.sendRedirect("/SVD_IntraNet/DefaultViewServlet");
    }

    private static final long serialVersionUID = 1L;
}
