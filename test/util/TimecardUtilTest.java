package util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import model.TimecardBean;

public class TimecardUtilTest {

	@Before
	public void setUp() {
		List<TimecardBean> list = new ArrayList<TimecardBean>();
		TimecardBean timecardBean = new TimecardBean();
		String name = "name";
		String date = "date";
		String start = "start";
		String end = "end";
		int workingTime = 60;
		timecardBean.setName(name);
		timecardBean.setDate(date);
		timecardBean.setStartTime(start);
		timecardBean.setEndTime(end);
		timecardBean.setWorkingTime(workingTime);
		list.add(timecardBean);

	}

	@Ignore
	public void WriteCSV() {
		try {
			TimecardUtil.writeCSV("sakakibara@starv-data.com", "20190101", "20190531",
					"C:\\Users\\atfam\\OneDrive\\Documents\\Star_View_Data\\Learnings\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\SVD_IntraNet\\csv\\");
			System.out.println("Finished");
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void ReadCSV() {
		try {
			String result = TimecardUtil.readCSV(
					"C:\\Users\\atfam\\OneDrive\\Documents\\Star_View_Data\\Learnings\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\SVD_IntraNet\\csv\\",
					"sakakibara@starv-data.com");
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
			fail("まだ実装されていません");
		}

	}

}
