package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.DatabaseProp;
import model.TimecardBean;
import util.DayUtil;
import util.TimecardUtil;

// Referenced classes of package dao:
//            Task

public class TaskDAO extends AbstractDao<TimecardBean> {
	/** テーブル名 */
	private static final String TABLE = "task";

	/** カラム名 */
	private static final String COL_YEAR = "year";
	private static final String COL_MONTH = "month";
	private static final String COL_DATE = "date";
	private static final String COL_USERID = "userid";
	private static final String COL_TITLE = "title";
	private static final String COL_STARTTIME = "starttime";
	private static final String COL_ENDTIME = "endtime";
	private static final String COL_ISPUBLIC = "ispublic";
	private static final String COL_COLOR = "color";
	private static final String COL_CONTENT = "content";
	/** 全カラム */
	private final String ALL_COL = concatCommandsByCOMMA(COL_YEAR, COL_MONTH, COL_DATE, COL_USERID, COL_TITLE,
			COL_STARTTIME, COL_ENDTIME, COL_ISPUBLIC, COL_COLOR, COL_CONTENT);

	@SuppressWarnings("unchecked")
	@Override
	protected TimecardBean getModel(ResultSet resultSet) throws SQLException {
		String name;
		String start_time;
		String end_time;
		int working_time;

		name = AccountDAO.findAccountByUserID(resultSet.getString(COL_USERID)).getName();
		start_time = resultSet.getString(COL_STARTTIME);
		end_time = resultSet.getString(COL_ENDTIME);
		//勤務時間の計算
		working_time = TimecardUtil.calculate_workingtime(start_time, end_time);
		TimecardBean timecardBean = new TimecardBean();
		timecardBean.setName(name);
		timecardBean.setStartTime(start_time);
		timecardBean.setEndTime(end_time);
		timecardBean.setWorkingTime(working_time);
		return timecardBean;
	}


	public TaskDAO() {
		//テーブル名定義
		super("task");
	}

	public static void newTask(Task task) {
		Connection connection = null;
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
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
			sql = (new StringBuilder(String.valueOf(sql))).append("VALUES('").append(year).append("','").append(month)
					.append("','").append(date).append("','").append(userID).append("','").append(title).append("','")
					.append(startTime).append("','").append(endTime).append("','").append(isPublic).append("','")
					.append(color).append("','").append(content).append("');").toString();
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

	public static void editTask(Task task) {
		Connection connection = null;
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
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
			String sql = (new StringBuilder("UPDATE TASK SET  MONTH =")).append(year).append(",MONTH=").append(month)
					.append(",DATE=").append(date).append(",USERID=").append(userID).append(",TITLE=").append(title)
					.append(",STARTTIME=").append(startTime).append(",ENDTIME=").append(endTime).append(",ISPUBLIC=")
					.append(isPublic).append(",COLOR=").append(color).append(",CONTENT=").append(content)
					.append(" WHERE TASKID =").append(taskID).toString();
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

	public static Task findTaskByTaskID(String taskID) {
		Connection connection;
		Task task;
		connection = null;
		task = null;
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
			String sql = (new StringBuilder("SELECT * FROM TASK WHERE TASKID ='")).append(taskID).append("'")
					.toString();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			for (ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next();) {
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
		return task;
	}

	/**
	 * 指定したユーザIDのTimecardBeanを取得するメソッドです。
	 * 引数に与えた日付のリストに対応するタイムカードを検索します。
	 * @param userID ユーザID
	 * @param dateList 対象日付
	 * @return 日付に対応するタイムカードを保持するMap
	 */
	public Map<String, List<TimecardBean>> selectTimeBeans(String userID, List<String> dateList) {
		Connection connection;
		Map<String, List<TimecardBean>> result = new HashMap<>();
		connection = null;
		//SQL
		//setStringがバグるのでここでユーザIDを代入
		userID = "\'" + userID + "\'";
		String sql = concatCommandsBySpace(SELECT, ALL_COL, FROM, TABLE, WHERE, COL_USERID, EQ, userID,
				AND, COL_YEAR, EQ_QE, AND, COL_MONTH, EQ_QE, AND, COL_DATE, EQ_QE);
		System.out.println("SQL:" + sql);
		try {
			//DB接続
			Class.forName("org.h2.Driver");

			//テスト用
//			connection = DriverManager.getConnection("jdbc:h2:file:C:/data/example", "sa",
//					"");

			//本番環境用
						connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
								"");

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			int size = dateList.size();
			for (int i = 0; i < size; i++) {
				//指定日を取得
				String[] tmp = DayUtil.formatStr(dateList.get(i));
				//				preparedStatement.setString(1, userID);
				preparedStatement.setInt(1, Integer.parseInt(tmp[DayUtil.YEAR]));
				preparedStatement.setInt(2, Integer.parseInt(tmp[DayUtil.MONTH]));
				preparedStatement.setInt(3, Integer.parseInt(tmp[DayUtil.DATE]));
				List<TimecardBean> timecardBeanList = getElements(preparedStatement.executeQuery());
				result.put(dateList.get(i), timecardBeanList);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		//マップを日付で昇順にする
		result = result.entrySet().stream()
                .sorted(Map.Entry.<String, List<TimecardBean>>comparingByKey()
                .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return result;
	}

	public static List findTasksByMonth(String year, String month) {
		Connection connection;
		List tasks;
		connection = null;
		tasks = new ArrayList();
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
			String sql = (new StringBuilder("SELECT * FROM TASK WHERE YEAR ='")).append(year).append("' AND MONTH ='")
					.append(month).append("'").toString();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			Task task;
			for (ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next(); tasks.add(task)) {
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
		return tasks;
	}

	public static List findTasksByDate(String date) {
		Connection connection;
		List tasks;
		connection = null;
		tasks = new ArrayList();
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
			String year = date.substring(0, 4);
			String month = date.substring(4, 6);
			month = String.valueOf(Integer.parseInt(month));
			String day = date.substring(6, 8);
			day = String.valueOf(Integer.parseInt(day));
			String sql = (new StringBuilder("SELECT * FROM TASK WHERE YEAR ='")).append(year).append("' AND MONTH ='")
					.append(month).append("' AND DATE ='").append(day).append("'").toString();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			Task task;
			for (ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next(); tasks.add(task)) {
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
		return tasks;
	}

	public static List findTasksByDateANDUserID(int year, int month, int date, String userID) {
		Connection connection;
		List tasks;
		connection = null;
		tasks = new ArrayList();
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
			String sql = (new StringBuilder("SELECT * FROM TASK WHERE YEAR ='")).append(year).append("' AND MONTH ='")
					.append(month).append("' AND DATE ='").append(date).append("' AND USERID ='").append(userID)
					.append("'").toString();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			Task task;
			for (ResultSet resultSet = preparedStatement.executeQuery(); resultSet.next(); tasks.add(task)) {
				String taskID = resultSet.getString("TASKID");
				String title = resultSet.getString("TITLE");
				String startTime = resultSet.getString("STARTTIME");
				String endTime = resultSet.getString("ENDTIME");
				String isPublic = resultSet.getString("ISPUBLIC");
				String color = resultSet.getString("COLOR");
				String content = resultSet.getString("Content");
				task = new Task(taskID, String.valueOf(year), String.valueOf(month), String.valueOf(date), userID,
						title, startTime, endTime, isPublic, color, content);
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
			connection = DriverManager.getConnection(DatabaseProp.getDatabasePath(), DatabaseProp.getDatabaseUser(),
					"");
			String sql = (new StringBuilder("DELETE FROM TASK WHERE TASKID ='")).append(taskID)
					.append("' AND USERID ='").append(userID).append("'").toString();
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
}
