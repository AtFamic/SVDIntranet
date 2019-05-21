// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   AccountDAO.java

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.DatabaseProp;

// Referenced classes of package dao:
//            Login, Account

public class AccountDAO
{

    private AccountDAO()
    {
        userList = new HashMap();
    }

    public static Account findAccountByLogin(Login login)
    {
        Account account;
        Connection connection;
        account = null;
        connection = null;
        try {
        	Class.forName("org.h2.Driver");
            DatabaseProp.getDatabasePath();
            DatabaseProp.getDatabaseUser();
            connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
            String userID = login.getUserID();
            String password = login.getPassword();
            String sql = (new StringBuilder("SELECT * FROM ACCOUNT WHERE USERID = '")).append(userID).append("' AND PASSWORD = '").append(password).append("'").toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                String name = resultSet.getString("NAME");
                String mail = resultSet.getString("MAIL");
                long lastLogin;
                try
                {
                    lastLogin = resultSet.getLong("LASTLOGIN");
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                    lastLogin = 0L;
                }
                account = new Account(userID, password, name, mail, lastLogin);
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
        return account;
    }

    public static Account findAccountByUserID(String userID)
    {
        Account account = null;
        Connection connection = null;
        try {
        	account = null;
            connection = null;
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
            String sql = (new StringBuilder("SELECT * FROM ACCOUNT WHERE USERID = '")).append(userID).append("'").toString();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                String password = resultSet.getString("PASSWORD");
                String name = resultSet.getString("NAME");
                String mail = resultSet.getString("MAIL");
                long lastLogin;
                try
                {
                    lastLogin = resultSet.getLong("LASTLOGIN");
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                    lastLogin = 0L;
                }
                account = new Account(userID, password, name, mail, lastLogin);
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
        return account;
    }

    public static List getAllAccounts()
    {
        Connection connection;
        List accounts = null;
        Account account = null;
        connection = null;
        try {
        	accounts = new ArrayList();
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(), "");
            String sql = "SELECT * FROM ACCOUNT";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for(ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next();)
            {
                int count = 1;
                String userID = resultSet.getString("USERID");
                String name = resultSet.getString("NAME");
                String mail = resultSet.getString("MAIL");
                account = new Account(userID, "", name, mail, 0L);
                accounts.add(account);
                count++;
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
        return accounts;
    }

    private static AccountDAO accountDAO = new AccountDAO();
    private Map userList;

}
