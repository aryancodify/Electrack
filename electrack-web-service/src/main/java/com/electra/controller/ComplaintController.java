package com.electra.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electra.domain.ComplaintBean;
import com.electra.service.ElectrackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/electra/complaint/")
@Api("Electrack complaint")
public class ComplaintController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComplaintController.class);

	@Autowired
	private ElectrackService electraService;

	@ApiOperation(value = "Create User Complaint", notes = "Create Electrack User Complaint")
	@RequestMapping(value = "{userId}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void complaint(@PathVariable String userId,
						  @RequestParam String desc,
						  @RequestParam String type) {
		electraService.getElectraUser(userId);
		LOGGER.info("Create User Complaint : "+userId);
		ComplaintBean complaintBean=new ComplaintBean(userId, desc, type);
		electraService.createComplaint(complaintBean);
	}

	@ApiOperation(value = "Get User Complaint", notes = "Get Electrack User's All Complaints")
	@RequestMapping(value = "{userId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<ComplaintBean> getComplaint(@PathVariable String userId) {
		electraService.getElectraUser(userId);
		LOGGER.info("Get User's All Complaints : "+userId);
		return electraService.getUserComplaints(userId);
	}
	
	@ApiOperation(value = "Get All Complaint", notes = "Get All User Complaint")
	@RequestMapping(value = "", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<ComplaintBean> getAllComplaint() {
		LOGGER.info("Get All Complaint : ");
		return electraService.getAllComplaint();
	}
	
	@ApiOperation(value = "Update Complaint", notes = "Update Status Of User Complaint")
	@RequestMapping(value = "{userId}/{complaintId}", method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE })
	public void updateComplaint(@PathVariable String userId,
								@PathVariable String complaintId,
								@RequestParam String status) {
		electraService.getElectraUser(userId);
		LOGGER.info("Update Complaint : ");
		electraService.updateComplaint(userId, complaintId, status);
	}
}
