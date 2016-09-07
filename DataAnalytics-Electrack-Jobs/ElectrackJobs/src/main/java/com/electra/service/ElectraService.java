package com.electra.service;

import java.util.Date;
import java.util.List;

import com.electra.domain.UserBillBean;

public interface ElectraService {
	public double getUnit(String userId,Date date);
	public List<UserBillBean> getBill(String userId);
	public List<UserBillBean> getDayData(String userId, int month , int year);
}
