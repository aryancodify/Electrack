package com.electra.controller;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electra.service.ElectrackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/electra/admin/")
@Api("Electrack Admin")
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private ElectrackService electraService;

	@ApiOperation(value = "Get Total Units", notes = "Get Total Units of Specific State")
	@RequestMapping(value = "", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String,String> stateConsumption(@RequestParam String state,
			                               @RequestParam Date from,
			                               @RequestParam Date to) {
		LOGGER.info("Get Users of State : " + state);
		return electraService.stateConsumption(state, from, to);
	}
	
}
