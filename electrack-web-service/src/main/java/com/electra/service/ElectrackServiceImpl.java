/**
 * 
 */
package com.electra.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electra.domain.AnomalyBean;
import com.electra.domain.ComplaintBean;
import com.electra.domain.Location;
import com.electra.domain.PredictionBean;
import com.electra.domain.UnitBean;
import com.electra.domain.UserBean;
import com.electra.domain.UserUnitBean;
import com.electra.domain.UserUpdateBean;
import com.electra.exceptionhandler.BadRequestException;
import com.electra.exceptionhandler.NotFoundException;
import com.electra.repository.AnomalyRepository;
import com.electra.repository.ComplaintRepository;
import com.electra.repository.PredictionRepository;
import com.electra.repository.UnitsRepository;
import com.electra.repository.UserRepository;
import com.electra.utils.ElectraUtils;

/**
 * @author sahu
 *
 */
@Service
public class ElectrackServiceImpl implements ElectrackService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ElectrackServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UnitsRepository unitsRepository;

	@Autowired
	private ComplaintRepository complaintRepository;

	@Autowired
	private PredictionRepository predictionRepository;

	@Autowired
	private AnomalyRepository anomalyRepository;

	/****************************** USER START ******************************/

	@Override
	public UserBean getElectraUser(String userId) {
		UserBean userDetail = userRepository.findOne(userId);
		if (userDetail == null) {
			LOGGER.error("User Not Found : " + userId);
			throw new NotFoundException("User Not Found");
		}
		return userDetail;
	}

	@Override
	public void createElectraUser(UserBean userDetail) {
		try {
			userRepository.insert(userDetail);
		} catch (Exception ex) {
			throw new BadRequestException("userId already exist");
		}
	}

	@Override
	public void updateUser(UserUpdateBean userUpdateDetail, String userId) {
		UserBean userDetail = userRepository.findOne(userId);
		if (userDetail == null) {
			LOGGER.error("User Not Found : " + userUpdateDetail);
			throw new NotFoundException("User Not Found");
		}
		if (userUpdateDetail.getAddress() != null && !"".equals(userUpdateDetail.getAddress())) {
			userDetail.setAddress(userUpdateDetail.getAddress());
		} else if (userUpdateDetail.getName() != null && !"".equals(userUpdateDetail.getName())) {
			userDetail.setName(userUpdateDetail.getName());
		} else if (userUpdateDetail.getPassword() != null && !"".equals(userUpdateDetail.getPassword())) {
			userDetail.setPassword(userUpdateDetail.getPassword());
		} else if (userUpdateDetail.getCity() != null && !"".equals(userUpdateDetail.getCity())) {
			userDetail.setCity(userUpdateDetail.getCity());
		} else if (userUpdateDetail.getDeveiceId() != null && !"".equals(userUpdateDetail.getDeveiceId())) {
			userDetail.setDeveiceId(userUpdateDetail.getDeveiceId());
		} else if (userUpdateDetail.getState() != null && !"".equals(userUpdateDetail.getState())) {
			userDetail.setState(userUpdateDetail.getState());
		} else if (userUpdateDetail.getLoadLimit() > 0) {
			userDetail.setLoadLimit(userUpdateDetail.getLoadLimit());
		}
		userRepository.save(userDetail);
	}

	@Override
	public UserBean electraUserLogin(String userId, String password) {
		UserBean userBean = userRepository.userDetail(userId, password);
		if (userBean == null) {
			throw new NotFoundException("User Not Found");
		}
		return userBean;
	}

	@Override
	public List<UserBean> getUserNear(String userId) {
		UserBean userDetail = userRepository.findOne(userId);
		if (userDetail == null) {
			LOGGER.error("User Not Found : " + userId);
			throw new NotFoundException("User Not Found");
		}
		Location location = userDetail.getLocation();
		List<Double> coordinated = location.getCoordinates();

		// Point DUS = new Point(userDetail., 51.224088 );

		//List<UserBean> userList=userRepository.findByPositionNear(p, d);

		 List<UserBean>	 userList=userRepository.userNearMe(coordinated.get(0),coordinated.get(1));

		return userList;
	}

	@Override
	public Map<String, String> stateConsumption(String state, Date from, Date to) {
		Map<String, String> map = new HashMap<>();
		map.put("totalUnits", "0");
		List<UserBean> userList = userRepository.userOfState(state);

		String userIdString = userList.toString().substring(0);
		userIdString = userIdString.substring(0, userIdString.length() - 1);
		List<String> userIds = new ArrayList<>(Arrays.asList(userIdString.split(",")));

		List<UserUnitBean> findUnitsBetween = unitsRepository.findUnitsBetween(userIds, from, to);

		findUnitsBetween.forEach(userUnitBean -> {

			List<UnitBean> userUnitsList = userUnitBean.getUnits();
			if (!userUnitsList.isEmpty()) {
				Double totalUnits = new Double(map.get("totalUnits"));
				totalUnits = totalUnits + new Double(userUnitsList.get(userUnitsList.size() - 1).getUnit());
				map.put("totalUnits", totalUnits.toString());
			}

		});
		return map;
	}

	/****************************** USER END ******************************/

	/****************************** UNITS START ******************************/

	@Override
	public void insertUserUnit(UserUnitBean userUnit) {
		try {
			unitsRepository.insert(userUnit);
			LOGGER.info("Document succefully creted");
		} catch (Exception e) {
			unitsRepository.insertUnit(userUnit.getDate(), userUnit.getUnits().get(0));
		}
	}

	@Override
	public List<UserUnitBean> getUnitsBetween(String userId, Date from, Date to) {
		return unitsRepository.findUnitsBetween(userId, from, to);
	}

	public double getUnit(String userId) {
		UserUnitBean userUnitBean = unitsRepository.getCurrentMonthUnit(userId, ElectraUtils.getDate());
		if (userUnitBean != null) {
			List<UnitBean> unitBeans = userUnitBean.getUnits();
			Collections.sort(unitBeans, UnitBean.COMPARE_TIME);
			if (!unitBeans.isEmpty()) {
				return unitBeans.get(unitBeans.size() - 1).getUnit();
			}
		}
		return 0;
	}

	/****************************** UNITS END ******************************/

	/******************************
	 * COMPLAINT START
	 ******************************/

	@Override
	public void createComplaint(ComplaintBean complaintBean) {
		complaintRepository.save(complaintBean);
	}

	@Override
	public List<ComplaintBean> getAllComplaint() {
		return complaintRepository.findAll();
	}

	@Override
	public List<ComplaintBean> getUserComplaints(String userId) {
		return complaintRepository.userAllComplaint(userId);
	}

	@Override
	public ComplaintBean getComplaint(String complaintId) {
		return complaintRepository.findOne(complaintId);
	}

	@Override
	public void updateComplaint(String userId, String complaintId, String status) {
		ComplaintBean complaintBean = complaintRepository.userComplaint(userId, complaintId);
		if (complaintBean == null) {
			throw new NotFoundException("Complaint Not Found");
		}
		complaintBean.setStatus(status);
		complaintRepository.save(complaintBean);
	}

	/******************************
	 * COMPLAINT END
	 ******************************/

	/*************************** PREDICTION START *****************************/
	@Override
	public PredictionBean getPrediction(String userId) {
		PredictionBean bean = predictionRepository.findOne(userId);
		if (bean == null) {
			throw new NotFoundException("No Prediction Found");
		}
		return bean;
	}

	/******************************
	 * PREDICTION END
	 ******************************/

	/*************************** Anomaly START *****************************/
	@Override
	public List<AnomalyBean> getAnomaly() {
		return anomalyRepository.findAll();
	}

	/****************************** Anomaly END ******************************/
}
