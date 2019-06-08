package dao;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import model.TimecardBean;
import util.DayUtil;

public class TaskDAOTest {
	/** テスト内で用いるDao */
	TaskDAO taskDao;
	/** 日付のリスト */
	List<String> dateList;

	@Before
	public void setUp() {
		taskDao = new TaskDAO();
		//日付のリストの作成
		dateList = DayUtil.dateList("20190106", "20190209");
	}

	@Test
	public void マップ型の値を返却するメソッドのテスト() {
		try {
			Map<String, List<TimecardBean>> test1 = taskDao.selectTimeBeans("sakakibara@starv-data.com", dateList);
			sysoutMap(test1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	private void sysoutMap(Map<String, List<TimecardBean>> map) {
		Iterator<String> iterator = map.keySet().iterator();
		while(iterator.hasNext()) {
			String date = iterator.next();
			System.out.println(date);
			List<TimecardBean> timecardBeans = map.get(date);
			Iterator<TimecardBean> iterator2 = timecardBeans.iterator();
			while(iterator2.hasNext()) {
				TimecardBean timecardBean = iterator2.next();
				System.out.println(date + ":"  + timecardBean.getName() + ", " + timecardBean.getStartTime());
			}

		}
	}

}
