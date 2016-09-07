package com.electra.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electra.domain.UnitBean;
import com.electra.domain.UserBean;
import com.electra.domain.UserBillBean;
import com.electra.domain.UserUnitBean;
import com.electra.repository.BillRepository;
import com.electra.repository.UnitsRepository;
import com.electra.repository.UserRepository;

@Service
public class ElectraServiceImpl implements ElectraService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ElectraServiceImpl.class);
	@Autowired
	UnitsRepository unitsRepository;
	
	@Autowired
	BillRepository billRepository;
	
	@Autowired
	public UserRepository userRepository;
	
	public double getUnit(String userId,Date date){
		 // LocalDate date =LocalDateTime.now().toLocalDate().plusDays(1);
		  LOGGER.info("date "+date);
		  UserUnitBean userUnitBean=unitsRepository.getCurrentMonthUnit(userId,date);
		  if(userUnitBean!=null){
		   List<UnitBean> unitBeans=userUnitBean.getUnits();
		   Collections.sort(unitBeans, UnitBean.COMPARE_TIME);
		   if(!unitBeans.isEmpty()){
		    return unitBeans.get(unitBeans.size()-1).getUnit();
		   }
		  }
		  return 0;
		 }
	@Override
	public List<UserBillBean> getBill(String userId) {

		List<UserBillBean> getBill = billRepository.userBill(userId);
		if (getBill.size() > 5) {
			getBill = getBill.subList(0, 6);
			Collections.reverse(getBill);
			return getBill;
		} else {
			Collections.reverse(getBill);
			return getBill;
		}
	}
	
	@Override
	public List<UserBillBean> getDayData(String userId, int month , int year){
		
		UserBean userBean = userRepository.findOne(userId);
		if (userBean == null) {
			return null;
		}

		int day = new Double(userBean.getBillCycleDate()).intValue();
		
		LocalDate from = LocalDateTime.now().toLocalDate().withMonth(month).withYear(year).withDayOfMonth(day);
		
		LocalDate to = LocalDateTime.now().toLocalDate().withMonth(month+1).withYear(year).withDayOfMonth(day);
		
		List<UserUnitBean> getMonthUnit=unitsRepository.getMonthUnit(userId, from, to);
		
		List<UserBillBean> userBillBeans=new ArrayList<>();
		UserBillBean temp=new UserBillBean();
		for (UserUnitBean userUnitBean : getMonthUnit) {
			temp = calculation(userUnitBean, userUnitBean, false);
			temp.setTimestamp(userUnitBean.getDate());
			userBillBeans.add(temp);
		}
		return userBillBeans;
	}
	
	private UserBillBean calculation(UserUnitBean userUnitBeanFrom, UserUnitBean userUnitBeanto, boolean save) {

		double oldUnit = 0;
		double newUnit = 0;
		List<UnitBean> unitBeans = userUnitBeanFrom.getUnits();
		Collections.sort(unitBeans, UnitBean.COMPARE_TIME);
		if (!unitBeans.isEmpty()) {
			if(unitBeans.size()>1){
				oldUnit = unitBeans.get(1).getUnit();	
			}
			else
			{
				oldUnit = unitBeans.get(0).getUnit();
			}
			
		}

		unitBeans = userUnitBeanto.getUnits();
		Collections.sort(unitBeans, UnitBean.COMPARE_TIME);
		if (!unitBeans.isEmpty()) {
			newUnit = unitBeans.get(unitBeans.size() - 1).getUnit();
		}
		double totalUnit = newUnit - oldUnit;
		double totalAmount = totalUnit * 5;

		UserBillBean userBillBean = new UserBillBean(userUnitBeanFrom.getUserId(), totalAmount, false, totalUnit);
		userBillBean.setMainUnit(newUnit);
		if (save){
			billRepository.save(userBillBean);
		}
		return userBillBean;
	}
}
