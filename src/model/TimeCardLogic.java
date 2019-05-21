// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   TimeCardLogic.java

package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dao.TimeCard;
import dao.TimeCardDAO;

public class TimeCardLogic{

	/**
	 * 終了ボタンまで押されたのち、当日の表示にさらに一行追加するためのフラグ
	 * 押されていればTRUE、そうでなければFAULSEとする。
	 */
	private static boolean isFinished;

    public static String createTimeCardTable(String userID, Date today)
    {
        StringBuffer result = new StringBuffer("");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        calendar.add(2, -1);
        Date lastMonth = calendar.getTime();
        calendar.setTime(today);
        calendar.add(2, 1);
        Date nextMonth = calendar.getTime();
        Date thisMonth = new Date();
        //先月・今月・来月への変更<a>タグ
        result.append((new StringBuilder("<table class=\"caption\">\r\n"
        		+ "<tr><th class=\"date\">")).append(year).append("年").append(month).append("月度</th>\r\n"
        		+ "").append("<th class=\"changeDate\"><a href=\"/SVD_IntraNet/TimeCardServlet?date="
        		).append(lastMonth.getTime()).append("\">\u5148\u6708</a></th>\r\n"
        		+ "").append("<th class=\"changeDate\"><a href=\"/SVD_IntraNet/TimeCardServlet?date="
        		).append(thisMonth.getTime()).append("\">\u4ECA\u6708</a></th>\r\n"
        		+ "").append("<th class=\"changeDate\"><a href=\"/SVD_IntraNet/TimeCardServlet?date="
        		).append(nextMonth.getTime()).append("\">\u6765\u6708</a></th></tr>\r\n"
        		+ "").append("</table>").toString());

        //タイムカード出力
        calendar.setTime(today);
        calendar.set(Calendar.DATE, 1);
        result.append(createFirstTimeCardTable());
        result.append(createTimeCardRow(userID, thisMonth, calendar.getTime(), true));
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        int lastDayOfMonth = calendar.get(Calendar.DATE);
        calendar.setTime(today);
        calendar.setLenient(false);
        /**
         * 2日目から最終日までを表示させる。
         * しかし、本日において終了まで記録されている場合は続いてさらにもう一度「開始行」を追加する。
         * isFinishedがtrueの場合、終了が埋まっているので、もう一行追加する。
         */
        for(int i = 2; i <= lastDayOfMonth; i++) {
        	try{
                calendar.set(Calendar.DATE, i);
                result.append(createTimeCardRow(userID, thisMonth, calendar.getTime(), false));
                if(isFinished){
                	result.append(createNextTimeCardRow(userID, thisMonth, calendar.getTime(), false));
                }
            }
            catch(IllegalArgumentException illegalargumentexception) {
            }
        }

        result.append("</table>");
        return result.toString();
    }

    public static String createTimeCardRow(String userID, Date today, Date changeDate, boolean isFirstRow){
    	//isFinishedをfalseで初期化
    	isFinished = false;
        StringBuffer result = new StringBuffer("");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(changeDate);
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        result.append(make_1st_column(changeDate, true));
        //flagは扱っている日付が今日であればTRUEを持つ
        boolean flag = false;
        calendar.setTime(today);
        int monthToday = calendar.get(2) + 1;
        int dayToday = calendar.get(5);
        if(month == monthToday && day == dayToday)
            flag = true;
        calendar.setTime(changeDate);
        List<TimeCard> timeCards = TimeCardDAO.findTimeCardByUserIDANDDate(userID, String.valueOf(year), String.valueOf(month), String.valueOf(day));
        //timeCardの数だけその行を追加します
        int size = timeCards.size();
        //一つもない場合は1に初期化
        if(size == 0) {
        	size = 1;
        	timeCards = new ArrayList<TimeCard>();
        	timeCards.add(new TimeCard("", userID, "", "", "", ""));
        }
        for(int i = 0; i < size; i++) {
        	String arrivalTime = null;
        	String leaveTime = null;
        	isFinished = false;
        	TimeCard timeCard = timeCards.get(i);
        	if(timeCard == null) {
        		timeCard = new TimeCard("", userID, "", "", "", "");
        	}
        	//2回目以降の表示では日付を表示しないセルを一つ挟む
        	if(i != 0) {
        		result.append(make_1st_column(changeDate, false));
        	}

            //日付が本日の場合（終了まで押されている場合isFinishedをtrueにします。
        	result.append(make_2nd_column(changeDate, userID, timeCard));
            //終了時刻
        	result.append(make_3rd_column(changeDate, userID, timeCard));
            //修正
        	result.append(make_4th_column(changeDate, userID, timeCard));
            //勤務時間を計算し表示する。
            String workingTime = "";
            arrivalTime = timeCard.getArrivalTime();
            leaveTime = timeCard.getLeaveTime();
            if(arrivalTime != null && leaveTime != null) {
            	if(!arrivalTime.equals("") && !leaveTime.equals("") ) {
                	String[] tempArrival = arrivalTime.split(":");
                	int hArrival = Integer.parseInt(tempArrival[0]);
                	int mArrival = Integer.parseInt(tempArrival[1]);
                	String[] tempLeave = leaveTime.split(":");
                	int hLeave = Integer.parseInt(tempLeave[0]);
                	int mLeave = Integer.parseInt(tempLeave[1]);

                	int duration = (hLeave * 60 + mLeave) - (hArrival * 60 + mArrival);
                	if(duration >= 0) {
                		int workinghour = duration / 60;
                		int workingmin = duration % 60;
                		workingTime = workinghour + "時間" + workingmin + "分";
                	}else if(duration < 0) {
                		workingTime = "終了時間が開始時間より早くなっています。";
                	}
                }
            }
            result.append(make_5th_column(changeDate, timeCard, workingTime));
        }

        return result.toString();
    }


    /**
     * isFinishedがtrueの場合に、呼ばれることを想定しており、このメソッドは追加分のタイムカード行を出力します。
     * @param userID userID
     * @param today 本日の日付
     * @param changeDate 表示させたい日付
     * @param isFirstRow 一行目かどうか
     * @return 追加分のHTML
     */
    public static String createNextTimeCardRow(String userID, Date today, Date changeDate, boolean isFirstRow) {
        StringBuffer result = new StringBuffer("");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(changeDate);
        int year = calendar.get(1);
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        result.append(make_1st_column(changeDate, false));
        //flagは扱っている日付が今日であればTRUEを持つ
        boolean flag = false;
        String arrivalTime = null;
        String leaveTime = null;
        calendar.setTime(today);
        int monthToday = calendar.get(2) + 1;
        int dayToday = calendar.get(5);
        if(month == monthToday && day == dayToday) {
        	flag = true;
        }
        calendar.setTime(changeDate);
        TimeCard timeCard = new TimeCard("", userID, "", "", "", "");
        result.append(make_2nd_column(changeDate, userID, timeCard));
        result.append(make_3rd_column(changeDate, userID, timeCard));
        result.append(make_4th_column(changeDate, userID, timeCard));
            //日付が本日の場合（終了まで押されている場合isFinishedをtrueにします。
            //勤務時間を計算し表示する。
            String workingTime = "";
            if(arrivalTime != null && leaveTime != null) {
            	if(!arrivalTime.equals("") && !leaveTime.equals("") ) {
                	String[] tempArrival = arrivalTime.split(":");
                	int hArrival = Integer.parseInt(tempArrival[0]);
                	int mArrival = Integer.parseInt(tempArrival[1]);
                	String[] tempLeave = leaveTime.split(":");
                	int hLeave = Integer.parseInt(tempLeave[0]);
                	int mLeave = Integer.parseInt(tempLeave[1]);

                	int duration = (hLeave * 60 + mLeave) - (hArrival * 60 + mArrival);
                	if(duration >= 0) {
                		int workinghour = duration / 60;
                		int workingmin = duration % 60;
                		workingTime = workinghour + "時間" + workingmin + "分";
                	}else if(duration < 0) {
                		workingTime = "終了時間が開始時間より早くなっています。";
                	}
                }
            }
            result.append(make_5th_column(changeDate, timeCard, workingTime));

        return result.toString();
    }

    /**
     * タイムカードの1行目を生成するメソッド
     */
    public static String createFirstTimeCardTable(){
        StringBuffer result = new StringBuffer("");
        result.append("<table class = \"timeCard\">\r\n<tr><th bgcolor=\"#EFEFEF\">日付</th>\r\n<th bgcolor=\"#EFEFEF\">開始時刻</th>\r\n<th bgcolor=\"#EFEFEF\">終了時刻</th>\r\n<th bgcolor=\"#EFEFEF\">備考及び修正</th>\r\n<th bgcolor=\"#EFEFEF\">勤務時間</th></tr>");
        return result.toString();
    }

    public static String creteInputForTimecardModify(String userID, String year, String month, String date, String target, String timecardID)
    {
        StringBuffer result;
        String temp;
label0:
        {
            result = new StringBuffer("");
            TimeCard timeCard = TimeCardDAO.findTimeCardByTimeCardID(userID, timecardID);
            if(timeCard == null) {
            	timeCard = new TimeCard("", userID, "", "", "", "");
            }
            temp = null;
            String s;
            switch((s = target).hashCode()){
            //開始時間
            case -748104397:
                if(!s.equals("goOutTime"))
                    break;
                temp = timeCard.getGoOutTime();
                break label0;

            case -487586202:
                if(s.equals("arrivalTime"))
                {
                    temp = timeCard.getArrivalTime();
                    break label0;
                }
                break;

            case 360581468:
                if(!s.equals("goBackTime"))
                    break;
                temp = timeCard.getGoBackTime();
                break label0;

                //終了時間
            case 1661264420:
                if(!s.equals("leaveTime"))
                    break;
                temp = timeCard.getLeaveTime();
                break label0;
            }
            throw new IllegalArgumentException();
        }
        String hour = temp.equals("") ? "" : temp.split(":")[0];
        String min = temp.equals("") ? "" : temp.split(":")[1];
        System.out.println((new StringBuilder("hour:min")).append(hour).append(":").append(min).toString());
        if(hour == null && min == null)
        {
            result.append((new StringBuilder("<select name=\"")).append(target).append("Hour\">\r\n").toString());
            result.append("<option value=\"--\" selected>--</option>\r\n");
            for(int i = 0; i < 24; i++)
            {
                String iString = String.valueOf(i);
                iString = iString.length() != 2 ? "0".concat(iString) : iString;
                result.append((new StringBuilder("<option value=\"")).append(iString).append("\">").append(iString).append("</option>\r\n").toString());
            }

            result.append((new StringBuilder("</select>:<select name\"")).append(target).append("Min\">\r\n").toString());
            result.append("<option value=\"--\" selected>--</option>\r\n");
            for(int i = 0; i < 60; i++)
            {
                String iString = String.valueOf(i);
                iString = iString.length() != 2 ? "0".concat(iString) : iString;
                result.append((new StringBuilder("<option value=\"")).append(iString).append("\">").append(iString).append("</option>\r\n").toString());
            }

            result.append("</select>\r\n");
        } else
        {
            result.append((new StringBuilder("<select name=\"")).append(target).append("Hour\">\r\n").toString());
            result.append("<option value=\"--\">--</option>\r\n");
            for(int i = 0; i < 24; i++)
            {
                String iString = String.valueOf(i);
                iString = iString.length() != 2 ? "0".concat(iString) : iString;
                if(hour.equals(iString))
                    result.append((new StringBuilder("<option value=\"")).append(iString).append("\" selected>").append(iString).append("</option>\r\n").toString());
                else
                    result.append((new StringBuilder("<option value=\"")).append(iString).append("\">").append(iString).append("</option>\r\n").toString());
            }

            result.append((new StringBuilder("</select>:<select name=\"")).append(target).append("Min\">\r\n").toString());
            result.append("<option value=\"--\" >--</option>\r\n");
            for(int i = 0; i < 60; i++)
            {
                String iString = String.valueOf(i);
                iString = iString.length() != 2 ? "0".concat(iString) : iString;
                if(min.equals(iString))
                    result.append((new StringBuilder("<option value=\"")).append(iString).append("\" selected>").append(iString).append("</option>\r\n").toString());
                else
                    result.append((new StringBuilder("<option value=\"")).append(iString).append("\">").append(iString).append("</option>\r\n").toString());
            }

            result.append("</select>\r\n");
        }
        return result.toString();
    }

    public static void registerTime(Date date, String userID, String type)
    {
        date = date == null ? new Date() : date;
        TimeCardDAO.registerTimeCardDependOnTime(date, userID, type);
    }

    /**
     * 指定したユーザーのタイムカードを出力するメソッドです。
     * @param userID 指定したいユーザーのID
     * @param startDate 出力開始日
     * @param endDate 出力終了日
     */
    public static void createCSV(String userID, String startDate, String endDate) {
    	//引数チェック
    	if(userID == null) {
    		return;
    	}
    	if(!startDate.matches("^[12][\\d][3][01][\\d][0-3][\\d]$")) {
    		System.out.println("開始日の形式が不正です。YYYYMMDD形式で指定してください。");
    		return;
    	}
    	if(!endDate.matches("^[12][\\d][3][01][\\d][0-3][\\d]$")) {
    		System.out.println("終了日の形式が不正です。YYYYMMDD形式で指定してください。");
    		return;
    	}
    	//開始日と終了日を比較して、開始日が終了日よりも早いことを確かめます。
    	Calendar CS = Calendar.getInstance();
    	Calendar CE = Calendar.getInstance();
    	CS.set(Integer.parseInt(startDate.substring(0, 4)),Integer.parseInt(startDate.substring(4, 6)), Integer.parseInt(startDate.substring(6,8)));
    	CE.set(Integer.parseInt(endDate.substring(0, 4)),Integer.parseInt(endDate.substring(4, 6)), Integer.parseInt(endDate.substring(6,8)));
    	if(CE.compareTo(CS) == -1) {
    		System.out.println("開始日は終了日よりも前にはできません");
    		return;
    	}
    }

    /**
     * 1列目のセルを作成するメソッド
     * @param isFirst 指定日のうち1番始めの行ならTrue
     * @param date 表示させたい日付
     * @return 色を含むHTML
     */
    private static String make_1st_column(Date date, boolean isFirstRow) {
    	StringBuilder result = new StringBuilder("");
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int day = calendar.get(Calendar.DAY_OF_WEEK);

		//指定日の月・日を取得する
	    int month = calendar.get(Calendar.MONTH) + 1;
	    int day_of_month = calendar.get(Calendar.DATE);

	    if (isFirstRow) {
	    	//指定日が1日の場合
		    if(day_of_month == 1) {
		    	switch (day) {
				case Calendar.SUNDAY:
					result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#ffdbde\">")).append(month).append("/").append(day_of_month).append("(日)").toString());
					break;
		        case Calendar.MONDAY: // '\002'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).append(month).append("/").append(day_of_month).append("(月)").toString());
		            break;

		        case Calendar.TUESDAY: // '\003'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).append(month).append("/").append(day_of_month).append("(火)").toString());
		            break;

		        case Calendar.WEDNESDAY: // '\004'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).append(month).append("/").append(day_of_month).append("(水)").toString());
		            break;

		        case Calendar.THURSDAY: // '\005'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).append(month).append("/").append(day_of_month).append("(木)").toString());
		            break;

		        case Calendar.FRIDAY: // '\006'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).append(month).append("/").append(day_of_month).append("(金)").toString());
		            break;

		        case Calendar.SATURDAY: // '\007'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#d7eefb\">")).append(month).append("/").append(day_of_month).append("(土)").toString());
		            break;
				}
		    //1日以外
		    }else {
		    	switch (day) {
				case Calendar.SUNDAY:
					result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#ffdbde\">")).append(day_of_month).append("(日)").toString());
					break;
		        case Calendar.MONDAY: // '\002'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).append(day_of_month).append("(月)").toString());
		            break;

		        case Calendar.TUESDAY: // '\003'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).append(day_of_month).append("(火)").toString());
		            break;

		        case Calendar.WEDNESDAY: // '\004'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).append(day_of_month).append("(水)").toString());
		            break;

		        case Calendar.THURSDAY: // '\005'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).append(day_of_month).append("(木)").toString());
		            break;

		        case Calendar.FRIDAY: // '\006'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).append(day_of_month).append("(金)").toString());
		            break;

		        case Calendar.SATURDAY: // '\007'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#d7eefb\">")).append(day_of_month).append("(土)").toString());
		            break;
				}
		    }
		//2行目以降
		}else {
			//指定日が1日の場合
		    if(day_of_month == 1) {
		    	switch (day) {
				case Calendar.SUNDAY:
					result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#ffdbde\">")).toString());
					break;
		        case Calendar.MONDAY: // '\002'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).toString());
		            break;

		        case Calendar.TUESDAY: // '\003'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).toString());
		            break;

		        case Calendar.WEDNESDAY: // '\004'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).toString());
		            break;

		        case Calendar.THURSDAY: // '\005'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).toString());
		            break;

		        case Calendar.FRIDAY: // '\006'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).toString());
		            break;

		        case Calendar.SATURDAY: // '\007'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#d7eefb\">")).toString());
		            break;
				}
		    //1日以外
		    }else {
		    	switch (day) {
				case Calendar.SUNDAY:
					result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#ffdbde\">")).toString());
					break;
		        case Calendar.MONDAY: // '\002'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).toString());
		            break;

		        case Calendar.TUESDAY: // '\003'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).toString());
		            break;

		        case Calendar.WEDNESDAY: // '\004'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).toString());
		            break;

		        case Calendar.THURSDAY: // '\005'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).toString());
		            break;

		        case Calendar.FRIDAY: // '\006'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#EFEFEF\">")).toString());
		            break;

		        case Calendar.SATURDAY: // '\007'
		            result.append((new StringBuilder("<tr class=\"date\"><td bgcolor=\"#d7eefb\">")).toString());
		            break;
				}
		    }
		}

	    result.append("</td>\r\n");
    	return result.toString();
    }



    /**
     * 2列目のセルを作成するメソッド
     * @param date 指定日
     * @param userID ユーザーID
     * @param timeCard タイムカード
     * @return
     */
    private static String make_2nd_column(Date date, String userID, TimeCard timeCard) {
    	StringBuilder result = new StringBuilder("");
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int day = calendar.get(Calendar.DAY_OF_WEEK);

		//指定日の月・日を取得する
    	int year = calendar.get(Calendar.YEAR);
	    int month = calendar.get(Calendar.MONTH) + 1;
	    int day_of_month = calendar.get(Calendar.DATE);
	    String arrivalTime = timeCard.getArrivalTime();
	    //本日の情報を取得する
	    Date today = new Date();
	    calendar.setTime(today);
	    int year_of_today = calendar.get(Calendar.YEAR);
	    int month_of_today = calendar.get(Calendar.MONTH) + 1;
	    int day_of_today = calendar.get(Calendar.DATE);


	  //指定日と本日が同じかどうかで分類
	    if(year == year_of_today && month == month_of_today && day_of_month == day_of_today) {

	    	//開始時間が入力されている場合
	    	if (!arrivalTime.equals("")) {
	    		switch(day){
	            case Calendar.SUNDAY: // '\001'
	                result.append((new StringBuilder("<td bgcolor=\"#ffdbde\">")).append(arrivalTime).toString());
	                break;

	            case Calendar.MONDAY: // '\002'
	                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(arrivalTime).toString());
	                break;

	            case Calendar.TUESDAY: // '\003'
	                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(arrivalTime).toString());
	                break;

	            case Calendar.WEDNESDAY: // '\004'
	                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(arrivalTime).toString());
	                break;

	            case Calendar.THURSDAY: // '\005'
	                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(arrivalTime).toString());
	                break;

	            case Calendar.FRIDAY: // '\006'
	                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(arrivalTime).toString());
	                break;

	            case Calendar.SATURDAY: // '\007'
	                result.append((new StringBuilder("<td bgcolor=\"#d7eefb\">")).append(arrivalTime).toString());
	                break;
	            }
	            result.append("</td>\r\n");
	        //未入力の場合
			}else {
				switch(day){
                case 1: // '\001'
                    result.append((new StringBuilder("<td bgcolor=\"#ffdbde\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=arrival&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">開始</a></div>").toString());
                    break;

                case 2: // '\002'
                    result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=arrival&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">開始</a></div>").toString());
                    break;

                case 3: // '\003'
                    result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=arrival&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">開始</a></div>").toString());
                    break;

                case 4: // '\004'
                    result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=arrival&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">開始</a></div>").toString());
                    break;

                case 5: // '\005'
                    result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=arrival&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">開始</a></div>").toString());
                    break;

                case 6: // '\006'
                    result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=arrival&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">開始</a></div>").toString());
                    break;

                case 7: // '\007'
                    result.append((new StringBuilder("<td bgcolor=\"#d7eefb\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=arrival&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">開始</a></div>").toString());
                    break;
                }
                result.append("</td>\r\n");
			}
	    //指定日が本日ではない場合
	    }else {
	    	switch(day){
            case Calendar.SUNDAY: // '\001'
                result.append((new StringBuilder("<td bgcolor=\"#ffdbde\">")).append(arrivalTime).toString());
                break;

            case Calendar.MONDAY: // '\002'
                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(arrivalTime).toString());
                break;

            case Calendar.TUESDAY: // '\003'
                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(arrivalTime).toString());
                break;

            case Calendar.WEDNESDAY: // '\004'
                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(arrivalTime).toString());
                break;

            case Calendar.THURSDAY: // '\005'
                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(arrivalTime).toString());
                break;

            case Calendar.FRIDAY: // '\006'
                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(arrivalTime).toString());
                break;

            case Calendar.SATURDAY: // '\007'
                result.append((new StringBuilder("<td bgcolor=\"#d7eefb\">")).append(arrivalTime).toString());
                break;
            }
            result.append("</td>\r\n");
	    }
    	return result.toString();
    }

    /**
     * 3列目のセルを作成するメソッド
     * @param date 指定日
     * @param userID ユーザーID
     * @param timeCard タイムカード
     * @return
     */
    private static String make_3rd_column(Date date, String userID, TimeCard timeCard) {
    	StringBuilder result = new StringBuilder("");
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int day = calendar.get(Calendar.DAY_OF_WEEK);

		//指定日の月・日を取得する
    	int year = calendar.get(Calendar.YEAR);
	    int month = calendar.get(Calendar.MONTH) + 1;
	    int day_of_month = calendar.get(Calendar.DATE);
	    String leaveTime = timeCard.getLeaveTime();
	    //本日の情報を取得する
	    Date today = new Date();
	    calendar.setTime(today);
	    int year_of_today = calendar.get(Calendar.YEAR);
	    int month_of_today = calendar.get(Calendar.MONTH) + 1;
	    int day_of_today = calendar.get(Calendar.DATE);


	  //指定日と本日が同じかどうかで分類
	    if(year == year_of_today && month == month_of_today && day_of_month == day_of_today) {

	    	//終了時間が入力されている場合
	    	if (!leaveTime.equals("")) {
	    		isFinished = true;
	    		switch(day){
	            case Calendar.SUNDAY: // '\001'
	                result.append((new StringBuilder("<td bgcolor=\"#ffdbde\">")).append(leaveTime).toString());
	                break;

	            case Calendar.MONDAY: // '\002'
	                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(leaveTime).toString());
	                break;

	            case Calendar.TUESDAY: // '\003'
	                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(leaveTime).toString());
	                break;

	            case Calendar.WEDNESDAY: // '\004'
	                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(leaveTime).toString());
	                break;

	            case Calendar.THURSDAY: // '\005'
	                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(leaveTime).toString());
	                break;

	            case Calendar.FRIDAY: // '\006'
	                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(leaveTime).toString());
	                break;

	            case Calendar.SATURDAY: // '\007'
	                result.append((new StringBuilder("<td bgcolor=\"#d7eefb\">")).append(leaveTime).toString());
	                break;
	            }
	            result.append("</td>\r\n");
	        //未入力の場合
			}else {
				switch(day){
                case 1: // '\001'
                    result.append((new StringBuilder("<td bgcolor=\"#ffdbde\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=leave&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">終了</a></div>").toString());
                    break;

                case 2: // '\002'
                    result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=leave&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">終了</a></div>").toString());
                    break;

                case 3: // '\003'
                    result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=leave&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">終了</a></div>").toString());
                    break;

                case 4: // '\004'
                    result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=leave&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">終了</a></div>").toString());
                    break;

                case 5: // '\005'
                    result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=leave&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">終了</a></div>").toString());
                    break;

                case 6: // '\006'
                    result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=leave&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">終了</a></div>").toString());
                    break;

                case 7: // '\007'
                    result.append((new StringBuilder("<td bgcolor=\"#d7eefb\"><div class=\"button\"><a href=\"/SVD_IntraNet/TimeCardServlet?action=leave&year=")).append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\">終了</a></div>").toString());
                    break;
                }
                result.append("</td>\r\n");
			}
	    //指定日が本日ではない場合
	    }else {
	    	switch(day){
            case Calendar.SUNDAY: // '\001'
                result.append((new StringBuilder("<td bgcolor=\"#ffdbde\">")).append(leaveTime).toString());
                break;

            case Calendar.MONDAY: // '\002'
                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(leaveTime).toString());
                break;

            case Calendar.TUESDAY: // '\003'
                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(leaveTime).toString());
                break;

            case Calendar.WEDNESDAY: // '\004'
                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(leaveTime).toString());
                break;

            case Calendar.THURSDAY: // '\005'
                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(leaveTime).toString());
                break;

            case Calendar.FRIDAY: // '\006'
                result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\">")).append(leaveTime).toString());
                break;

            case Calendar.SATURDAY: // '\007'
                result.append((new StringBuilder("<td bgcolor=\"#d7eefb\">")).append(leaveTime).toString());
                break;
            }
            result.append("</td>\r\n");
	    }
    	return result.toString();
    }

    private static String make_4th_column(Date date, String userID, TimeCard timeCard) {
    	StringBuilder result = new StringBuilder("");
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int day = calendar.get(Calendar.DAY_OF_WEEK);

		//指定日の月・日を取得する
    	int year = calendar.get(Calendar.YEAR);
	    int month = calendar.get(Calendar.MONTH) + 1;
	    int day_of_month = calendar.get(Calendar.DATE);
	    switch(day){
        case 1: // '\001'
            result.append((new StringBuilder("<td bgcolor=\"#ffdbde\"><div class=\"plus\"><a href=\"/SVD_IntraNet/TimeCardModify?action=modify&timecardID=")).append(timeCard.getTimecardID()).append("&year=").append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\"><img border=\"0\" src=\"img/plus.png\" alt=\"Plus Image\"></img></a></div>").toString());
            break;

        case 2: // '\002'
            result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"plus\"><a href=\"/SVD_IntraNet/TimeCardModify?action=modify&timecardID=")).append(timeCard.getTimecardID()).append("&year=").append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\"><img border=\"0\" src=\"img/plus.png\" alt=\"Plus Image\"></img></a></div>").toString());
            break;

        case 3: // '\003'
            result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"plus\"><a href=\"/SVD_IntraNet/TimeCardModify?action=modify&timecardID=")).append(timeCard.getTimecardID()).append("&year=").append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\"><img border=\"0\" src=\"img/plus.png\" alt=\"Plus Image\"></img></a></div>").toString());
            break;

        case 4: // '\004'
            result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"plus\"><a href=\"/SVD_IntraNet/TimeCardModify?action=modify&timecardID=")).append(timeCard.getTimecardID()).append("&year=").append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\"><img border=\"0\" src=\"img/plus.png\" alt=\"Plus Image\"></img></a></div>").toString());
            break;

        case 5: // '\005'
            result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"plus\"><a href=\"/SVD_IntraNet/TimeCardModify?action=modify&timecardID=")).append(timeCard.getTimecardID()).append("&year=").append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\"><img border=\"0\" src=\"img/plus.png\" alt=\"Plus Image\"></img></a></div>").toString());
            break;

        case 6: // '\006'
            result.append((new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"plus\"><a href=\"/SVD_IntraNet/TimeCardModify?action=modify&timecardID=")).append(timeCard.getTimecardID()).append("&year=").append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\"><img border=\"0\" src=\"img/plus.png\" alt=\"Plus Image\"></img></a></div>").toString());
            break;

        case 7: // '\007'
            result.append((new StringBuilder("<td bgcolor=\"#d7eefb\"><div class=\"plus\"><a href=\"/SVD_IntraNet/TimeCardModify?action=modify&timecardID=")).append(timeCard.getTimecardID()).append("&year=").append(year).append("&month=").append(month).append("&date=").append(day_of_month).append("&userID=").append(userID).append("\"><img border=\"0\" src=\"img/plus.png\" alt=\"Plus Image\"></img></a></div>").toString());
            break;
        }
        result.append("</td>\r\n");
    	return result.toString();
    }

    private static String make_5th_column(Date date, TimeCard timeCard, String workingTime) {
    	StringBuilder result = new StringBuilder("");
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int day = calendar.get(Calendar.DAY_OF_WEEK);

	    switch(day){
	    case 1: // '\001'
            result.append(new StringBuilder("<td bgcolor=\"#ffdbde\"><div class=\"workingDuration\">" + workingTime + " </div>").toString());
            break;

        case 2: // '\002'
            result.append(new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"workingDuration\"> " + workingTime + " </div>").toString());
            break;

        case 3: // '\003'
            result.append(new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"workingDuration\"> " + workingTime + " </div>").toString());
            break;

        case 4: // '\004'
            result.append(new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"workingDuration\"> " + workingTime + " </div>").toString());
            break;

        case 5: // '\005'
            result.append(new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"workingDuration\"> " + workingTime + " </div>").toString());
            break;

        case 6: // '\006'
            result.append(new StringBuilder("<td bgcolor=\"#EFEFEF\"><div class=\"workingDuration\"> " + workingTime + " </div>").toString());
            break;

        case 7: // '\007'
            result.append(new StringBuilder("<td bgcolor=\"#d7eefb\"><div class=\"workingDuration\"> " + workingTime + " </div>").toString());
            break;
        }
        result.append("</td></tr>\r\n");
    	return result.toString();
    }
}
