// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   ChatworkAPI.java

package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ChatworkAPI
{

    public ChatworkAPI()
    {
    }


    /**
     * ChatworkAPIを用いてメッセージの登録を行うメソッドです。
     * @param message 送りたいメッセージ
     * @param roomnum 送りたいルームナンバー
     */
    public static void sendMessage(String message, int roomnum)
    {
    	String encodedMessage = encodeMessage(message);
        String command[] = {
            "curl", "-X", "POST", "-H", "X-ChatWorkToken: fa4d60276d4e1c9e5d40807295f129b4", "-d", "body=" + encodedMessage, "https://api.chatwork.com/v2/rooms/" + roomnum + "/messages"
        };
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        String curlResult = "";
        String line = "";
        try
        {

            Process process = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            do
            {
                line = r.readLine();
                if(line == null)
                    break;
                curlResult = (new StringBuilder(String.valueOf(curlResult))).append(line).toString();
            } while(true);
            System.out.println(curlResult);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public static String encodeMessage(String message) {
    	try {
    		message = URLEncoder.encode(message, "UTF-8");
    	}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return message;
    }
}
