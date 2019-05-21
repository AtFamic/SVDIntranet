package model;

import com.github.mygreen.supercsv.annotation.CsvBean;
import com.github.mygreen.supercsv.annotation.CsvColumn;

//タイムカード用のカラムの作成
@CsvBean(header=true)
public class TimecardBean {

	@CsvColumn(number = 1, label = "Name")
	private String name;

	@CsvColumn(number = 2, label = "Date")
	private String date;

	@CsvColumn(number = 3, label = "Start")
	private String startTime;

	@CsvColumn(number = 4, label = "End")
	private String endTime;

	@CsvColumn(number = 5, label = "Working Time")
	private int workingTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(int workingTime) {
		this.workingTime = workingTime;
	}

}
