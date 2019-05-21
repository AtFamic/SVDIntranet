// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   TaskDAO.java

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.DatabaseProp;

// Referenced classes of package dao:
//            Task

public class TaskDAO
{

    public TaskDAO()
    {
    }

    public static void newTask(Task task)
    {
        Connection connection = null;
        try {
        	Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
            String year = task.getYear();
            String month = task.getMonth();
            String date = task.getDate();
            String userID = task.getUserID();
            String title = task.getTitle();
            String startTime = task.getStartTime();
            String endTime = task.getEndTime();
            boolean isPublic = task.isPublic();
            String color = task.getColor();
            String content = task.getContent();
            System.out.println("title");
            String sql = "INSERT INTO TASK (YEAR, MONTH, DATE, USERID, TITLE, STARTTIME, ENDTIME, ISPUBLIC, COLOR, CONTENT) ";
            sql = (new StringBuilder(String.valueOf(sql))).append("VALUES('").append(year).append("','").append(month).append("','").append(date).append("','").append(userID).append("','").append(title).append("','").append(startTime).append("','").append(endTime).append("','").append(isPublic).append("','").append(color).append("','").append(content).append("');").toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        }catch (ClassNotFoundException e) {
			// TODO: handle exception
		}catch (SQLException e) {
			// TODO: handle exception
		}finally {
			if(connection != null)
	            try
	            {
	                connection.close();
	            }
	            catch(SQLException e)
	            {
	                e.printStackTrace();
	            }
		}
    }

    public static void editTask(Task task)
    {
        Connection connection = null;
        try {
        	Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
            String taskID = task.getTaskID();
            String year = task.getYear();
            String month = task.getMonth();
            String date = task.getDate();
            String userID = task.getUserID();
            String title = task.getTitle();
            String startTime = task.getStartTime();
            String endTime = task.getEndTime();
            boolean isPublic = task.isPublic();
            String color = task.getColor();
            String content = task.getContent();
            String sql = (new StringBuilder("UPDATE TASK SET  MONTH =")).append(year).append(",MONTH=").append(month).append(",DATE=").append(date).append(",USERID=").append(userID).append(",TITLE=").append(title).append(",STARTTIME=").append(startTime).append(",ENDTIME=").append(endTime).append(",ISPUBLIC=").append(isPublic).append(",COLOR=").append(color).append(",CONTENT=").append(content).append(" WHERE TASKID =").append(taskID).toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        }catch (ClassNotFoundException e) {
			// TODO: handle exception
		}catch (SQLException e) {
			// TODO: handle exception
		}finally {
			if(connection != null)
	            try
	            {
	                connection.close();
	            }
	            catch(SQLException e)
	            {
	                e.printStackTrace();
	            }
		}
    }

    public static Task findTaskByTaskID(String taskID)
    {
        Connection connection;
        Task task;
        connection = null;
        task = null;
        try {
        	 Class.forName("org.h2.Driver");
             connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
             String sql = (new StringBuilder("SELECT * FROM TASK WHERE TASKID ='")).append(taskID).append("'").toString();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             for(ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next();)
             {
                 String year = resultSet.getString("YEAR");
                 String month = resultSet.getString("MONTH");
                 String date = resultSet.getString("DATE");
                 String userID = resultSet.getString("USERID");
                 String title = resultSet.getString("TITLE");
                 String startTime = resultSet.getString("STARTTIME");
                 String endTime = resultSet.getString("ENDTIME");
                 String isPublic = resultSet.getString("ISPUBLIC");
                 String color = resultSet.getString("COLOR");
                 String content = resultSet.getString("Content");
                 task = new Task(taskID, year, month, date, userID, title, startTime, endTime, isPublic, color, content);
             }
        }catch (ClassNotFoundException e) {
			// TODO: handle exception
		}catch (SQLException e) {
			// TODO: handle exception
		}finally {
			if(connection != null)
	            try
	            {
	                connection.close();
	            }
	            catch(SQLException e)
	            {
	                e.printStackTrace();
	            }
		}
        return task;
    }

    public static List findTasksByMonth(String year, String month)
    {
        Connection connection;
        List tasks;
        connection = null;
        tasks = new ArrayList();
        try {
        	Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
            String sql = (new StringBuilder("SELECT * FROM TASK WHERE YEAR ='")).append(year).append("' AND MONTH ='").append(month).append("'").toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            Task task;
            for(ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next(); tasks.add(task))
            {
                String taskID = resultSet.getString("TASKID");
                String date = resultSet.getString("DATE");
                String userID = resultSet.getString("USERID");
                String title = resultSet.getString("TITLE");
                String startTime = resultSet.getString("STARTTIME");
                String endTime = resultSet.getString("ENDTIME");
                String isPublic = resultSet.getString("ISPUBLIC");
                String color = resultSet.getString("COLOR");
                String content = resultSet.getString("Content");
                task = new Task(taskID, year, month, date, userID, title, startTime, endTime, isPublic, color, content);
            }
        }catch (ClassNotFoundException e) {
			// TODO: handle exception
		}catch (SQLException e) {
			// TODO: handle exception
		}finally {
			if(connection != null)
	            try
	            {
	                connection.close();
	            }
	            catch(SQLException e)
	            {
	                e.printStackTrace();
	            }
		}
        return tasks;
    }

    public static List findTasksByDate(String date)
    {
        Connection connection;
        List tasks;
        connection = null;
        tasks = new ArrayList();
        try {
        	Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            month = String.valueOf(Integer.parseInt(month));
            String day = date.substring(6, 8);
            day = String.valueOf(Integer.parseInt(day));
            String sql = (new StringBuilder("SELECT * FROM TASK WHERE YEAR ='")).append(year).append("' AND MONTH ='").append(month).append("' AND DATE ='").append(day).append("'").toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            Task task;
            for(ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next(); tasks.add(task))
            {
                String taskID = resultSet.getString("TASKID");
                String userID = resultSet.getString("USERID");
                String title = resultSet.getString("TITLE");
                String startTime = resultSet.getString("STARTTIME");
                String endTime = resultSet.getString("ENDTIME");
                String isPublic = resultSet.getString("ISPUBLIC");
                String color = resultSet.getString("COLOR");
                String content = resultSet.getString("Content");
                task = new Task(taskID, year, month, day, userID, title, startTime, endTime, isPublic, color, content);
            }
        }catch (ClassNotFoundException e) {
			// TODO: handle exception
		}catch (SQLException e) {
			// TODO: handle exception
		}finally {
			if(connection != null)
	            try
	            {
	                connection.close();
	            }
	            catch(SQLException e)
	            {
	                e.printStackTrace();
	            }
		}
        return tasks;
    }

    public static List findTasksByDateANDUserID(int year, int month, int date, String userID)
    {
        Connection connection;
        List tasks;
        connection = null;
        tasks = new ArrayList();
        try {
        	Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
            String sql = (new StringBuilder("SELECT * FROM TASK WHERE YEAR ='")).append(year).append("' AND MONTH ='").append(month).append("' AND DATE ='").append(date).append("' AND USERID ='").append(userID).append("'").toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            Task task;
            for(ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next(); tasks.add(task))
            {
                String taskID = resultSet.getString("TASKID");
                String title = resultSet.getString("TITLE");
                String startTime = resultSet.getString("STARTTIME");
                String endTime = resultSet.getString("ENDTIME");
                String isPublic = resultSet.getString("ISPUBLIC");
                String color = resultSet.getString("COLOR");
                String content = resultSet.getString("Content");
                task = new Task(taskID, String.valueOf(year), String.valueOf(month), String.valueOf(date), userID, title, startTime, endTime, isPublic, color, content);
            }
        }catch (ClassNotFoundException e) {
			// TODO: handle exception
		}catch (SQLException e) {
			// TODO: handle exception
		}finally {
			if(connection != null)
	            try
	            {
	                connection.close();
	            }
	            catch(SQLException e)
	            {
	                e.printStackTrace();
	            }
		}
        return tasks;
    }

    /**
     * 与えられたタスクIDからそのタスク情報を削除するメソッドです
     * @param taskID 削除したいタスクのID
     * @param userID 削除したいタスクのuserID
     */
    public static void deleteTaskByTaskID(String taskID, String userID) {
    	Connection connection;
        connection = null;
        try {
        	Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
            String sql = (new StringBuilder("DELETE FROM TASK WHERE TASKID ='")).append(taskID).append("' AND USERID ='").append(userID).append("'").toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        }catch (ClassNotFoundException e) {
			// TODO: handle exception
		}catch (SQLException e) {
			// TODO: handle exception
		}finally {
			if(connection != null)
	            try
	            {
	                connection.close();
	            }
	            catch(SQLException e)
	            {
	                e.printStackTrace();
	            }
		}
    }
}
