// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   TimeCardDAO.java

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.DatabaseProp;

// Referenced classes of package dao:
//            TimeCard

public class TimeCardDAO {

	protected static final String SELECT = "select";

	protected static final String FROM = "from";

	protected static final String WHERE = "where";

	protected static final String TABLE = "timecard";

	protected static final String SPACE = " ";

	protected static final String COMMA = ",";

	private static final String COL_TIMECARDID = "timecardid";

	private static final String COL_USERID = "userid";

	private static final String COL_YEAR = "year";

	private static final String COL_MONTH = "month";

	private static final String COL_DATE = "date";

	private static final String COL_ARRIVALTIME = "arrivaltime";

	@Deprecated
	private static final String COL_GOOUTTIME = "goouttime";

	@Deprecated
	private static final String COL_GOBACKTIME = "gobacktime";

	private static final String COL_LEAVETIME = "leavetime";

	private static final String EQ_QE = "= ?";

	protected static final String AND = "and";

	private static final String ALL_COL = concatWithComma(//
			COL_TIMECARDID, //
			COL_USERID, //
			COL_YEAR, //
			COL_MONTH, //
			COL_DATE, //
			COL_ARRIVALTIME, //
			COL_GOOUTTIME, //
			COL_GOBACKTIME, //
			COL_LEAVETIME);

	protected static String concatWithSpace(String... string) {
		if (string == null) {
			return "";
		}
		return String.join(SPACE, string);
	}

	protected static String concatWithComma(String... string) {
		if (string == null) {
			return "";
		}
		return String.join(COMMA, string);
	}

	public TimeCardDAO() {
	}

	public static List<TimeCard> findTimeCardByUserIDANDDate(String userID, String year, String month, String date) {
		System.out.println(userID + ":" + year + month + date);
		Connection connection;
		TimeCard timeCard;
		connection = null;
		timeCard = null;
		String sql = concatWithSpace(//
				SELECT, //
				ALL_COL, //
				FROM, //
				TABLE, //
				WHERE, //
				COL_USERID, //
				EQ_QE, //
				AND, //
				COL_YEAR, //
				EQ_QE, //
				AND, //
				COL_MONTH, //
				EQ_QE, //
				AND, //
				COL_DATE, //
				EQ_QE);
		/** ロガー */
		final Logger log = LoggerFactory.getLogger(TimeCardDAO.class);
		log.info(sql);
		System.out.println(sql);
		List<TimeCard> timecards = new ArrayList<TimeCard>();
		try {
			Class.forName("org.h2.Driver");
			//本番環境用
//			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
//			"");
			//テスト用
			connection = DriverManager.getConnection("jdbc:h2:file:C:/data/example", "sa", "");
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userID);
			preparedStatement.setInt(2, Integer.parseInt(year));
			preparedStatement.setInt(3, Integer.parseInt(month));
			preparedStatement.setInt(4, Integer.parseInt(date));
			for (ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next();) {
				String timeCardID = resultSet.getString(COL_TIMECARDID);
				String arrivalTime = resultSet.getString(COL_ARRIVALTIME);
				String goOutTime = resultSet.getString(COL_GOOUTTIME);
				String goBackTime = resultSet.getString(COL_GOBACKTIME);
				String leaveTime = resultSet.getString(COL_LEAVETIME);
				timeCard = new TimeCard(timeCardID, userID, arrivalTime, goOutTime, goBackTime, leaveTime);
				timecards.add(timeCard);
			}
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return timecards;
	}

	public static TimeCard findTimeCardByTimeCardID(String userID, String timecardID) {
		Connection connection;
		TimeCard timeCard;
		connection = null;
		timeCard = null;

		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
			String sql = (new StringBuilder("SELECT * FROM TIMECARD WHERE USERID ='")).append(userID)
					.append("' AND TIMECARDID ='").append(timecardID).append("'").toString();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			for (ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next();) {
				String arrivalTime = resultSet.getString("ARRIVALTIME");
				String goOutTime = resultSet.getString("GOOUTTIME");
				String goBackTime = resultSet.getString("GOBACKTIME");
				String leaveTime = resultSet.getString("LEAVETIME");
				timeCard = new TimeCard(timecardID, userID, arrivalTime, goOutTime, goBackTime, leaveTime);
			}

		} catch (ClassNotFoundException e) {
			// TODO: handle exception
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return timeCard;
	}

	public static void registerTimeCardDependOnTime(Date date, String userID, String type) {
		Connection connection = null;
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(1);
			int month = calendar.get(2) + 1;
			int day = calendar.get(5);
			String SMonth = String.valueOf(month);
			SMonth = SMonth.length() == 1 ? SMonth : "0".concat(SMonth);
			String SDay = String.valueOf(day);
			SDay = SDay.length() == 1 ? SDay : "0".concat(SDay);
			List<TimeCard> timeCards = null;
			if (existsTimeCardDependOnTime(date, userID)) {
				timeCards = findTimeCardByUserIDANDDate(userID, String.valueOf(year), SMonth, SDay);

			} else {
				timeCards = new ArrayList<TimeCard>();
				timeCards.add(new TimeCard(userID, "", "", "", ""));
				String sql = (new StringBuilder(
						"INSERT INTO TIMECARD (USERID,YEAR,MONTH,DATE,ARRIVALTIME,GOOUTTIME,GOBACKTIME,LEAVETIME)\r\nVALUES ('"))
								.append(userID).append("','").append(year).append("','").append(SMonth).append("','")
								.append(SDay).append("','").append(timeCards.get(0).getArrivalTime()).append("','")
								.append(timeCards.get(0).getGoOutTime()).append("','")
								.append(timeCards.get(0).getGoBackTime()).append("','")
								.append(timeCards.get(0).getLeaveTime()).append("');").toString();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.executeUpdate();
			}
			//Listの最後を取得し、その終了時間が既に埋められていた場合、新たなタイムカードを作成し、その開始と終了を入力できるようにします。
			TimeCard timeCard = timeCards.get(timeCards.size() - 1);
			//終了時間が埋まっている場合
			if (!timeCard.getLeaveTime().equals("")) {
				System.out.println("新しいタイムカードを作成します。");
				timeCard = new TimeCard(userID, "", "", "", "");
				newTimeCard(timeCard, String.valueOf(year), SMonth, SDay);
				timeCards = findTimeCardByUserIDANDDate(userID, String.valueOf(year), SMonth, SDay);
				timeCard = timeCards.get(timeCards.size() - 1);
			}
			Date today = new Date();
			calendar.setTime(today);
			int hour = calendar.get(11);
			int min = calendar.get(12);
			String hString = String.valueOf(hour);
			hString = hString.length() != 1 ? hString : "0".concat(hString);
			String mString = String.valueOf(min);
			mString = mString.length() != 1 ? mString : "0".concat(mString);
			String now = hString.concat(":").concat(mString);
			System.out.println(now);
			String s;
			switch ((s = type).hashCode()) {
			default:
				break;

			case -1241591313:
				if (s.equals("goBack"))
					timeCard.setGoBackTime(now);
				break;

			case -734206983:
				if (s.equals("arrival"))
					timeCard.setArrivalTime(now);
				break;

			case 98509126:
				if (s.equals("goOut"))
					timeCard.setGoOutTime(now);
				break;

			case 102846135:
				if (s.equals("leave"))
					timeCard.setLeaveTime(now);
				break;
			}
			String sql = (new StringBuilder("UPDATE TIMECARD SET\r\nARRIVALTIME = '")).append(timeCard.getArrivalTime())
					.append("',").append("GOOUTTIME = '").append(timeCard.getGoOutTime()).append("',")
					.append("GOBACKTIME = '").append(timeCard.getGoBackTime()).append("',").append("LEAVETIME = '")
					.append(timeCard.getLeaveTime()).append("' WHERE ").append("USERID = '").append(userID)
					.append("' AND ").append("YEAR = ").append(year).append(" AND ").append("MONTH = ").append(month)
					.append(" AND ").append("DATE = ").append(day).append(" AND ").append("TIMECARDID = '")
					.append(timeCard.getTimecardID() + "'").toString();
			System.out.println(sql);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public static void updateTimeCard(TimeCard timecard) {
		Connection connection = null;
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
			String sql = (new StringBuilder("UPDATE TIMECARD SET\r\nARRIVALTIME = '")).append(timecard.getArrivalTime())
					.append("',\r\n").append("GOOUTTIME = '").append(timecard.getGoOutTime()).append("',\r\n")
					.append("GOBACKTIME = '").append(timecard.getGoBackTime()).append("',\r\n").append("LEAVETIME = '")
					.append(timecard.getLeaveTime()).append("' WHERE \r\n").append("TIMECARDID=")
					.append(timecard.getTimecardID()).toString();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public static boolean existsTimeCardDependOnTime(Date date, String userID) {
		Connection connection = null;
		try {
			ResultSet resultSet;
			Class.forName("org.h2.Driver");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(1);
			int month = calendar.get(2) + 1;
			int day = calendar.get(5);
			String SMonth = String.valueOf(month);
			SMonth = SMonth.length() == 1 ? SMonth : "0".concat(SMonth);
			String SDay = String.valueOf(day);
			SDay = SDay.length() == 1 ? SDay : "0".concat(SDay);
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
			String sql = (new StringBuilder("SELECT * FROM TIMECARD WHERE USERID ='")).append(userID)
					.append("' AND YEAR ='").append(year).append("' AND MONTH ='").append(month).append("' AND DATE ='")
					.append(day).append("'").toString();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (connection != null)
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				return true;
			}
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return false;
	}

	public static void newTimeCard(TimeCard timecard, String year, String month, String day) {
		Connection connection = null;
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
			String sql = (new StringBuilder(
					"INSERT INTO \r\nTIMECARD (\r\nUSERID,\r\nYEAR,\r\nMONTH,\r\nDATE,\r\nARRIVALTIME,\r\nGOOUTTIME,\r\nGOBACKTIME,\r\nLEAVETIME)\r\nVALUES(\r\n'"))
							.append(timecard.getUserID()).append("',\r\n").append("'").append(year).append("',\r\n")
							.append("'").append(month).append("',\r\n").append("'").append(day).append("',\r\n")
							.append("'").append(timecard.getArrivalTime()).append("',\r\n").append("'")
							.append(timecard.getGoOutTime()).append("',\r\n").append("'")
							.append(timecard.getGoBackTime()).append("',\r\n").append("'")
							.append(timecard.getLeaveTime()).append("')").toString();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public static String getTimeCardID(String userID, String year, String month, String date) {
		Connection connection;
		String result;
		connection = null;
		result = null;
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
			String sql = (new StringBuilder("SELECT * FROM TIMECARD WHERE USERID ='")).append(userID)
					.append("' AND YEAR ='").append(year).append("' AND MONTH ='").append(month).append("' AND DATE ='")
					.append(date).append("'").toString();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			for (ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next();)
				result = resultSet.getString("TIMECARDID");
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return result;
	}
}
