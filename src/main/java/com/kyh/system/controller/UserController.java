package com.kyh.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kyh.system.model.User;
import com.kyh.system.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	static final int pageSize = 10;

	@Autowired
	private UserService userService;

	// ユーザー管理のタグ
	@RequestMapping(value = "/userinfomation", method = { RequestMethod.POST, RequestMethod.GET })
	public String userinfomation(HttpSession session) {
		return "/common/information";
	}

	// 個人情報のタグ
	@RequestMapping(value = "/myInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView myInfo(HttpSession session) {
		ModelAndView model = new ModelAndView();
		User user = (User) session.getAttribute("user");
		User userLogined = userService.getUserByPrimaryKey(user.getNo());
		session.setAttribute("user", userLogined);
		model.addObject("user", userLogined);
		model.setViewName("/common/myInfo");
		return model;
	}

	//ユーザー一覧を初期化する際、ユーザー全量の情報とその数をマップに入れておく
	@PostMapping(value = "/userinforlist")
	@ResponseBody
	public Map<String, Object> userinforlist(HttpServletRequest request) {
		int page = Integer.parseInt(request.getParameter("page"));
		int pageSize = Integer.parseInt(request.getParameter("rows")); // pageSize
		int startRecord = (page - 1) * pageSize + 1;
		int total = userService.getCount();
		List<User> userinforlist = userService.selectAll(startRecord, pageSize);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", total - 1);
		resultMap.put("rows", userinforlist);
		return resultMap;
	}

	// 個人情報更新時（パスワードの更新は右上）
	@PostMapping(value = "/updateMyInfo")
	@ResponseBody
	public Map<String, String> updateMyInfo(
			@RequestParam("no") Integer no,
			@RequestParam("username") String username,
			@RequestParam("phone") String phone, HttpSession session) {
		Map<String, String> result = new HashMap<>();

		User user = new User();
		user.setNo(no);
		user.setUsername(username);
		user.setPhone(phone);
		try {
			userService.update(user);
			session.setAttribute("user", userService.getUserByPrimaryKey(no));
			result.put("success", "true");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// ユーザー情報の追加
	@PostMapping(value = "/add")
	@ResponseBody
	public Map<String, String> saveUsers(
			@RequestParam("userid") String userid,
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("phone") String phone, HttpSession session) {

		Map<String, String> map = new HashMap<>();
		User user = new User();
		user.setUserid(userid);
		user.setPassword(password);
		user.setUsername(username);
		user.setPhone(phone);
		try {
			if (userService.checkExistenceByUserId(user) > 0) {
				map.put("msg", "ユーザーは既に存在しているため、追加できません。");
			} else {
				userService.insert(user);
				map.put("success", "true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	// ユーザー情報の削除
	@PostMapping(value = "/delete")
	@ResponseBody
	public Map<String, String> removeUsers(@RequestParam("no") Integer no, HttpSession session) {
		Map<String, String> result = new HashMap<>();
		if (((User) session.getAttribute("user")).getNo().equals(no)) {
			result.put("msg", "現在ロングインしているユーザーは削除できません。");
			return result;
		}
		try {
			userService.delete(no);
			result.put("success", "true");
			System.out.println("削除No: " + no);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "エラーが発生しました。");
		}
		return result;
	}

	// ユーザー情報の更新
	@PostMapping(value = "/update")
	@ResponseBody
	public Map<String, String> update(
			@RequestParam("no") Integer no,
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("phone") String phone, HttpSession session) {

		Map<String, String> map = new HashMap<>();
		User user = new User();
		user.setNo(no);
		user.setPassword(password);
		user.setUsername(username);
		user.setPhone(phone);
		try {
			userService.update(user);
			map.put("success", "true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	// 右上のパスワード変更機能
	@PostMapping(value = "/modifypassword")
	@ResponseBody
	public Map<String, String> modifypassword(
			@RequestParam("no") int no,
			@RequestParam("oldpassword") String oldpassword,
			@RequestParam("newpassword1") String newpassword1,
			@RequestParam("newpassword2") String newpassword2, HttpSession session) {
		Map<String, String> result = new HashMap<>();
		User userLogined = (User) session.getAttribute("user");
		if (oldpassword == null || "".equals(oldpassword)) {
			result.put("msg", "現在のパスワードを入力してください。");
			return result;
		} else if (!userLogined.getPassword().equals(oldpassword)) {
			result.put("msg", "現在のパスワードが正しくありません");
			return result;
		} else if (newpassword1 == null || "".equals(newpassword1)) {
			result.put("msg", "新しいパスワードを入力してください。");
			return result;
		} else if (newpassword2 == null || "".equals(newpassword2)) {
			result.put("msg", "確認用パスワードを入力してください。");
			return result;
		} else if (!newpassword2.equals(newpassword1)) {
			result.put("msg", "２回入力したパスワードが一致しません。");
			return result;
		} else if (userLogined.getPassword().equals(newpassword1)) {
			result.put("msg", "前回のパスワードと同じものは設定できません。");
			return result;
		}

		User user = new User();
		user.setNo(no);
		user.setPassword(newpassword2);
		try {
			userService.update(user);
			
			session.setAttribute("user", userService.getUserByPrimaryKey(no));
			
			result.put("success", "true");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("msg", "変更が失敗しました。");
		return result;
	}

	// ログアウト
	@RequestMapping(value = "/exit", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView exit(HttpSession session) {
		session.removeAttribute("user");
		ModelAndView model = new ModelAndView();
		model.setViewName("redirect:/login/");
		return model;
	}
}
