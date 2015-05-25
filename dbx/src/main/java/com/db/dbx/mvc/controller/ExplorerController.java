package com.db.dbx.mvc.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ExplorerController {

	@RequestMapping(value="/explorer", method=RequestMethod.GET)
	public String explorer(Principal currentUser, Model model, HttpServletRequest request) {
	
		return "explorer";
	}
}
