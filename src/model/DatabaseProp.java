// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   DatabaseProp.java

package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class DatabaseProp
{

    public DatabaseProp()
    {
    }

    public static void setRealPath(String path)
    {
        realPath = path;
    }

    public static String getDatabasePath()
    {
        String result;
        Path path;
        result = null;
        path = Paths.get(realPath, new String[0]);
        Exception exception;
        exception = null;
        Object obj = null;
        try {
        	BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            Properties properties = new Properties();
            properties.load(reader);
            result = properties.getProperty("database.path");
            if(reader != null)
                reader.close();
        }catch (IOException e) {
			// TODO: handle exception
		}

        return result;
    }

    public static String getDatabaseUser()
    {
        String result;
        Path path;
        result = null;
        path = Paths.get(realPath, new String[0]);
        Exception exception;
        exception = null;
        Object obj = null;
        try {
        	BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            Properties properties = new Properties();
            properties.load(reader);
            result = properties.getProperty("database.user");
            if(reader != null)
                reader.close();
        }catch (IOException e) {
			// TODO: handle exception
		}

        return result;
    }

    public static String realPath;
}
