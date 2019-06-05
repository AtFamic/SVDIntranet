package util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.supercsv.prefs.CsvPreference;

import com.github.mygreen.supercsv.io.CsvAnnotationBeanReader;
import com.github.mygreen.supercsv.io.CsvAnnotationBeanWriter;

import dao.AccountDAO;
import dao.TimeCard;
import dao.TimeCardDAO;
import model.TimecardBean;

public class TimecardUtil {

	private static final String BR = System.lineSeparator();

	private static final String COMMA = ",";

	/**
	 * 開始時間から終了時間までの経過時間を計算します。
	 * @param start_time 開始時間
	 * @param end_time 終了時間
	 * @return 勤務時間
	 */
	public static int calculate_workingtime(String start_time, String end_time) {
		int result = 0;
		String[] start_array = DayUtil.formatStr(start_time);
		String[] end_array = DayUtil.formatStr(end_time);
		result += (Integer.parseInt(end_array[DayUtil.YEAR]) - Integer.parseInt(start_array[DayUtil.YEAR])) * 365 * 24 * 60;
		result += (Integer.parseInt(end_array[DayUtil.MONTH]) - Integer.parseInt(start_array[DayUtil.MONTH]))* 24 * 30 * 60;
		result += (Integer.parseInt(end_array[DayUtil.DATE]) - Integer.parseInt(start_array[DayUtil.DATE])) * 24 * 60;
		result += (Integer.parseInt(end_array[DayUtil.HOUR]) - Integer.parseInt(start_array[DayUtil.HOUR])) * 60;
		result += (Integer.parseInt(end_array[DayUtil.MIN]) - Integer.parseInt(start_array[DayUtil.MIN]));

		return result;

	}

	/**
	 * 指定したユーザーのタイムカードをCSV形式で出力します
	 * @param userID
	 * @param start YYYYMMDD形式で入力してください
	 * @param end YYYYMMDD形式で入力してください
	 * @param file_path ファイルの保存先を指定してください
	 */
	public static void write(String userID, String start, String end, String file_path)
			throws IOException, IllegalArgumentException {

		System.out.println(file_path);
		//		ICsvBeanWriter beanWriter = null;
		//		FileOutputStream fos = new FileOutputStream(new File("timecard.csv"));
		//		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		//		beanWriter = new CsvBeanWriter(osw, CsvPreference.EXCEL_PREFERENCE);
		//		beanWriter.writeHeader("START TIME");
		//		beanWriter.close();

		/*
		 * まずタイムカードを取得します。
		 * 各月の最終日を31日と規定し、それぞれの期間に対して各日で検索を掛けます。
		 * NULLの場合は一つだけ名前以外を空白としたものを含ませます。
		 */
		//入力値チェック
		if (start == null || end == null) {
			throw new IllegalArgumentException("開始日または終了日がNULLです");
		}
		if (!start.matches("[12]\\d{3}[01]\\d[0123]\\d")) {
			throw new IllegalArgumentException("開始日はYYYYMMDDの形式で入力してください");
		}
		if (!end.matches("[12]\\d{3}[01]\\d[0123]\\d")) {
			throw new IllegalArgumentException("終了日はYYYYMMDDの形式で入力してください");
		}
		if (Integer.parseInt(start) - Integer.parseInt(end) >= 0) {
			throw new IllegalArgumentException("開始日は終了日よりも早くできません");
		}

		List<TimecardBean> output = getTimecardBeans(userID, start, end);

		CsvAnnotationBeanWriter<TimecardBean> csvWriter = new CsvAnnotationBeanWriter<>(TimecardBean.class,
				Files.newBufferedWriter(new File(file_path).toPath(), Charset.forName("UTF-8")),
				CsvPreference.EXCEL_PREFERENCE);

		csvWriter.writeHeader();

		int size = output.size();
		System.out.println(size);
		for (int i = 0; i < size; i++) {

			//名前、日付、開始時間、終了時間、総勤務時間が正しいのかをチェックします。
			//不正な場合はIllegalArgumentExceptionを吐きます
			TimecardBean temp = output.get(i);

			String name = temp.getName();
			String date = temp.getDate();
			String startTime = temp.getStartTime();
			String endTime = temp.getEndTime();
			if (name == null) {
				throw new IllegalArgumentException("名前がNULLです");
			}
			if (date == null) {
				throw new IllegalArgumentException("日付がNULLです");
			}
			if (!date.matches("[12]\\d{3}[01]\\d[0123]\\d")) {
				throw new IllegalArgumentException("日付はYYYYMMDDの形式で入力してください");
			}
			if (startTime == null || endTime == null) {
				throw new IllegalArgumentException("開始時間および終了時間がNULLです");
			}
			if (!startTime.matches("\\d{2}[:]\\d{2}") && !startTime.equals("")) {
				throw new IllegalArgumentException("開始時間はHH:mmの形式で入力してください");
			}
			if (!endTime.matches("\\d{2}[:]\\d{2}") && !endTime.equals("")) {
				throw new IllegalArgumentException("終了時間はHH:mmの形式で入力してください");
			}
			temp.setWorkingTime(calculateWorkingTime(startTime, endTime));

			csvWriter.write(temp);
			csvWriter.flush();
		}
		csvWriter.flush();
		csvWriter.close();
	}

	public static void writeCSV(String userID, String startTime, String endTime, String filePath) throws IOException {
		//ファイルの存在確認
		if (!new File(filePath).exists()) {
			//ファイルが存在しない場合新しくファイルを作成する
			System.out.println("ファイル不存在");
			try {
				new File(filePath).createNewFile();
			} catch (IOException e) {
				throw new IOException("指定先のフォルダが存在していません。");
			}
		}
		//出力データ取得
		List<TimecardBean> output = getTimecardBeans(userID, startTime, endTime);


		CsvAnnotationBeanWriter<TimecardBean> csvWriter = new CsvAnnotationBeanWriter<>(TimecardBean.class,
				Files.newBufferedWriter(new File(filePath).toPath(), Charset.forName("Shift-JIS")),
				CsvPreference.EXCEL_PREFERENCE);
		csvWriter.writeAll(output);
		csvWriter.close();
	}

	/**
	 * ファイルパスに指定されたCSVを読み込み、文字列形式にして出力する
	 * @param filePath
	 * @return
	 */
	public static String readCSV(String filePath) throws IOException {
		StringBuilder result = new StringBuilder("");
		CsvAnnotationBeanReader<TimecardBean> csvReader = new CsvAnnotationBeanReader<>(TimecardBean.class,
				Files.newBufferedReader(new File(filePath).toPath(), Charset.forName("UTF-8")),
				CsvPreference.EXCEL_PREFERENCE);

		List<TimecardBean> list = new ArrayList<>();
		//ヘッダーの取得
		String[] headers = csvReader.getHeader(true);
		TimecardBean record = null;
		while ((record = csvReader.read()) != null) {
			list.add(record);
		}
		//ヘッダーの出力
		int size = headers.length;
		for (int i = 0; i < size; i++) {
			result.append(headers[i]);
			if (i == (size - 1)) {
				break;
			}
			result.append(COMMA);
		}
		result.append(BR);

		//内容の出力
		size = list.size();
		record = null;
		for (int i = 0; i < size; i++) {
			record = list.get(i);
			String name = record.getName();
			String date = record.getDate();
			String start = record.getStartTime();
			String end = record.getEndTime();
			int working_time = record.getWorkingTime();

			String output = name + COMMA + date + COMMA + start + COMMA + end + COMMA + working_time + BR;
			result.append(output);
		}

		csvReader.close();

		return result.toString();

	}

	/**
	 * BOMを取得するメソッド
	 * @return UTF-8のBOM
	 */
	public static String getBOM() {
		return "EF BB BF ";
	}

	/**
	 *指定した日付の間を含むTimecardをListで出力します
	 * @param userID
	 * @param start YYYYMMDD形式
	 * @param end
	 * @return
	 */
	private static List<TimecardBean> getTimecardBeans(String userID, String start, String end) {

		List<TimecardBean> result = new ArrayList<TimecardBean>();

		String sYear = start.substring(0, 4);
		String sMonth = start.substring(4, 6);
		String sDate = start.substring(6, 8);
		String eYear = end.substring(0, 4);
		String eMonth = end.substring(4, 6);
		String eDate = end.substring(6, 8);

		Calendar sCalendar = Calendar.getInstance();
		sCalendar.set(Integer.parseInt(sYear), Integer.parseInt(sMonth), Integer.parseInt(sDate));
		Calendar eCalendar = Calendar.getInstance();
		eCalendar.set(Integer.parseInt(eYear), Integer.parseInt(eMonth), Integer.parseInt(eDate));
		eCalendar.set(Calendar.HOUR, 23);
		eCalendar.set(Calendar.MINUTE, 59);

		while (sCalendar.before(eCalendar)) {
			List<TimeCard> timeCards = TimeCardDAO.findTimeCardByUserIDANDDate(userID,
					String.valueOf(sCalendar.get(Calendar.YEAR)), String.valueOf(sCalendar.get(Calendar.MONTH)),
					String.valueOf(sCalendar.get(Calendar.DATE)));
			if (timeCards == null) {
				continue;
			}
			int size = timeCards.size();
			for (int i = 0; i < size; i++) {
				String year = String.valueOf(sCalendar.get(Calendar.YEAR));
				String month = String.valueOf(sCalendar.get(Calendar.MONTH));
				if (month.length() == 1) {
					month = "0".concat(month);
				}
				String date = String.valueOf(sCalendar.get(Calendar.DATE));
				if (date.length() == 1) {
					date = "0".concat(date);
				}

				TimeCard timeCardTemp = timeCards.get(i);
				TimecardBean timecardBeanTemp = new TimecardBean();
				timecardBeanTemp.setName(AccountDAO.findAccountByUserID(timeCardTemp.getUserID()).getName());
				timecardBeanTemp.setDate(date);
				if (timeCardTemp.getArrivalTime().equals("") && !timeCardTemp.getLeaveTime().equals("")) {
					timeCardTemp.setArrivalTime(timeCardTemp.getLeaveTime());
				}
				if (!timeCardTemp.getArrivalTime().equals("") && timeCardTemp.getLeaveTime().equals("")) {
					timeCardTemp.setLeaveTime(timeCardTemp.getArrivalTime());
				}

				timecardBeanTemp.setStartTime(timeCardTemp.getArrivalTime());
				timecardBeanTemp.setEndTime(timeCardTemp.getLeaveTime());
				timecardBeanTemp.setDate(year.concat(month).concat(date));

				result.add(timecardBeanTemp);
			}
			sCalendar.add(Calendar.DATE, 1);
		}
		return result;

	}

	/**
	 * 指定した開始日と終了日までの全日程をキーにもち、その日付に対応するタイムカードを値に持つMapを返却します。
	 * @param userID ユーザID
	 * @param start 開始日
	 * @param end 終了日
	 * @return 開始日から終了日までの全日程に対応するタイムカード情報を保持したマップ
	 */
	private static Map<String, List<TimeCard>> getTimecardBeansMap(String userID, String start, String end) {
		//対象日付のリストを取得
		List<String> dateList = DayUtil.dateList(start, end);

		//Map<日付, List<TimecardBean>> を取得
		Map<String, List<TimeCard>> result = new HashMap<>();

		int size = dateList.size();
		for (int i = 0; i < size; i++) {
			String[] tmp = DayUtil.formatStr(dateList.get(i));
			result.put(dateList.get(i), TimeCardDAO.findTimeCardByUserIDANDDate(userID, tmp[DayUtil.YEAR],
					tmp[DayUtil.MONTH], tmp[DayUtil.DATE]));
		}
		return result;
	}

	private static int calculateWorkingTime(String startTime, String endTime) {
		if (startTime.equals("") && endTime.equals("")) {
			return 0;
		}
		String[] start = startTime.split(":");
		String[] end = endTime.split(":");

		int workStart = Integer.parseInt(start[0]) * 60 + Integer.parseInt(start[1]);
		int workEnd = Integer.parseInt(end[0]) * 60 + Integer.parseInt(end[1]);

		//日付をまたいだ場合
		if (workEnd < workStart) {
			workEnd += 60 * 24;
		}

		return workEnd - workStart;

		//		int duration = workEnd - workStart;
		//		int hours = duration / 60;
		//		int min = duration % 60;
		//		//HH:mm形式で出力
		//		return String.valueOf(hours).concat(":").concat(String.valueOf(min));
	}

	/**
	 * 本日の日付を選択済みにして、selectの選択を作成します。
	 * YYYYの形式で出力します
	 * @return
	 */
	public static String getYearHTML(String name) {
		StringBuilder result = new StringBuilder();
		Calendar today = Calendar.getInstance();

		result.append("<select name=\"" + name + "\">\r\n");
		for (int i = 1995; i < 2050; i++) {
			if (today.get(Calendar.YEAR) == i) {
				result.append("<option value=\"" + i + "\" selected>" + i + "</option>\r\n");
			} else {
				result.append("<option value=\"" + i + "\">" + i + "</option>\r\n");
			}
		}
		result.append("</select>\r\n");
		return result.toString();
	}

	/**
	 * 本日の日付を選択済みにして、selectの選択を作成します。
	 * MMの形式で出力します
	 * @return
	 */
	public static String getMonthHTML(String name) {
		StringBuilder result = new StringBuilder();
		Calendar today = Calendar.getInstance();

		result.append("<select name=\"" + name + "\">\r\n");
		for (int i = 1; i < 13; i++) {
			if (today.get(Calendar.MONTH) + 1 == i) {
				if (i < 10) {
					result.append("<option value=\"0" + i + "\" selected>" + i + "</option>\r\n");
				} else {
					result.append("<option value=\"" + i + "\" selected>" + i + "</option>\r\n");
				}
			} else {
				if (i < 10) {
					result.append("<option value=\"0" + i + "\" selected>" + i + "</option>\r\n");
				} else {
					result.append("<option value=\"" + i + "\">" + i + "</option>\r\n");
				}
			}
		}
		result.append("</select>");
		return result.toString();
	}

	/**
	 * 本日の日付を選択済みにして、selectの選択を作成します。
	 * ddの形式で出力します
	 * @return
	 */
	public static String getDateHTML(String name) {
		StringBuilder result = new StringBuilder();
		Calendar today = Calendar.getInstance();

		result.append("<select name=\"" + name + "\">");
		for (int i = 1; i < 32; i++) {
			if (today.get(Calendar.DATE) == i) {
				if (i < 10) {
					result.append("<option value=\"0" + i + "\" selected>" + i + "</option>\r\n");
				} else {
					result.append("<option value=\"" + i + "\" selected>" + i + "</option>\r\n");
				}
			} else {
				if (i < 10) {
					result.append("<option value=\"0" + i + "\" selected>" + i + "</option>\r\n");
				} else {
					result.append("<option value=\"" + i + "\">" + i + "</option>\r\n");
				}
			}
		}
		result.append("</select>");
		return result.toString();
	}
}
