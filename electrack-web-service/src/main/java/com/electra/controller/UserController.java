package com.electra.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electra.domain.UserBean;
import com.electra.domain.UserUpdateBean;
import com.electra.service.BillGeneration;
import com.electra.service.ElectrackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Validated
@RestController
@RequestMapping("/api/electra/user/{userId}")
@Api("Electrack User")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private ElectrackService electraService;
	
	@Autowired
	private BillGeneration billGeneration;
 
	@ApiOperation(value = "Get Electrack User", notes = "Get User Detail")
	@RequestMapping(value = "", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public UserBean getElectraUser(@PathVariable String userId) {
		LOGGER.info("Get User Detail : "+userId);
		return electraService.getElectraUser(userId);
	}

	@ApiOperation(value = "", notes = "Create New Electrack User.")
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> createElectraUser(@RequestBody UserBean user) {
		LOGGER.info("Create User ");
		
		/*for (int i = 2; i < 100000; i++) {
			electraService.createElectraUser(user);
			List<Double> loc=new ArrayList<>();
			loc.add(-73.93414657);
			loc.add( 40.82302903);
			user =new UserBean("U"+i, "USER NAME "+i, i+ "#, ADDRESS", i+" CITY", i+" state", "user", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Z3V5IiwiZ3JvdXAiOiJjbGllbnQiLCJleHAiOjE0NjEzODU2NzAwNzksInNjb3BlIjpbXSwiaXNzIjoiaWFtLm1lcnJpbGxjb3JwLmNvbSIsImF1ZCI6ImphdmVsaW5tYy5jb20iLCJVc2VyTmFtZSI6InRlc3RAbWVycmlsbGNvcnAuY29tIn0.G40nfX0G9al4i6gcTde51HwqU3MlqoyC25W8JLT0Bko", new Location(loc));
		}*/
		electraService.createElectraUser(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "", notes = "Electrack User Login.")
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public UserBean electraUserLogin(@PathVariable String userId,@RequestParam String password) {
		LOGGER.info("Electrack User Login "+userId);
		electraService.getUnit(userId);
		UserBean detail=electraService.electraUserLogin(userId, password);
		detail.setTotalUnits(String.valueOf(billGeneration.getCurrentMonthBill(userId).getTotalUnit()));
		return detail;
	}
	
	@ApiOperation(value = "Get Electrack Users", notes = "Get All User Detail")
	@RequestMapping(value = "/near", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<UserBean> getUserNear(@PathVariable String userId) {
		LOGGER.info("Get User Detail : "+userId);
		return electraService.getUserNear(userId);
	}
	
	@ApiOperation(value = "", notes = "Update Load Limit")
	@RequestMapping(value = "update", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<JSONObject> updateProfileUser(@PathVariable String userId,
			@RequestParam(required=true) double loadLimit) {
		LOGGER.info("update Profile "+userId);
		UserUpdateBean userDetail=new UserUpdateBean();
		userDetail.setLoadLimit(loadLimit);
		electraService.updateUser(userDetail,userId);
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("status", "success");
		jsonObject.put("code", 200);
		return new ResponseEntity<>(jsonObject,HttpStatus.OK);
	}
}
