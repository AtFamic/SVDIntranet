package util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
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

	@Test
	public void WriteCSV() {
		try {
			TimecardUtil.writeCSV("sakakibara", "20190501", "20190531", "C:\\Users\\atfam\\OneDrive\\Documents\\Star_View_Data\\Learnings\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\SVD_IntraNet\\csv\\timecard.csv");
		}catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
//	@Ignore
//	public void ReadCSV() {
//		try {
//			String result = TimecardUtil.readCSV(
//					"C:\\Users\\atfam\\OneDrive\\Documents\\Star_View_Data\\Learnings\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\SVD_IntraNet\\csv\\timecard.csv");
//			System.out.println("start");
//			System.out.println(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail("まだ実装されていません");
//		}
//
//	}

}
