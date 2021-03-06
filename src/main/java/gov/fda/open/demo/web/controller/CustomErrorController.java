/**
 * *********************************************************************
 * Copyright (c) 2015 InfoZen, Inc. All rights reserved. InfoZen
 * PROPRIETARY/CONFIDENTIAL. Usage is subject to license terms.
 * *********************************************************************
 */
package gov.fda.open.demo.web.controller;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.base.Throwables;

/**
 * Controller class to handle unknow exceptions.
 */
@Controller
class CustomErrorController {
	
	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(CustomErrorController.class);
	
	/**
	 * Display an error page, as defined in web.xml <code>custom-error</code>
	 * element.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping("unknownerror")
	public String generalError(HttpServletRequest request, HttpServletResponse response, Model model) {
		// retrieve some useful information from the request
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		// String servletName = (String)
		// request.getAttribute("javax.servlet.error.servlet_name");
		String exceptionMessage = getExceptionMessage(throwable, statusCode);

		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}
		
		String message = String.format(
				"Http Status code : '%d' returned for request URI '%s' with message '%s'", statusCode,
				requestUri, exceptionMessage);
		LOG.error(message, throwable);
		
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		model.addAttribute("errorMessage", message);
		return "error/general";
	}

	/**
	 * Gets the exception message.
	 *
	 * @param throwable
	 *            the throwable
	 * @param statusCode
	 *            the status code
	 * @return the exception message
	 */
	private String getExceptionMessage(Throwable throwable, Integer statusCode) {
		if (throwable != null) {
			return Throwables.getRootCause(throwable).getMessage();
		}
		HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
		return httpStatus.getReasonPhrase();
	}
}
