package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.TimecardBean;
import model.TimecardWriter;

public class TimecardWriterTest {

	public static void main() {
		try {

			TimecardBean timecardBean1 = new TimecardBean();
			timecardBean1.setName("takafumi Sakakibara");
			timecardBean1.setDate("20190317");
			timecardBean1.setStartTime("03:14");
			timecardBean1.setEndTime("04:15");
//			TimecardBean timecardBean2 = new TimecardBean();
//			timecardBean2.setName("sakakibara222");
			List<TimecardBean> timecardBeans = new ArrayList<TimecardBean>();

			timecardBeans.add(timecardBean1);
//			timecardBeans.add(timecardBean2);
			TimecardWriter.write("sakakibara@starv-data.com", "20190101", "20190317",
					"C:\\Users\\atfam\\OneDrive\\Documents\\Star_View_Data\\Learnings\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\SVD_IntraNet\\csv\\timecard.csv");
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
