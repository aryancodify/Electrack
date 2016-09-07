package com.electra.processor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import com.electra.domain.AnomalyBean;
import com.electra.domain.UserBean;
import com.electra.domain.UserUnitBean;
import com.electra.repository.UnitsRepository;

public class AnomalyProcessor implements ItemProcessor<UserBean,AnomalyBean>{

	private UnitsRepository unitsRepository;
	private Calendar cal = Calendar.getInstance();
	public AnomalyProcessor(UnitsRepository unitsRepository) {
		this.unitsRepository = unitsRepository;
	}

	@Override
	public AnomalyBean process(UserBean item) throws Exception {
		AnomalyBean anomalyBean;
		Date currentDate = new Date();
		cal.setTime(currentDate);
		cal.add(Calendar.MINUTE, -30);
		List<UserUnitBean> units = unitsRepository.findUnitsBetween(item.getUserId(),cal.getTime(),currentDate);
		if(units==null || units.size()==0){
			anomalyBean = new AnomalyBean(item.getUserId(),item.getAddress());
			anomalyBean.setStatus("open");
			return anomalyBean;
		}
		else
			return null;
		
	}

}
