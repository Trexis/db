package com.db.dbx.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecuredController {

	@RequestMapping(value="/security/{action}", method=RequestMethod.GET)
	public String security(@PathVariable String action) {
		return action;
	}
}
