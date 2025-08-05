package com.kyh.system.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

import com.kyh.system.model.User;
import com.kyh.system.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    /* =========================================================
     *  // ユーザー管理のタグ
     * ========================================================= */
    @Test
    void userinfomation_returnsCommonInformation() {
        HttpSession session = mock(HttpSession.class);

        String view = userController.userinfomation(session);

        assertEquals("/common/information", view);
    }
    
    /* =========================================================
     *  // 個人情報のタグ
     * ========================================================= */
    @Test
    void myInfo_setsModelAndViewName() {
        HttpSession session = mock(HttpSession.class);
        User login = new User();
        login.setNo(1);
        when(session.getAttribute("user")).thenReturn(login);

        User detail = new User();
        detail.setNo(1);
        when(userService.getUserByPrimaryKey(1)).thenReturn(detail);

        ModelAndView mv = userController.myInfo(session);

        assertNotNull(mv);
        assertEquals("/common/myInfo", mv.getViewName());
        // 뷰에 담기는 키가 특정되어 있다면 아래처럼 검증 추가 (필요 시 실제 키로 수정)
        // assertEquals(detail, mv.getModel().get("user"));
        verify(userService).getUserByPrimaryKey(1);
    }

    /* =========================================================
     *  // ユーザー一覧を初期化する際、ユーザー全量の情報とその数をマップに入れておく
     * ========================================================= */
    @Test
    void userinforlist_returnsTotalMinusOneAndRows() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("page")).thenReturn("2");
        when(req.getParameter("rows")).thenReturn("10");

        // startRecord = (2-1)*10 + 1 = 11
        List<User> rows = Arrays.asList(new User(), new User());
        when(userService.getCount()).thenReturn(5);
        when(userService.selectAll(11, 10)).thenReturn(rows);

        Map<String, Object> map = userController.userinforlist(req);

        assertEquals(4, map.get("total"));      // total - 1
        assertEquals(rows, map.get("rows"));
        verify(userService).selectAll(11, 10);
        verify(userService).getCount();
    }

    /* =========================================================
     *  // 個人情報更新時（パスワードの更新は右上）
     * ========================================================= */
    @Test
    void updateMyInfo_success_updatesUserAndSession() {
        HttpSession session = mock(HttpSession.class);
        when(userService.update(any(User.class))).thenReturn(1);
        User refreshed = new User();
        refreshed.setNo(7);
        when(userService.getUserByPrimaryKey(7)).thenReturn(refreshed);

        Map<String, String> ret = userController.updateMyInfo(7, "Alice", "090-0000-0000", session);

        assertEquals("true", ret.get("success"));
        verify(userService).update(argThat(u ->
                u.getNo() == 7 &&
                "Alice".equals(u.getUsername()) &&
                "090-0000-0000".equals(u.getPhone())
        ));
        verify(session).setAttribute(eq("user"), eq(refreshed));
    }

    /* =========================================================
     *  // ユーザー情報の追加
     * ========================================================= */
    @Test
    void saveUsers_whenDuplicate_returnsMsg() {
        HttpSession session = mock(HttpSession.class);
        when(userService.checkExistenceByUserId(any(User.class))).thenReturn(1);

        Map<String, String> map = userController.saveUsers("alice", "Alice", "pw", "090", session);

        assertEquals("ユーザーは既に存在しているため、追加できません。", map.get("msg"));
        verify(userService, never()).insert(any(User.class));
    }

    @Test
    void saveUsers_whenNew_insertsAndSuccessTrue() {
        HttpSession session = mock(HttpSession.class);
        when(userService.checkExistenceByUserId(any(User.class))).thenReturn(0);
        when(userService.insert(any(User.class))).thenReturn(1);

        Map<String, String> map = userController.saveUsers("bob", "Bob", "pw", "080", session);

        assertEquals("true", map.get("success"));
        verify(userService).insert(argThat(u ->
                "bob".equals(u.getUserid()) &&
                "Bob".equals(u.getUsername()) &&
                "pw".equals(u.getPassword()) &&
                "080".equals(u.getPhone())
        ));
    }

    /* =========================================================
     *  // ユーザー情報の削除
     * ========================================================= */
    @Test
    void removeUsers_whenSelfDelete_returnsMsg() {
        HttpSession session = mock(HttpSession.class);
        User login = new User();
        login.setNo(10);
        when(session.getAttribute("user")).thenReturn(login);

        Map<String, String> map = userController.removeUsers(10, session);

        assertEquals("現在ロングインしているユーザーは削除できません。", map.get("msg"));
        verify(userService, never()).delete(anyInt());
    }

    @Test
    void removeUsers_whenOtherUser_successTrue() {
        HttpSession session = mock(HttpSession.class);
        User login = new User();
        login.setNo(10);
        when(session.getAttribute("user")).thenReturn(login);

        Map<String, String> map = userController.removeUsers(11, session);

        assertEquals("true", map.get("success"));
        verify(userService).delete(11);
    }

    /* =========================================================
     *  // ユーザー情報の更新
     * ========================================================= */
    @Test
    void update_successTrueAndCallsServiceUpdate() {
        HttpSession session = mock(HttpSession.class);
        when(userService.update(any(User.class))).thenReturn(1);

        Map<String, String> map = userController.update(3, "Neo", "newpw", "070", session);

        assertEquals("true", map.get("success"));
        verify(userService).update(argThat(u ->
                u.getNo() == 3 &&
                "Neo".equals(u.getUsername()) &&
                "newpw".equals(u.getPassword()) &&
                "070".equals(u.getPhone())
        ));
    }

    /* =========================================================
     *  // 右上のパスワード変更機能
     * ========================================================= */
    @Test
    void modifypassword_whenOldEmpty_returnsMsg() {
        HttpSession session = mock(HttpSession.class);
        User login = new User(); login.setPassword("cur");
        when(session.getAttribute("user")).thenReturn(login);

        Map<String, String> map = userController.modifypassword(1, "", "n1", "n2", session);
        assertEquals("現在のパスワードを入力してください。", map.get("msg"));
    }

    @Test
    void modifypassword_whenOldWrong_returnsMsg() {
        HttpSession session = mock(HttpSession.class);
        User login = new User(); login.setPassword("cur");
        when(session.getAttribute("user")).thenReturn(login);

        Map<String, String> map = userController.modifypassword(1, "wrong", "n1", "n1", session);
        assertEquals("現在のパスワードが正しくありません", map.get("msg"));
    }

    @Test
    void modifypassword_whenConfirmEmpty_returnsMsg() {
        HttpSession session = mock(HttpSession.class);
        User login = new User(); login.setPassword("cur");
        when(session.getAttribute("user")).thenReturn(login);

        Map<String, String> map = userController.modifypassword(1, "cur", "n1", "", session);
        assertEquals("確認用パスワードを入力してください。", map.get("msg"));
    }

    @Test
    void modifypassword_whenMismatch_returnsMsg() {
        HttpSession session = mock(HttpSession.class);
        User login = new User(); login.setPassword("cur");
        when(session.getAttribute("user")).thenReturn(login);

        Map<String, String> map = userController.modifypassword(1, "cur", "n1", "n2", session);
        assertEquals("２回入力したパスワードが一致しません。", map.get("msg"));
    }

    @Test
    void modifypassword_whenSameAsOld_returnsMsg() {
        HttpSession session = mock(HttpSession.class);
        User login = new User(); login.setPassword("cur");
        when(session.getAttribute("user")).thenReturn(login);

        Map<String, String> map = userController.modifypassword(1, "cur", "cur", "cur", session);
        assertEquals("前回のパスワードと同じものは設定できません。", map.get("msg"));
    }

    @Test
    void modifypassword_success_updatesPasswordAndReturnsSuccess() {
        HttpSession session = mock(HttpSession.class);
        User login = new User(); login.setPassword("cur");
        when(session.getAttribute("user")).thenReturn(login);

        when(userService.update(any(User.class))).thenReturn(1);

        Map<String, String> map = userController.modifypassword(1, "cur", "next", "next", session);

        assertEquals("true", map.get("success"));
        verify(userService).update(argThat(u -> u.getNo() == 1 && "next".equals(u.getPassword())));
    }

    /* =========================================================
     *  // ログアウト
     * ========================================================= */
    @Test
    void exit_removesSessionUser_andRedirectsLogin() {
        HttpSession session = mock(HttpSession.class);

        ModelAndView mv = userController.exit(session);

        verify(session).removeAttribute("user");
        assertEquals("redirect:/login/", mv.getViewName());
    }
    
    
    
    
}
