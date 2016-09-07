package com.electra.service;

import java.util.List;

import com.electra.domain.UserBillBean;

/**
 * @author sahu
 *
 */
public interface BillGeneration {

	void billGenerate();

	List<UserBillBean> getBill(String userId);

	void payBill(String userId, String id);

	UserBillBean getCurrentMonthBill(String userId);

	UserBillBean getLastMonthBill(String userId);

	List<UserBillBean> getMonthlyData(String userId, int month, int year);
}
