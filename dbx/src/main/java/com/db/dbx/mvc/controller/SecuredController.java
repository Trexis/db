package com.db.dbx.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecuredController {

	@RequestMapping(value="/secured", method=RequestMethod.GET)
	public void secured() {
	}
}
