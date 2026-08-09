package com.kyh.system.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.kyh.system.model.User;
import com.kyh.system.service.UserService;

public class LoginControllerTest {

    private LoginController loginController;
    private UserService userService;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        loginController = new LoginController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        // リフレクションでUserServiceをセットする
//        try {
//            java.lang.reflect.Field field = LoginController.class.getDeclaredField("userService");
//            field.setAccessible(true);
//            field.set(loginController, userService);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    // 1. 正常なログインを作成したテストケース
    @Test
    public void testLogin_successful() {
        when(request.getParameter("userid")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("password123");
        User user = new User();
        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(user);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("login/index", mv.getViewName());
        verify(session).setAttribute("user", user);
    }

    // 2. パスワード未入力
    @Test
    public void testLogin_emptyPassword() {
        when(request.getParameter("userid")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("");
        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(null);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("/login/login", mv.getViewName());
        assertEquals("ユーザー名またはパスワードが間違っています", mv.getModel().get("MSG"));
        verify(session, never()).setAttribute(eq("user"), any());
    }

    // 3. パスワードnull
    @Test
    public void testLogin_nullPassword() {
        when(request.getParameter("userid")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn(null);
        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(null);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("/login/login", mv.getViewName());
    }

    // 4. ユーザーID未入力
    @Test
    public void testLogin_emptyUserId() {
        when(request.getParameter("userid")).thenReturn("");
        when(request.getParameter("password")).thenReturn("password123");
        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(null);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("/login/login", mv.getViewName());
    }

    // 5. ユーザーID null
    @Test
    public void testLogin_nullUserId() {
        when(request.getParameter("userid")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("password123");
        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(null);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("/login/login", mv.getViewName());
    }

    // 6. ユーザーIDが数字のみ
    @Test
    public void testLogin_userIdNumericOnly() {
        when(request.getParameter("userid")).thenReturn("123456");
        when(request.getParameter("password")).thenReturn("password123");
        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(null);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("/login/login", mv.getViewName());
    }

    // 7. ユーザーIDにアンダースコア
    @Test
    public void testLogin_userIdWithUnderscore() {
        when(request.getParameter("userid")).thenReturn("admin_user");
        when(request.getParameter("password")).thenReturn("password123");
        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(null);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("/login/login", mv.getViewName());
    }

    // 8. ユーザーIDが日本語
    @Test
    public void testLogin_userIdInJapanese() {
        when(request.getParameter("userid")).thenReturn("テスト");
        when(request.getParameter("password")).thenReturn("password123");
        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(null);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("/login/login", mv.getViewName());
    }

    // 9. パスワード間違い
    @Test
    public void testLogin_wrongPassword() {
        when(request.getParameter("userid")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("wrongpass");
        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(null);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("/login/login", mv.getViewName());
    }

    // 10. 存在しないユーザーID
    @Test
    public void testLogin_userNotFound() {
        when(request.getParameter("userid")).thenReturn("nonexistent");
        when(request.getParameter("password")).thenReturn("anything");
        when(userService.getUserByUserIdAndPassword(any(User.class))).thenReturn(null);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("/login/login", mv.getViewName());
    }
}