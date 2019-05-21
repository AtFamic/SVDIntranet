// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   CalendarLogic.java

package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dao.Account;
import dao.AccountDAO;
import dao.Task;
import dao.TaskDAO;

// Referenced classes of package model:
//            AccountLogic

public class CalendarLogic
{

    public CalendarLogic()
    {
    }

    public static String createCalendar(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(1);
        int month = calendar.get(2);
        int dayOfMonth = calendar.get(5);
        calendar.set(year, month, 1);
        int startDayOfWeek = calendar.get(7);
        calendar.set(year, month + 1, 0);
        int lastDayOfMouth = calendar.get(5);
        calendar.set(year, month, 0);
        int lastYear = calendar.get(1);
        int lastMonth = calendar.get(2);
        int lastDayOfLastMonth = calendar.get(5);
        int calendarYear[] = new int[42];
        int calendarMonth[] = new int[42];
        int calendarDay[] = new int[42];
        int counter = 0;
        for(int i = startDayOfWeek - 2; i >= 0; i--)
        {
            calendarYear[counter] = lastYear;
            calendarMonth[counter] = lastMonth;
            calendarDay[counter] = lastDayOfLastMonth - i;
            counter++;
        }

        for(int i = 1; i <= lastDayOfMouth; i++)
        {
            calendarYear[counter] = year;
            calendarMonth[counter] = month;
            calendarDay[counter] = i;
            counter++;
        }

        calendar.set(year, month + 1, dayOfMonth);
        int nextYear = calendar.get(1);
        int nextMonth = calendar.get(2);
        int nextMonthDay = 1;
        for(; counter % 7 != 0; counter++)
        {
            calendarYear[counter] = nextYear;
            calendarMonth[counter] = nextMonth;
            calendarDay[counter] = nextMonthDay++;
        }

        String gotoLastMonth = (new StringBuilder("<div class=\"month\">\r\n<a href=\"/SVD_IntraNet/MonthViewServlet?Year=")).append(year).append("&Month=").append(month - 1).append("\">\u5148\u6708 </a>").toString();
        String thisMonth = (new StringBuilder(String.valueOf(year))).append("\u5E74").append(month + 1).append("\u6708").toString();
        String gotoNextMonth = (new StringBuilder("<a href=\"/SVD_IntraNet/MonthViewServlet?Year=")).append(year).append("&Month=").append(month + 1).append("\"> \u6765\u6708</a></div>\r\n").toString();
        String theFirstCode = (new StringBuilder(String.valueOf(gotoLastMonth))).append(thisMonth).append(gotoNextMonth).append("<div class=\"calendar\">\r\n").append("<table class=\"calendar schedule\">\r\n").append("\t<tr>\r\n").append("\t\t<th class=\"sun\">\u65E5</th><th class=\"mon\">\u6708</th><th class=\"tue\">\u706B</th><th class=\"wed\">\u6C34</th><th class=\"thu\">\u6728</th><th class=\"fri\">\u91D1</th><th class=\"sat\">\u571F</th>\r\n").append("\t</tr>\r\n").toString();
        StringBuffer result = new StringBuffer(theFirstCode);
        int weekCounter = counter / 7;
        for(int i = 0; i < weekCounter; i++)
        {
            result.append("<tr>\r\n");
            for(int j = i * 7; j < i * 7 + 7; j++)
            {
                if(calendarDay[j] < 10)
                {
                    if(calendarMonth[j] < 10)
                        result.append((new StringBuilder("<td class=\"tdlink\"><div class=\"date\" class=\"block\"><a id=\"")).append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\" href=\"/SVD_IntraNet/DailyViewServlet?action=").append(calendarYear[j]).append("-0").append(calendarMonth[j]).append("-0").append(calendarDay[j]).append("\"> ").append(calendarDay[j]).append("</a></div>").toString());
                    else
                        result.append((new StringBuilder("<td class=\"tdlink\"><div class=\"date\" class=\"block\"><a id=\"")).append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\" href=\"/SVD_IntraNet/DailyViewServlet?action=").append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-0").append(calendarDay[j]).append("\"> ").append(calendarDay[j]).append("</a></div>").toString());
                } else
                if(calendarMonth[j] < 10)
                    result.append((new StringBuilder("<td class=\"tdlink\"><div class=\"date\" class=\"block\"><a id=\"")).append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\" href=\"/SVD_IntraNet/DailyViewServlet?action=").append(calendarYear[j]).append("-0").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\">").append(calendarDay[j]).append(" </a></div>").toString());
                else
                    result.append((new StringBuilder("<td class=\"tdlink\"><div class=\"date\" class=\"block\"><a id=\"")).append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\" href=\"/SVD_IntraNet/DailyViewServlet?action=").append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\">").append(calendarDay[j]).append(" </a></div>").toString());
                String task = createTaskHTML(String.valueOf(calendarYear[j]), String.valueOf(calendarMonth[j] + 1), String.valueOf(calendarDay[j]));
                result.append(task);
                result.append("</td>");
            }

            result.append("</tr>\r\n");
        }

        result.append("\r\n</table>\r\n");
        result.append("</div>");
        return result.toString();
    }

    public Date calcLastDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(1);
        int month = calendar.get(2);
        if(month == 11)
        {
            year++;
            month = 0;
        } else
        {
            month++;
        }
        calendar.set(year, month, 1);
        calendar.add(5, -1);
        return calendar.getTime();
    }

    public static String createFirstWeeklyRowForCalendar(Date date)
    {
        StringBuffer result = new StringBuffer("");
        result.append("<tr>");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for(int i = 0; i < 7; i++)
        {
            result.append("<td class=\"dayOfWeek\" ");
            int dayOfWeek = calendar.get(7);
            if(dayOfWeek == 1)
                result.append("bgcolor=\"#FADBDA\">");
            else
            if(dayOfWeek == 7)
                result.append("bgcolor=\"#ADD8E6\">");
            else
                result.append("bgcolor=\"#D3D3D3\">");
            result.append(calendar.get(5));
            switch(dayOfWeek)
            {
            case 1: // '\001'
                result.append("\uFF08\u65E5\uFF09");
                break;

            case 2: // '\002'
                result.append("\uFF08\u6708\uFF09");
                break;

            case 3: // '\003'
                result.append("\uFF08\u706B\uFF09");
                break;

            case 4: // '\004'
                result.append("\uFF08\u6C34\uFF09");
                break;

            case 5: // '\005'
                result.append("\uFF08\u6728\uFF09");
                break;

            case 6: // '\006'
                result.append("\uFF08\u91D1\uFF09");
                break;

            case 7: // '\007'
                result.append("\uFF08\u571F\uFF09");
                break;
            }
            result.append("</td>\r\n");
            calendar.add(5, 1);
        }

        result.append("</tr>");
        return result.toString();
    }

    /**
     *
     * @param date
     * @param users
     * @param user ログイン中ユーザーアカウント情報
     * @return
     */
    public static String createMonthCalendarHTMLByUsersFor1(Date date, List users, Account user)
    {
        int size = users == null ? 0 : users.size();
        StringBuffer result = new StringBuffer("");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int today_year = calendar.get(1);
        int today_month = calendar.get(2) + 1;
        int today_dayOfMonth = calendar.get(5);
        calendar.setTime(date);
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int dayOfMonth = calendar.get(5);
        calendar.add(2, -1);
        long lastMonth = calendar.getTimeInMillis();
        calendar.setTime(date);
        calendar.add(2, 1);
        long nextMonth = calendar.getTimeInMillis();
        String selectHTML = AccountLogic.createAllAcountsSelectHTMLFor1((String)users.get(0));
        result.append((new StringBuilder("<table class=\"caption\">\r\n<tr><td class=\"name\">")).append(selectHTML).append("\u306E\u4E88\u5B9A</td>").append("<td class=\"date\">").append(year).append("\u5E74").append(month).append("\u6708</td>\r\n").append("<td class=\"changeMonth\"><a href=\"/SVD_IntraNet/MonthViewServlet?date=").append(lastMonth).append("\">\u5148\u6708</a></td>\r\n").append("<td class=\"changeMonth\"><a href=\"/SVD_IntraNet/MonthViewServlet\"?date=").append((new Date()).getTime()).append("\">\u4ECA\u6708</a></td>\r\n").append("<td class=\"changeMonth\"><a href=\"/SVD_IntraNet/MonthViewServlet?date=").append(nextMonth).append("\">\u7FCC\u6708</a></td></tr>\r\n").append("</table>\r\n").toString());
        calendar.setTime(date);
        calendar.add(5, -7);
        long lastWeek = calendar.getTimeInMillis();
        calendar.setTime(date);
        calendar.add(5, 7);
        long nextWeek = calendar.getTimeInMillis();
        result.append((new StringBuilder("<div id=\"changeWeek\">\r\n<a href=\"/SVD_IntraNet/MonthViewServlet?date=")).append(lastWeek).append("\">\u5148\u9031</a>\r\n").append("<a href=\"/SVD_IntraNet/MonthViewServlet?date=").append(nextWeek).append("\">\u6765\u9031</a>\r\n").append("</div>").toString());
        result.append("<table class=\"calendar\">\r\n");
        calendar.setTime(date);
        int day = calendar.get(7);
        switch(day)
        {
        case 2: // '\002'
            calendar.add(5, -1);
            break;

        case 3: // '\003'
            calendar.add(5, -2);
            break;

        case 4: // '\004'
            calendar.add(5, -3);
            break;

        case 5: // '\005'
            calendar.add(5, -4);
            break;

        case 6: // '\006'
            calendar.add(5, -5);
            break;

        case 7: // '\007'
            calendar.add(5, -6);
            break;
        }
        for(int i = 0; i < 5; i++)
        {
            result.append(createFirstWeeklyRowForCalendar(calendar.getTime()));
            result.append(createWeeklyRowByMultipleUsers(calendar.getTime(), users, user.getUserID()));
            calendar.add(5, 7);
        }

        result.append("</table>");
        return result.toString();
    }

    /**
     *
     * @param date
     * @param users
     * @param user ログイン中ユーザーアカウント情報
     * @return
     */
    public static String createMonthCalendarHTMLByUsersFor2(Date date, List users, Account user)
    {
        int size = users == null ? 0 : users.size();
        StringBuffer result = new StringBuffer("");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int today_year = calendar.get(1);
        int today_month = calendar.get(2) + 1;
        int today_dayOfMonth = calendar.get(5);
        calendar.setTime(date);
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int dayOfMonth = calendar.get(5);
        calendar.add(2, -1);
        long lastMonth = calendar.getTimeInMillis();
        calendar.setTime(date);
        calendar.add(2, 1);
        long nextMonth = calendar.getTimeInMillis();
        String selectHTML = AccountLogic.createAllAcountsSelectHTMLFor2((String)users.get(0));
        result.append((new StringBuilder("<table class=\"caption\">\r\n<tr><td class=\"name\">")).append(selectHTML).append("\u306E\u4E88\u5B9A</td>").append("<td class=\"date\">").append(year).append("\u5E74").append(month).append("\u6708</td>\r\n").append("<td class=\"changeMonth\"><a href=\"/SVD_IntraNet/MonthViewServlet?date=").append(lastMonth).append("\">\u5148\u6708</a></td>\r\n").append("<td class=\"changeMonth\"><a href=\"/SVD_IntraNet/MonthViewServlet\"?date=").append((new Date()).getTime()).append("\">\u4ECA\u6708</a></td>\r\n").append("<td class=\"changeMonth\"><a href=\"/SVD_IntraNet/MonthViewServlet?date=").append(nextMonth).append("\">\u7FCC\u6708</a></td></tr>\r\n").append("</table>\r\n").toString());
        calendar.setTime(date);
        calendar.add(5, -7);
        long lastWeek = calendar.getTimeInMillis();
        calendar.setTime(date);
        calendar.add(5, 7);
        long nextWeek = calendar.getTimeInMillis();
        result.append((new StringBuilder("<div id=\"changeWeek\">\r\n<a href=\"/SVD_IntraNet/MonthViewServlet?date=")).append(lastWeek).append("\">\u5148\u9031</a>\r\n").append("<a href=\"/SVD_IntraNet/MonthViewServlet?date=").append(nextWeek).append("\">\u6765\u9031</a>\r\n").append("</div>").toString());
        result.append("<table class=\"calendar\">\r\n");
        calendar.setTime(date);
        int day = calendar.get(7);
        switch(day)
        {
        case 2: // '\002'
            calendar.add(5, -1);
            break;

        case 3: // '\003'
            calendar.add(5, -2);
            break;

        case 4: // '\004'
            calendar.add(5, -3);
            break;

        case 5: // '\005'
            calendar.add(5, -4);
            break;

        case 6: // '\006'
            calendar.add(5, -5);
            break;

        case 7: // '\007'
            calendar.add(5, -6);
            break;
        }
        for(int i = 0; i < 5; i++)
        {
            result.append(createFirstWeeklyRowForCalendar(calendar.getTime()));
            result.append(createWeeklyRowByMultipleUsers(calendar.getTime(), users, user.getUserID()));
            calendar.add(5, 7);
        }

        result.append("</table>");
        return result.toString();
    }

    /**
     *
     * @param date
     * @param users
     * @param userID メソッドの呼び出し側のユーザーID
     * @return
     */
    public static String createWeeklyRowByMultipleUsers(Date date, List users, String userID)
    {
        StringBuffer result = new StringBuffer("");
        Date today = date;
        List accounts = new ArrayList();
        List names = new ArrayList();
        int size_temp = users == null ? 0 : users.size();
        for(int i = 0; i < size_temp; i++)
        {
            accounts.add(AccountDAO.findAccountByUserID((String)users.get(i)));
            names.add(((Account)accounts.get(i)).getName());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        for(int i = 0; i < 7; i++)
        {
            result.append("<td class=\"task\">\r\n");
            int year = calendar.get(1);
            int month = calendar.get(2) + 1;
            int day = calendar.get(5);
            List tasks = new ArrayList();
            for(int j = 0; j < size_temp; j++)
            {
                List temp = TaskDAO.findTasksByDateANDUserID(year, month, day, ((Account)accounts.get(j)).getUserID());
                int size_temp2 = temp == null ? 0 : temp.size();
                for(int s = 0; s < size_temp2; s++)
                    tasks.add((Task)temp.get(s));

            }

            int size = tasks == null ? 0 : tasks.size();
            for(int j = 0; j < size; j++)
            {
                Task task = (Task)tasks.get(j);
                String taskID = task.getTaskID();
                String name = AccountDAO.findAccountByUserID(task.getUserID()).getName();
                String startTime = task.getStartTime();
                String endTime = task.getEndTime();
                String title = task.getTitle();
                String color = task.getColor();
                String startHour = startTime.substring(8, 10);
                String startMin = startTime.substring(10, 12);
                String endHour = endTime.substring(8, 10);
                String endMin = endTime.substring(10, 12);
                result.append((new StringBuilder("<div class=\"")).append(color).append("\">").toString());
                result.append((new StringBuilder(String.valueOf(startHour))).append(":").append(startMin).append("-").append(endHour).append(":").append(endMin).toString());
                result.append("<br>\r\n");
                result.append((new StringBuilder("<span class=\"")).append(color).append("\">").append(color).append("</span>\r\n").toString());
                result.append((new StringBuilder("<br>\r\n<a href=\"ScheduleServlet?taskID=" + taskID + "\">" )).append(title).append("<br>\r\n\u3010").append(name).append("\u3011</a>\r\n").toString());
                //削除ボタンをUserIDが一致していた場合のみ表示する
                if(userID.equals(task.getUserID())) {
                	result.append("<a href=\"ScheduleServlet?taskID=" + taskID + "&action=delete\">削除</a>\r\n");
                }
                result.append("</div>\r\n");
            }

            result.append((new StringBuilder("<a href=\"/SVD_IntraNet/ScheduleServlet?year=")).append(year).append("&month=").append(month).append("&date=").append(day).append("\"><img border=\"0\" src=\"img/plus.png\" alt=\"Plus\"></img></a>").toString());
            result.append("</td>\r\n");
            calendar.add(5, 1);
        }

        result.append("</tr>");
        return result.toString();
    }

    public static String createTaskHTML(String year, String month, String day)
    {
        String result = "";
        List tasks = TaskDAO.findTasksByMonth(year, month);
        int length = tasks == null ? 0 : tasks.size();
        for(int i = 0; i < length; i++)
        {
            Task temp = (Task)tasks.get(i);
            if(temp.getDate().equals(String.valueOf(day)))
            {
                System.out.println(temp.getUserID());
                String name = AccountDAO.findAccountByUserID(temp.getUserID()).getName();
                String startTime = temp.getStartTime().substring(8, 12);
                if(temp.getStartTime().substring(0, 8).equals(temp.getEndTime().substring(0, 8)))
                {
                    String endTime = temp.getEndTime().substring(8, 12);
                    result = (new StringBuilder(String.valueOf(result))).append("\r\n<div class=\"block\"><a href=\"/SVD_IntraNet/ScheduleServlet?taskID=").append(temp.getTaskID()).append("\">").append(startTime).append("\uFF5E").append(endTime).append(":").append(name).append("</a></div>").toString();
                } else
                {
                    result = (new StringBuilder(String.valueOf(result))).append("\r\n<div class=\"block\"><a href=\"/SVD_IntraNet/ScheduleServlet?taskID=").append(temp.getTaskID()).append("\">").append(startTime).append("\uFF5E").append(":").append(name.substring(0, 2)).append("</a></div>").toString();
                }
            }
        }

        return result;
    }

    public static String createDaily(String date)
    {
        System.out.println((new StringBuilder("date:")).append(date).toString());
        List tasks = TaskDAO.findTasksByDate(date);
        StringBuffer result = new StringBuffer();
        result.append("<table class=\"sche\" align=\"center\">\r\n\t<tr><td class=\"top\" style=\"width:80px\">\u6642\u523B</td><td class=\"top\" style=\"width:300px\">\u4E88\u5B9A</td></tr>\r\n");
        int size = tasks == null ? 0 : tasks.size();
        int time_width[] = new int[size];
        String time_start[] = new String[size];
        String userName[] = new String[size];
        for(int i = 0; i < size; i++)
        {
            Task task = (Task)tasks.get(i);
            String startTime = task.getStartTime();
            String endTime = task.getEndTime();
            userName[i] = AccountDAO.findAccountByUserID(task.getUserID()).getName();
            if(String.valueOf(Integer.parseInt(startTime.substring(0, 8))).equals(String.valueOf(Integer.parseInt(endTime.substring(0, 8)))))
            {
                String startTime_temp = startTime.substring(8, 12);
                String endTime_temp = endTime.substring(8, 12);
                int start_hour = Integer.parseInt(startTime_temp.substring(0, 2));
                int start_min = Integer.parseInt(startTime_temp.substring(2, 4));
                int end_hour = Integer.parseInt(endTime_temp.substring(0, 2));
                int end_min = Integer.parseInt(endTime_temp.substring(2, 4));
                System.out.println((new StringBuilder("startHour, startMin, endHour, endMin")).append(start_hour).append(",").append(start_min).append(",").append(end_hour).append(",").append(end_min).toString());
                time_width[i] = (end_hour - start_hour) * 60 + (end_min - start_min) <= 0 ? 1 : ((end_hour - start_hour) * 60 + (end_min - start_min)) / 30 + 1;
                start_min /= 30;
                start_min *= 30;
                time_start[i] = (new StringBuilder(String.valueOf(String.valueOf(start_hour)))).append(String.valueOf(start_min)).toString();
            } else
            {
                time_width[i] = 0;
                time_start[i] = "skip";
            }
        }

        for(int i = 0; i < 24; i++)
        {
            String time = String.valueOf(i);
            if(time.length() == 1)
                time = (new StringBuilder("0")).append(time).append(":00").toString();
            else
                time = (new StringBuilder(String.valueOf(time))).append(":00").toString();
            result.append((new StringBuilder("<tr><td class=")).append(time).append(">").toString());
            result.append(time);
            result.append("</td>");
            boolean checker = false;
            for(int j = 0; j < size; j++)
            {
                if(time_start[j].equals("skip"))
                    continue;
                String temp = (new StringBuilder(String.valueOf(String.valueOf(i)))).append(String.valueOf(0)).toString();
                if(!time_start[j].equals(temp))
                    continue;
                result.append((new StringBuilder("<td class=\"ex\" rowspan=\"")).append(time_width[j]).append("\">").append(userName[j]).append("</td>\r\n").toString());
                checker = true;
                break;
            }

            if(!checker)
                result.append("<td class=\"contents\"></td></tr>\r\n\t<tr><td class=\"timeb\"></td>");
            checker = false;
            for(int j = 0; j < size; j++)
            {
                if(time_start[j].equals("skip"))
                    continue;
                String temp = (new StringBuilder(String.valueOf(String.valueOf(i)))).append(String.valueOf(0)).toString();
                temp = (new StringBuilder(String.valueOf(String.valueOf(i)))).append(String.valueOf(30)).toString();
                if(time_start[j].equals(temp))
                {
                    result.append((new StringBuilder("<td class=\"ex\" rowspan=\"")).append(time_width[j]).append("\">").append(userName[j]).append("</td>\r\n").toString());
                    checker = true;
                    break;
                }
                System.out.println((new StringBuilder(String.valueOf(j))).append("\u56DE\u76EE temp:").append(temp).append(" timestart:").append(time_start[j]).append("timewidth").append(time_width[j]).toString());
            }

            if(!checker)
                result.append("<td class=\"contentsb\"></td></tr>\r\n");
        }

        result.append("\r\n</table>");
        System.out.println((new StringBuilder("size:")).append(size).toString());
        return result.toString();
    }

    public static String createWeeklyRow(Date date, String userID)
    {
        StringBuffer result = new StringBuffer("");
        Date today = date;
        Account account = AccountDAO.findAccountByUserID(userID);
        String name = account.getName();
        result.append((new StringBuilder("<tr><td><div class = \"name\"><img border=\"0\" src=\"img/user.png\" alt=\"User Image\">")).append(name).append("</img></div></td>\r\n").toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        for(int i = 0; i < 7; i++)
        {
            result.append("<td>\r\n");
            int year = calendar.get(1);
            int month = calendar.get(2) + 1;
            int day = calendar.get(5);
            List tasks = TaskDAO.findTasksByDateANDUserID(year, month, day, userID);
            int size = tasks == null ? 0 : tasks.size();
            for(int j = 0; j < size; j++)
            {
                Task task = (Task)tasks.get(j);
                String startTime = task.getStartTime();
                String endTime = task.getEndTime();
                String title = task.getTitle();
                String color = task.getColor();
                String startHour = startTime.substring(8, 10);
                String startMin = startTime.substring(10, 12);
                String endHour = endTime.substring(8, 10);
                String endMin = endTime.substring(10, 12);
                result.append((new StringBuilder("<div class=\"")).append(color).append("\">").toString());
                result.append((new StringBuilder(String.valueOf(startHour))).append(":").append(startMin).append("-").append(endHour).append(":").append(endMin).toString());
                result.append("<br>\r\n");
                result.append((new StringBuilder("<span class=\"")).append(color).append("\">").append(color).append("</span>\r\n").toString());
                result.append(title);
                result.append("</div>\r\n");
            }

            result.append((new StringBuilder("<a href=\"/SVD_IntraNet/ScheduleServlet?year=")).append(year).append("&month=").append(month).append("&date=").append(day).append("\"><img border=\"0\" src=\"img/plus.png\" alt=\"Plus\"></img></a>").toString());
            result.append("</td>\r\n");
            calendar.add(5, 1);
        }

        result.append("</tr>");
        return result.toString();
    }

    public static String createFirstWeeklyRow(Date date)
    {
        StringBuffer result = new StringBuffer("");
        result.append("<tr><th></th>");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for(int i = 0; i < 7; i++)
        {
            result.append("<th class=\"dayOfWeek\" ");
            int dayOfWeek = calendar.get(7);
            if(dayOfWeek == 1)
                result.append("bgcolor=\"#FADBDA\">");
            else
            if(dayOfWeek == 7)
                result.append("bgcolor=\"#ADD8E6\">");
            else
                result.append("bgcolor=\"#D3D3D3\">");
            result.append(calendar.get(5));
            switch(dayOfWeek)
            {
            case 1: // '\001'
                result.append("\uFF08\u65E5\uFF09");
                break;

            case 2: // '\002'
                result.append("\uFF08\u6708\uFF09");
                break;

            case 3: // '\003'
                result.append("\uFF08\u706B\uFF09");
                break;

            case 4: // '\004'
                result.append("\uFF08\u6C34\uFF09");
                break;

            case 5: // '\005'
                result.append("\uFF08\u6728\uFF09");
                break;

            case 6: // '\006'
                result.append("\uFF08\u91D1\uFF09");
                break;

            case 7: // '\007'
                result.append("\uFF08\u571F\uFF09");
                break;
            }
            result.append("</th>\r\n");
            calendar.add(5, 1);
        }

        result.append("</tr>");
        return result.toString();
    }

    public static String justCreateCalendar(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(1);
        int month = calendar.get(2);
        int dayOfMonth = calendar.get(5);
        calendar.set(year, month, 1);
        int startDayOfWeek = calendar.get(7);
        calendar.set(year, month + 1, 0);
        int lastDayOfMouth = calendar.get(5);
        calendar.set(year, month, 0);
        int lastYear = calendar.get(1);
        int lastMonth = calendar.get(2);
        int lastDayOfLastMonth = calendar.get(5);
        int calendarYear[] = new int[42];
        int calendarMonth[] = new int[42];
        int calendarDay[] = new int[42];
        int counter = 0;
        for(int i = startDayOfWeek - 2; i >= 0; i--)
        {
            calendarYear[counter] = lastYear;
            calendarMonth[counter] = lastMonth;
            calendarDay[counter] = lastDayOfLastMonth - i;
            counter++;
        }

        for(int i = 1; i <= lastDayOfMouth; i++)
        {
            calendarYear[counter] = year;
            calendarMonth[counter] = month;
            calendarDay[counter] = i;
            counter++;
        }

        calendar.set(year, month + 1, dayOfMonth);
        int nextYear = calendar.get(1);
        int nextMonth = calendar.get(2);
        int nextMonthDay = 1;
        for(; counter % 7 != 0; counter++)
        {
            calendarYear[counter] = nextYear;
            calendarMonth[counter] = nextMonth;
            calendarDay[counter] = nextMonthDay++;
        }

        String gotoLastMonth = (new StringBuilder("<div class=\"month\">\r\n<a href=\"/SVD_IntraNet/DefaultViewServlet?Year=")).append(year).append("&Month=").append(month - 1).append("\">\u5148\u6708 </a>").toString();
        String thisMonth = (new StringBuilder(String.valueOf(year))).append("\u5E74").append(month + 1).append("\u6708").toString();
        String gotoNextMonth = (new StringBuilder("<a href=\"/SVD_IntraNet/DefaultViewServlet?Year=")).append(year).append("&Month=").append(month + 1).append("\"> \u6765\u6708</a></div>\r\n").toString();
        String theFirstCode = (new StringBuilder(String.valueOf(gotoLastMonth))).append(thisMonth).append(gotoNextMonth).append("<div class=\"calendar\">\r\n").append("<table class=\"calendar schedule\">\r\n").append("\t<tr>\r\n").append("\t\t<th class=\"sun\">\u65E5</th><th class=\"mon\">\u6708</th><th class=\"tue\">\u706B</th><th class=\"wed\">\u6C34</th><th class=\"thu\">\u6728</th><th class=\"fri\">\u91D1</th><th class=\"sat\">\u571F</th>\r\n").append("\t</tr>\r\n").toString();
        StringBuffer result = new StringBuffer(theFirstCode);
        int weekCounter = counter / 7;
        for(int i = 0; i < weekCounter; i++)
        {
            result.append("<tr>\r\n");
            for(int j = i * 7; j < i * 7 + 7; j++)
            {
                if(calendarDay[j] < 10)
                {
                    if(calendarMonth[j] < 10)
                        result.append((new StringBuilder("<td class=\"tdlink\"><div class=\"date\" class=\"block\"><a id=\"")).append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\" href=\"/SVD_IntraNet/DailyViewServlet?action=").append(calendarYear[j]).append("-0").append(calendarMonth[j]).append("-0").append(calendarDay[j]).append("\"> ").append(calendarDay[j]).append("</a></div>").toString());
                    else
                        result.append((new StringBuilder("<td class=\"tdlink\"><div class=\"date\" class=\"block\"><a id=\"")).append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\" href=\"/SVD_IntraNet/DailyViewServlet?action=").append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-0").append(calendarDay[j]).append("\"> ").append(calendarDay[j]).append("</a></div>").toString());
                } else
                if(calendarMonth[j] < 10)
                    result.append((new StringBuilder("<td class=\"tdlink\"><div class=\"date\" class=\"block\"><a id=\"")).append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\" href=\"/SVD_IntraNet/DailyViewServlet?action=").append(calendarYear[j]).append("-0").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\">").append(calendarDay[j]).append(" </a></div>").toString());
                else
                    result.append((new StringBuilder("<td class=\"tdlink\"><div class=\"date\" class=\"block\"><a id=\"")).append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\" href=\"/SVD_IntraNet/DailyViewServlet?action=").append(calendarYear[j]).append("-").append(calendarMonth[j]).append("-").append(calendarDay[j]).append("\">").append(calendarDay[j]).append(" </a></div>").toString());
                result.append("</td>");
            }

            result.append("</tr>\r\n");
        }

        result.append("\r\n</table>\r\n");
        result.append("</div>");
        return result.toString();
    }
}
