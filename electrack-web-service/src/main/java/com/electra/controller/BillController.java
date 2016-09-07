package com.electra.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electra.domain.UserBillBean;
import com.electra.service.BillGeneration;
import com.electra.service.ElectrackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Validated
@RestController
@RequestMapping("/api/electra/user/{userId}/bill")
@Api("Electrack  Bill")
public class BillController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BillController.class);

	@Autowired
	private BillGeneration billGeneration;

	@Autowired
	private ElectrackService electraService;

	@ApiOperation(value = "Get Bill", notes = "Get Electrack Users Bill")
	@RequestMapping(value = "", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<UserBillBean> getUserBill(@PathVariable String userId) {
		electraService.getElectraUser(userId);
		LOGGER.info("Get my Bill : " + userId);
		return billGeneration.getBill(userId);
	}

	@ApiOperation(value = "Pay Bill", notes = "Pay Electrack Users Bill")
	@RequestMapping(value = "{billId}", method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void payUserBill(@PathVariable String userId, @PathVariable String billId) {
		electraService.getElectraUser(userId);
		LOGGER.info("Pay my Bill : " + userId);
		billGeneration.payBill(userId, billId);
	}

	@ApiOperation(value = "Get Current Month Bill", notes = "Get Electrack Users Current Bill and Unit Detail")
	@RequestMapping(value = "now", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public UserBillBean getUserCurrentMonthBill(@PathVariable String userId) {
		LOGGER.info("Get my Current Bill : " + userId);
		return billGeneration.getCurrentMonthBill(userId);
	}

	@ApiOperation(value = "Get Last Month Bill", notes = "Get Electrack Users Last Bill and Unit Detail")
	@RequestMapping(value = "last", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public UserBillBean getUserLastMonthBill(@PathVariable String userId) {
		electraService.getElectraUser(userId);
		LOGGER.info("Get my Latest Bill : " + userId);
		return billGeneration.getLastMonthBill(userId);
	}
	
	@ApiOperation(value = "Get monthly Units", notes = "Get Electrack Users monthly  Units")
	@RequestMapping(value = "monthly", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<UserBillBean> getUserMonthlyBill(@PathVariable String userId,
												 @RequestParam(required =true) int month,
												 @RequestParam(required =true) int year) {
		LOGGER.info("Get my Bill : " + userId);
		return billGeneration.getMonthlyData(userId, month, year);
	}
}
