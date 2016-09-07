/**
 * 
 */
package com.electra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;
 
@Controller
@ApiIgnore
public class HomeController {

	@RequestMapping("/")
	public String redirectToSwaggerUi() {
		return "redirect:/swagger-ui.html";
	}

	@RequestMapping("/swagger/{appName}")
	public String forwardToSwaggerDefinition() {
		return "forward:/v2/api-docs";
	}

}
