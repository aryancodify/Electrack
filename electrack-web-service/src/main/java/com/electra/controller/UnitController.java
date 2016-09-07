package com.electra.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electra.domain.UnitBean;
import com.electra.domain.UserUnitBean;
import com.electra.service.ElectrackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Validated
@RestController
@RequestMapping("/api/electra/unit")
@Api("Electrack Unit")
public class UnitController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnitController.class);

	@Autowired
	private ElectrackService electraService;

	@ApiOperation(value = "Get User Units", notes = "Get User Units Detail")
	@RequestMapping(value = "{userId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<UserUnitBean> getUserUnits(@PathVariable String userId,
			@RequestParam  Date from,
			@RequestParam Date to) {
		electraService.getElectraUser(userId);
		LOGGER.info("Get User Detail : " + userId);
		return electraService.getUnitsBetween(userId, from, to);
	}

	@ApiOperation(value = "Insert Units", notes = "Insert New Electrack Unit Once a Day.")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<String> createElectraUser(@RequestParam String userId,@RequestParam double unit) {
		electraService.getElectraUser(userId);
		LOGGER.info("Insert User Unit");
		UserUnitBean userUnit=new UserUnitBean(userId, unit);
		electraService.insertUserUnit(userUnit);
		return new ResponseEntity<>("1 success",HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get User Unit", notes = "Get Current Month Unit")
	@RequestMapping(value = "/{userId}/unit", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String,String> getUserUnits(@PathVariable String userId) {
		electraService.getElectraUser(userId);
		LOGGER.info("Get User Detail : " + userId);
		Map<String , String> result=new HashMap<>();
		result.put("unit", String.valueOf(electraService.getUnit(userId)));
		return result;
	}
	
	private static double permisstionMask[] = { 0.1, 0.2, 0.5,1.0,0.3,1,1.5 ,0.9, 0.7};
	@Deprecated
	@ApiOperation(value = "dataEntry dataEntry", notes = "dataEntry dataEntry dataEntry dataEntry dataEntry.")
	@RequestMapping(value = "/{userId}/dataentry", method = RequestMethod.PUT)
	public ResponseEntity<String> dataEntry(@PathVariable String userId,@RequestParam double unit,@RequestParam int month) {
		electraService.getElectraUser(userId);
		LOGGER.info("Insert User Unit");
		UserUnitBean userUnit;
		double totalUnit=unit;
		for (int i = 1; i <= 31; i++) {
			userUnit=new UserUnitBean(userId, unit);
			userUnit.setDate(LocalDateTime.now().toLocalDate().withMonth(month).withDayOfMonth(i));
			electraService.insertUserUnit(userUnit);
			
			for(int k=0; k<=23;k++){
				userUnit.setDate(LocalDateTime.now().withHour(k).withMonth(month).toLocalDate().withDayOfMonth(i));
				int idx = new Random().nextInt(permisstionMask.length);
				totalUnit+=idx;
				double m=totalUnit;
				userUnit.setUnits(new ArrayList<UnitBean>() {
					{
						add(new UnitBean(m));
					}
				});
				electraService.insertUserUnit(userUnit);
			}
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
}
