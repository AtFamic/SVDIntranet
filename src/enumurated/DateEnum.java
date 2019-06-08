package enumurated;

/**
 * 日付の形式の列挙
 * @author atfam
 *
 */
public enum DateEnum {
	/** 日付のフォーマット */
	/** YYYYMMDD */
	YYYYMMDD("yyyyMMdd"), //
	/** YYYY/MM/DD */
	YYYY_MM_DD("yyyy/MM/dd"), //
	/** YYYYMMDDHHmm */
	YYYYMMDDHHmm("yyyyMMddHHmm"), //
	/** YYYY/MM/DD HH:mm */
	YYYY_MM_DD_HH_mm("yyyy/MM/dd HH:mm"),//
	//日付に関する文字列
	YEAR(0),//
	MONTH(1),//
	DATE(2),//
	HOUR(3),//
	MIN(4),//
	DAY_OF_WEEK(5);

	private String formatStr;
	private int num;

	private DateEnum(String formatStr) {
		this.formatStr = formatStr;
	}
	private DateEnum(int num) {
		this.num = num;
	}
	public String getFormatStr() {
		return formatStr;
	}
	public int getNum() {
		return num;
	}

}
