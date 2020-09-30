/**
 * 
 */
package com.citi.zuul;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Khyati
 *
 */
@RestController
public class ZuulGatewayController {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String firstPage() {

		return "Welcome to Zuul Gateway!";
	}
}
