package converter;

import dao.AccountDAO;
import dao.TimeCard;
import model.TimecardBean;
import util.DayUtil;
import util.TimecardUtil;

/**
 * TimeCardクラスをTimecardBeanに変換するクラスです。
 * @author atfam
 *
 */
public class TimecardBeanConverter {

	//エンティティ→Beans
	public static TimecardBean converToTimecardBean(String date, String name, TimeCard timeCard) {

		TimecardBean timecardBean = new TimecardBean();
		if(timeCard == null || timeCard.getArrivalTime() == null) {
			timecardBean.setDate(date);
			timecardBean.setName(name);
			timecardBean.setStartTime("");
			timecardBean.setEndTime("");
			timecardBean.setWorkingTime(0);
			return timecardBean;
		}
		//開始時間
		System.out.println(timeCard.getArrivalTime());
		String[] dateArray = DayUtil.formatStr(timeCard.getArrivalTime());
		//日付
		timecardBean.setDate(date);
		timecardBean.setStartTime(DayUtil.parseToHHMM(dateArray));
		//終了時間
		dateArray = DayUtil.formatStr(timeCard.getLeaveTime());

		timecardBean.setEndTime(DayUtil.parseToHHMM(dateArray));
		timecardBean.setName(AccountDAO.findAccountByUserID(timeCard.getUserID()).getName());
		timecardBean
				.setWorkingTime(TimecardUtil.calculate_workingtime(timeCard.getArrivalTime(), timeCard.getLeaveTime()));

		return timecardBean;
	}

}
