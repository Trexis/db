package com.db.dbx.mvc.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.db.dbx.controller.ModelContext;
import com.db.dbx.enums.StatusCode;
import com.db.dbx.exceptions.UsernameAlreadyInUseException;
import com.db.dbx.i18n.Message;
import com.db.dbx.model.User;
import com.db.dbx.repository.UserRepository;
import com.db.dbx.security.EnrollForm;
import com.db.dbx.utilities.SecurityUtils;

@Controller
public class EnrollController {

	private final UserRepository userRepository;
	private final ProviderSignInUtils providerSignInUtils;

	@Inject
	private ModelContext modelContext;
	
	@Inject
	public EnrollController(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.providerSignInUtils = new ProviderSignInUtils();
	}

	@RequestMapping(value="/security/enroll", method=RequestMethod.GET)
	public EnrollForm enrollForm(WebRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		if (connection != null) {
			request.setAttribute("message", new Message(StatusCode.Info, "Your " + StringUtils.capitalize(connection.getKey().getProviderId()) + " account is not associated with a Spring Social Showcase account. If you're new, please sign up."), WebRequest.SCOPE_REQUEST);
			return EnrollForm.fromProviderUser(connection.fetchUserProfile());
		} else {
			return new EnrollForm();
		}
	}

	@RequestMapping(value="/security/enroll", method=RequestMethod.POST)
	public String enroll(@Valid EnrollForm form, BindingResult formBinding, WebRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		User user = createUser(form, formBinding);
		if (user != null) {
			SecurityUtils.signin(user.getUsername());
			providerSignInUtils.doPostSignUp(user.getUsername(), request);
			return "redirect:/";
		}
		return null;
	}

	// internal helpers
	
	private User createUser(EnrollForm form, BindingResult formBinding) {
		try {
			User user = new User(modelContext, form.getUsername(), form.getPassword(), form.getFirstName(), form.getLastName(), form.getTenantName(), "default");
			userRepository.createUser(user);
			return user;
		} catch (UsernameAlreadyInUseException e) {
			formBinding.rejectValue("username", "user.duplicateUsername", "already in use");
			return null;
		}
	}

}
