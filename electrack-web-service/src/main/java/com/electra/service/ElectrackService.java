/**
 * 
 */
package com.electra.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.electra.domain.AnomalyBean;
import com.electra.domain.ComplaintBean;
import com.electra.domain.PredictionBean;
import com.electra.domain.UserBean;
import com.electra.domain.UserUnitBean;
import com.electra.domain.UserUpdateBean;

/**
 * @author sahu
 *
 */
public interface ElectrackService {

	/**
	 * <strong>getElectraUser</strong><br>
	 * Get Electra User Detail<br>
	 * 
	 * @author sahu
	 * @param userId
	 *            : userId of type String
	 * @return {@link com.electra.domain.UserBean}
	 */
	UserBean getElectraUser(String userId);

	/**
	 * <strong>createElectraUser</strong><br>
	 * Create Electra User Detail<br>
	 * 
	 * @author sahu
	 * @param userDetail
	 *            : userDetail of type {@link UserBean}
	 */
	void createElectraUser(UserBean userDetail);
	
	/**
	 * <strong>updateElectraProfile</strong><br>
	 * Update Electra User Details<br>
	 * 
	 * @author sahu
	 * @param userDetail
	 *            : userDetail of type {@link UserBean}
	 */
	void updateUser(UserUpdateBean userUpdateDetail,String userId);

	/**
	 * @author sahu
	 * @param email
	 * @param password
	 * @return
	 */
	UserBean electraUserLogin(String email,String password);
	
	/**
	 * <strong>createElectraUser</strong><br>
	 * Insert New Unit Per Day<br>
	 * 
	 * @param userUnit
	 */
	void insertUserUnit(UserUnitBean userUnit);
	
	/**
	 * @param userId
	 * @return
	 */
	List<UserBean> getUserNear(String userId);
	
	double getUnit(String userId);
	
	List<UserUnitBean> getUnitsBetween(String userId, Date from, Date to);
	
	void createComplaint(ComplaintBean complaintBean);
	
	List<ComplaintBean> getAllComplaint();
	
	List<ComplaintBean> getUserComplaints(String userId);
	
	ComplaintBean getComplaint(String complaintId);
	
	void updateComplaint(String userId,String complaintId, String status);
	
	PredictionBean getPrediction(String userId);
	
	Map<String,String> stateConsumption(String state,Date from,Date to);
	
	List<AnomalyBean> getAnomaly();
	
}
