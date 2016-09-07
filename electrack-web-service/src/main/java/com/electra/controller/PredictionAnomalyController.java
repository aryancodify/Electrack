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
import org.springframework.web.bind.annotation.RestController;

import com.electra.domain.AnomalyBean;
import com.electra.domain.PredictionBean;
import com.electra.service.ElectrackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Validated
@RestController
@RequestMapping("/api/electra/")
@Api("Electrack Predition")
public class PredictionAnomalyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PredictionAnomalyController.class);

	@Autowired
	private ElectrackService electraService;

	@ApiOperation(value = "Get Prediction", notes = "Get User Units Prediction")
	@RequestMapping(value = "predition/{userId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public PredictionBean getPrediction(@PathVariable String userId) {
		electraService.getElectraUser(userId);
		LOGGER.info("Get Prediction : " + userId);
		return electraService.getPrediction(userId);
	}
	
	@ApiOperation(value = "Get Anomaly", notes = "Get Anomaly Users")
	@RequestMapping(value = "anomaly", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<AnomalyBean> getAnomaly() { 
		return electraService.getAnomaly();
	}
	
}
