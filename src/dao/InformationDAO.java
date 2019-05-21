// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   InformationDAO.java

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
//            Information

public class InformationDAO
{

    public InformationDAO()
    {
    }

    public static void newInfo(Information information)
    {
        Connection connection;
        if(information == null)
            return;
        connection = null;
        try {
        	 Class.forName("org.h2.Driver");
             connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
             String source = information.getSourceUserID();
             String sourceName = information.getSourceUserName();
             String destination = information.getDestinationUserID();
             String title = information.getTitle();
             String comment = information.getComment();
             String sql = "INSERT INTO INFORMATION (SOURCEID, SOURCENAME, DESTINATIONID, TITLE, COMMENT) ";
             sql = (new StringBuilder(String.valueOf(sql))).append("VALUES('").append(source).append("','").append(sourceName).append("','").append(destination).append("','").append(title).append("','").append(comment).append("');").toString();
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

    public static List getPublicInfo()
    {
        Connection connection;
        List information;
        connection = null;
        information = new ArrayList();
        try {
        	Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
            String sql = "SELECT * FROM INFORMATION WHERE DESTINATIONID ='' ORDER BY INFOID DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            Information temp;
            for(ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next(); information.add(temp))
            {
                String sourceID = resultSet.getString("SOURCEID");
                String sourceName = resultSet.getString("SOURCENAME");
                String destinationID = resultSet.getString("DESTINATIONID");
                String title = resultSet.getString("TITLE");
                String comment = resultSet.getString("COMMENT");
                temp = new Information(sourceID, sourceName, destinationID, title, comment);
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
        return information;
    }

    public static List getPersonalInfo(String userID)
    {
        Connection connection;
        List information;
        connection = null;
        information = new ArrayList();
        try {
        	Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
            String sql = (new StringBuilder("SELECT * FROM INFORMATION WHERE DESTINATIONID ='")).append(userID).append("' ORDER BY INFOID DESC").toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            Information temp;
            for(ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next(); information.add(temp))
            {
                String sourceID = resultSet.getString("SOURCEID");
                String sourceName = resultSet.getString("SOURCENAME");
                String destinationID = userID;
                String title = resultSet.getString("TITLE");
                String comment = resultSet.getString("COMMENT");
                temp = new Information(sourceID, sourceName, destinationID, title, comment);
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
        return information;
    }
}
