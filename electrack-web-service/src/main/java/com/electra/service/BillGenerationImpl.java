/**
 * 
 */
package com.electra.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.electra.domain.UnitBean;
import com.electra.domain.UserBean;
import com.electra.domain.UserBillBean;
import com.electra.domain.UserUnitBean;
import com.electra.exceptionhandler.NotFoundException;
import com.electra.notification.PushNotification;
import com.electra.repository.BillRepository;
import com.electra.repository.UnitsRepository;
import com.electra.repository.UserRepository;
import com.electra.utils.ElectraUtils;

/**
 * @author sahu
 *
 */
@Service
@Component
public class BillGenerationImpl implements BillGeneration {

	private static final Logger LOGGER = LoggerFactory.getLogger(BillGenerationImpl.class);

	@Autowired
	private BillRepository billRepository;

	@Autowired
	public UnitsRepository unitsRepository;

	@Autowired
	public UserRepository userRepository;

	@Override
	@Scheduled(cron="0 30 2 * * ?")
	public void billGenerate() {
		int day = LocalDateTime.now().getDayOfMonth();
		List<UserBean> userBeans = userRepository.getTodayCycleUser(day);

		LocalDate from = ElectraUtils.getDate().minusMonths(1);
		LocalDate to = ElectraUtils.getDate();

		for (UserBean userBean : userBeans) {

			UserUnitBean userUnitBeanFrom = unitsRepository.getCurrentMonthUnit(userBean.getUserId(), from);
			if (userUnitBeanFrom != null) {
				UserUnitBean userUnitBeanto = unitsRepository.getCurrentMonthUnit(userBean.getUserId(), to);
				if (userUnitBeanto != null) {
					UserBillBean result =calculation(userUnitBeanFrom, userUnitBeanto, true);
					try {
						PushNotification.send("Dear Customer, Your Bill amount for "+userUnitBeanto.getDate()+" is generated for an ammount INR "+result.getTotalAmount()+" with due date "+result.getDueDate(), userBean.getDeveiceId());	
					} catch (Exception e) {
					}
				}
			} else {
				userUnitBeanFrom = unitsRepository.getMonthUnit(userBean.getUserId(), from);
				if (userUnitBeanFrom != null) {
					UserUnitBean userUnitBeanto = unitsRepository.getCurrentMonthUnit(userBean.getUserId(), to);
					if (userUnitBeanto != null) {
						UserBillBean result =calculation(userUnitBeanFrom, userUnitBeanto, true);
						try {
							PushNotification.send("Dear Customer, Your Bill amount for "+userUnitBeanto.getDate()+" is generated for an ammount INR "+result.getTotalAmount()+" with due date "+result.getDueDate(), userBean.getDeveiceId());
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

	private UserBillBean calculation(UserUnitBean userUnitBeanFrom, UserUnitBean userUnitBeanto, boolean save) {

		double oldUnit = 0;
		double newUnit = 0;
		List<UnitBean> unitBeans = userUnitBeanFrom.getUnits();
		Collections.sort(unitBeans, UnitBean.COMPARE_TIME);
		if (!unitBeans.isEmpty()) {
			if(unitBeans.size()>=2){
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

		if (save){
			billRepository.save(userBillBean);
		}
		return userBillBean;
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
	public void payBill(String userId, String id) {
		UserBillBean userBillBean = billRepository.findOne(id);
		if (userBillBean == null) {
			LOGGER.error("Bill Not Found");
			throw new NotFoundException("Bill Id NOT FOUND");
		}
		userBillBean.setPaid(true);
		billRepository.save(userBillBean);
	}

	@Override
	public UserBillBean getCurrentMonthBill(String userId) {
		UserBean userBean = userRepository.findOne(userId);
		if (userBean == null) {
			throw new NotFoundException("User Not Found");
		}

		int day = new Double(userBean.getBillCycleDate()).intValue();
		LocalDate from = LocalDateTime.now().toLocalDate().withDayOfMonth(day);

		LocalDate to = ElectraUtils.getDate();

		UserUnitBean userUnitBeanFrom = unitsRepository.getMonthUnit(userBean.getUserId(), from);
		if (userUnitBeanFrom != null) {
			UserUnitBean userUnitBeanto = unitsRepository.getCurrentMonthUnit(userBean.getUserId(), to);
			if (userUnitBeanto != null) {
				UserBillBean userBillBean =calculation(userUnitBeanFrom, userUnitBeanto, false);
				userBillBean.setTimestamp(to);
				return userBillBean;
			}
		} 
		LOGGER.error("Bill Not Found");
		throw new NotFoundException("Bill Id NOT FOUND");
	}

	@Override
	public UserBillBean getLastMonthBill(String userId) {
		UserBillBean userBillBean = billRepository.userCurrentMonthBill(userId);
		if (userBillBean == null) {
			LOGGER.error("Bill Not Found");
			throw new NotFoundException("Bill Id NOT FOUND");
		}
		return userBillBean;
	}
	
	@Override
	public List<UserBillBean> getMonthlyData(String userId, int month , int year){
		
		UserBean userBean = userRepository.findOne(userId);
		if (userBean == null) {
			throw new NotFoundException("User Not Found");
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
}
