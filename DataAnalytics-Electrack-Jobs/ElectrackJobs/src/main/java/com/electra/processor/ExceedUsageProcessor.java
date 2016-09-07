package com.electra.processor;

import java.util.Calendar;
import java.util.Date;

import org.springframework.batch.item.ItemProcessor;

import com.electra.domain.DayByDayUsage;
import com.electra.domain.UserBean;
import com.electra.repository.UnitsRepository;
import com.electra.service.ElectraService;


public class ExceedUsageProcessor implements ItemProcessor<UserBean,DayByDayUsage>{

	private static Calendar cal = Calendar.getInstance();
	
	UnitsRepository unitsRepos;
	
	ElectraService electraService;
	
	
	public ExceedUsageProcessor(UnitsRepository unitsRepos,
			ElectraService electraService) {
		super();
		this.unitsRepos = unitsRepos;
		this.electraService = electraService;
	}


	@Override
	public DayByDayUsage process(UserBean user) throws Exception {
		DayByDayUsage dayByDayUsage;
		double loadLimit = user.getLoadLimit();
		int billCycleDay = user.getBillCycleDate();
		int todayDate = cal.get(Calendar.DAY_OF_MONTH);
		int currentMonth = cal.get(Calendar.MONTH);
		int month = (todayDate>=billCycleDay)?currentMonth:currentMonth-1;
		cal.set(Calendar.YEAR, month, billCycleDay);
		Date billCycleDate = cal.getTime();
		double totalConsumptionTillCycle = electraService.getUnit(user.getUserId(), billCycleDate);
		double totalConsumptionTillToday = electraService.getUnit(user.getUserId(), new Date());
		double consumptionThisMonth = totalConsumptionTillToday - totalConsumptionTillCycle;
		dayByDayUsage = new DayByDayUsage(user.getUserId(), consumptionThisMonth, loadLimit, user.getDeveiceId());
		return dayByDayUsage;
	}

}
