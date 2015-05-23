package com.db.dbx.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ExplorerController {

	@RequestMapping(value="/explorer", method=RequestMethod.GET)
	public void explorer() {
	}
}
