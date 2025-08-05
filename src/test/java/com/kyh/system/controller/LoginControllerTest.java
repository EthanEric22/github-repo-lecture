package com.kyh.system.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import com.kyh.system.model.User;
import com.kyh.system.service.UserService;

class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserLoginSuccess() {
        // given
        String userid = "admin";
        String password = "adminpass";

        when(request.getParameter("userid")).thenReturn(userid);
        when(request.getParameter("password")).thenReturn(password);

        User user = new User();
        user.setUserid(userid);
        user.setPassword(password);

        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(user);

        // when
        ModelAndView modelAndView = loginController.userLogin(request, response, session);

        // then
        assertEquals("login/index", modelAndView.getViewName());
        verify(session).setAttribute("user", user);
    }

    @Test
    void testUserLoginFailure() {
        // given
        when(request.getParameter("userid")).thenReturn("wrong");
        when(request.getParameter("password")).thenReturn("wrongpass");

        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(null);

        // when
        ModelAndView modelAndView = loginController.userLogin(request, response, session);

        // then
        assertEquals("/login/login", modelAndView.getViewName());
        assertEquals("ユーザー名またはパスワードが間違っています", modelAndView.getModel().get("MSG"));
    }
}
