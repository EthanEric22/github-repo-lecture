package com.kyh.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.kyh.system.model.User;
import com.kyh.system.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
    public RedirectView redirectToLogin() {
        return new RedirectView("/login/");
    }

	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
	public String login() {
		return "/login/login";
	}

	// ユーザーログイン
	@RequestMapping(value = "/login/userLogin", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView userLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		user.setUserid(request.getParameter("userid"));
		user.setPassword(request.getParameter("password"));
		User result = userService.getUserByUserIdAndPassword(user);
		
		if (result != null) {
			session.setAttribute("user", result);
			model.setViewName("login/index");
		} else {
			model.addObject("MSG", "ユーザー名またはパスワードが間違っています");
			model.setViewName("/login/login");
		}
		return model;
	}

	@RequestMapping(value = "/login/welcome", method = { RequestMethod.POST, RequestMethod.GET })
	public String welcome() {
		return "/login/welcome";
	}
}
