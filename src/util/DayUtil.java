package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import enumurated.DateEnum;

/**
 * 日付情報に対応するUtilを集めたクラス
 * @author atfam
 *
 */
public class DayUtil {

	/** ロガー */
	private static final Logger log = LoggerFactory.getLogger(DayUtil.class);

	/** 配列数 */
	private static int arrayIndex = 6;
	/** 日付のフォーマット */
	/** YYYYMMDD */
	public static final String YYYYMMDD = "yyyyMMdd";
	/** YYYY/MM/DD */
	public static final String YYYY_MM_DD = "yyyy/MM/dd";
	/** YYYYMMDDHHmm */
	public static final String YYYYMMDDHHmm = "yyyyMMddHHmm";
	/** YYYY/MM/DD HH:mm */
	public static final String YYYY_MM_DD_HH_mm = "yyyy/MM/dd HH:mm";
	/** HH:mm */
	public static final String HH_mm = "HH:mm";
	/** HHmm */
	public static final String HHmm = "HHmm";


	public static final int YEAR = 0;
	public static final int MONTH = 1;
	public static final int DATE = 2;
	public static final int HOUR = 3;
	public static final int MIN = 4;
	public static final int DAY_OF_WEEK = 5;

	/**
	 * 与えられた引数の日付を分析し、年・月・日付・時間・分・曜日に分解して、配列で返却します。
	 * なお、年は4桁、曜日は数字1桁、それ以外は2桁になるよう調節して返却します。
	 * @param date 日付を表す文字列
	 * @return 年・月・日付・時間・分が順に入った文字列の配列
	 */
	public static String[] formatStr(String date) throws IllegalArgumentException {
		if(date.equals("") || date == null) {
			return null;
		}
		SimpleDateFormat format = null;
		//dateの形でフォーマットを推測する
		//YYYYMMDD
		if (date.matches("^[\\d]{8}$")) {
			format = new SimpleDateFormat(YYYYMMDD);
		}
		//YYYY/MM/DD
		if (date.matches("^[\\d]{4}[/][\\d]{2}[/][\\d]{2}$")) {
			format = new SimpleDateFormat(YYYY_MM_DD);
		}
		//YYYYMMDDHHmm
		if (date.matches("^[\\d]{4}[\\d]{2}[\\d]{2}[\\d]{2}[\\d]{2}$")) {
			format = new SimpleDateFormat(YYYYMMDDHHmm);
		}
		//YYYY/MM/DD HH:mm
		if (date.matches("^[\\d]{4}[/][\\d]{2}[/][\\d]{2}[\\s][\\d]{2}[:][\\d]{2}$")) {
			format = new SimpleDateFormat(YYYY_MM_DD_HH_mm);
		}
		//時間形式の場合は別対応
		// HH:mm
		if(date.matches("^[\\d]{2}[:][\\d]{2}$")) {
			format = new SimpleDateFormat(HH_mm);
			String[] result = new String[6];
			String[] tmp = date.split(":");
			result[HOUR] = tmp[0];
			result[MIN] = tmp[1];
			return result;
		}
		//HHmm
		if(date.matches("^[\\d]{2}[\\d]{2}$")) {
			format = new SimpleDateFormat(HHmm);
			String[] result = new String[6];
			result[HOUR] = date.substring(0, 2);
			result[MIN] = date.substring(2,4);
			return result;
		}

		Calendar calendar = Calendar.getInstance();
		//存在しない日付でもエラーが発生。
		format.setLenient(false);
		try {
			Date tmpDate = format.parse(date);
			calendar.setTime(tmpDate);
		} catch (ParseException e) {
			System.out.println("サポートされていない日付形式です。");
			log.error("サポートされていない日付形式です。");
			e.printStackTrace();
			throw new IllegalArgumentException("サポートされていない日付形式です。");
		} catch (NullPointerException e) {
			System.err.println("NullPointerException");
			log.error("NullPointerException");
			e.printStackTrace();
			throw new IllegalArgumentException("サポートされていない日付形式です。");

		}

		String[] result = new String[6];
		result[YEAR] = String.valueOf(calendar.get(Calendar.YEAR));
		//月はプラス1しておく
		result[MONTH] = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		result[DATE] = String.valueOf(calendar.get(Calendar.DATE));
		result[HOUR] = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		result[MIN] = String.valueOf(calendar.get(Calendar.MINUTE));
		result[DAY_OF_WEEK] = getDayOfTheWeek(calendar.get(Calendar.DAY_OF_WEEK));

		return array2Digits(result);

	}

	/**
	 * 開始日から終了日までの日付のリストを返却します。
	 * 日付の形式はYYYYMMDDで返却されます。
	 * @param startDate 開始日
	 * @param endDate 終了日
	 * @return YYYYMMDD形式の日付の配列
	 */
	public static List<String> dateList(String startDate, String endDate) {
		List<String> result = new ArrayList<>();
		//カレンダー初期化
		String[] startArray = formatStr(startDate);
		String[] endArray = formatStr(endDate);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(startArray[YEAR]), Integer.parseInt(startArray[MONTH]) - 1,
				Integer.parseInt(startArray[DATE]), Integer.parseInt(startArray[HOUR]), Integer.parseInt(startArray[MIN]));
		result.add(parseFromStringArray(startArray));
		while (true) {
			//一日進める
			calendar.add(Calendar.DATE, 1);
			String[] tmpArray = parseFromCalendar(calendar);
			result.add(parseFromStringArray(tmpArray));
			//最終日と一致したら脱出
			if(parseFromStringArray(tmpArray).equals(parseFromStringArray(endArray))) {
				break;
			}
		}
		return result;
	}

	/**
	 * カレンダー型から既定の文字列日時に変換します
	 * @param calendar
	 * @return
	 */
	public static String[] parseFromCalendar(Calendar calendar) {
		String[] strings = new String[arrayIndex];
		strings[YEAR] = String.valueOf(calendar.get(Calendar.YEAR));
		strings[MONTH] = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		strings[DATE] = String.valueOf(calendar.get(Calendar.DATE));
		strings[HOUR] = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		strings[MIN] = String.valueOf(calendar.get(Calendar.MINUTE));
		strings[DAY_OF_WEEK] = getDayOfTheWeek(calendar.get(Calendar.DAY_OF_WEEK));
		return array2Digits(strings);
	}

	/**
	 * 文字列配列から文字列に変換します。指定がない場合はYYYYMMDD形式で出力します。
	 * @param array
	 * @return
	 */
	public static String parseFromStringArray(String[] array) {
		return array[YEAR] + array[MONTH] + array[DATE];
	}

	/**
	 * 文字列配列から文字列に変換します。指定がない場合はYYYYMMDD形式で出力します。
	 * @param array
	 * @return
	 */
	public static String parseFromStringArray(String[] array, DateEnum format) throws ParseException {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.YYYYMMDD.getFormatStr());
		Date date = simpleDateFormat.parse(parseFromStringArray(array));
		return new SimpleDateFormat(format.getFormatStr()).format(date);
	}

	/**
	 * 文字列配列から時間を生成します。指定がない場合はHH:mm形式で出力します。
	 * @param array
	 * @return
	 */
	public static String parseToHHMM(String[] array) {
		if(array == null || array.length < 2) {
			return "N/A";
		}
		return array[DayUtil.HOUR] + ":" + array[DayUtil.MIN];
	}

	/**
	 * 現在の曜日を返します。
	 * @return	現在の曜日
	 */
	public static String getDayOfTheWeek(int dayOfWeek) {
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			return "日曜日";
		case Calendar.MONDAY:
			return "月曜日";
		case Calendar.TUESDAY:
			return "火曜日";
		case Calendar.WEDNESDAY:
			return "水曜日";
		case Calendar.THURSDAY:
			return "木曜日";
		case Calendar.FRIDAY:
			return "金曜日";
		case Calendar.SATURDAY:
			return "土曜日";
		}
		throw new IllegalStateException();
	}

	/**
	 * 与えられた文字列配列のうち1桁のものがあると、先頭に0を付けて2桁にして返す
	 * @param tmp
	 * @return
	 */
	private static String[] array2Digits(String[] tmp) {
		String[] result = new String[tmp.length];
		int i = 0;
		for (String str : tmp) {
			result[i] = changeTo2digits(str);
			i++;
		}
		return result;
	}

	/**
	 * 文字列を2桁にして返却します。
	 * @param str 文字列
	 * @return 1桁の場合のみ0を先頭につけて2桁に変換
	 */
	private static String changeTo2digits(String str) {
		return str.length() == 1 ? "0".concat(str) : str;
	}
}
