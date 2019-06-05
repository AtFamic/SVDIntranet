package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class DayUtilTest {

	@Test
	public void YYYYMMDDのテスト() {
		try {
			String[] test20190530 = DayUtil.formatStr("20190530");
			sysout(test20190530);
			assertThat(test20190530[DayUtil.YEAR], is("2019"));
			assertThat(test20190530[DayUtil.YEAR], is("2019"));
			assertThat(test20190530[DayUtil.MONTH], is("05"));
			assertThat(test20190530[DayUtil.DATE], is("30"));
			assertThat(test20190530[DayUtil.DAY_OF_WEEK], is("木曜日"));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void YYYY_MM_DDのテスト() {
		try {
			String[] test2019_05_30 = DayUtil.formatStr("2019/05/30");
			sysout(test2019_05_30);
			assertThat(test2019_05_30[DayUtil.YEAR], is("2019"));
			assertThat(test2019_05_30[DayUtil.YEAR], is("2019"));
			assertThat(test2019_05_30[DayUtil.MONTH], is("05"));
			assertThat(test2019_05_30[DayUtil.DATE], is("30"));
			assertThat(test2019_05_30[DayUtil.DAY_OF_WEEK], is("木曜日"));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void 日付幅の出力のテスト() {
		try {
			List<String> test20190320to20190504 = DayUtil.dateList("20190320", "20190504");
			test20190320to20190504.stream().forEach(s -> System.out.println(s));

			List<String> test20180904to20190504 = DayUtil.dateList("20190320", "20190504");
			test20190320to20190504.stream().forEach(s -> System.out.println(s));

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	private void sysout(String[] result) {
		if (result == null) {
			System.out.println("NULL");
			return;
		}
		int i = 0;
		for (String tmp : result) {
			System.out.println(i + "個目： " + tmp);
		}
	}

}
