package com.kyh.system.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.kyh.system.mapper.UserMapper;
import com.kyh.system.model.User;
import com.kyh.system.model.UserExample;
import com.kyh.system.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

	// データベース操作のマッパーをインジェクションする
	@Autowired
	private UserMapper userMapper;

	/**
	 * ユーザーを追加する
	 * */
	@Override
	public int insert(User user) {
		return userMapper.insert(user);
	}

	/***
	 * IDによるユーザー削除
	 *
	 */
	@Override
	public int delete(int no) {
		return userMapper.deleteByPrimaryKey(no);
	}

	/***
	 *
	 * ユーザーを更新する
	 */
	@Override
	public int update(User user) {
		return userMapper.updateByPrimaryKeySelective(user);
	}

	/***
	 * ページング検索
	 * */
	@Override
	public List<User> selectAll(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		UserExample example = new UserExample();
		List<User> users = userMapper.selectByExample(example);

		return users;
	}

	/**
	* ユーザーログイン
	* */
	@Override
	public User getUserByUserIdAndPassword(User user) {

		UserExample example = new UserExample();
		UserExample.Criteria criteria = example.createCriteria();

		criteria.andUseridEqualTo(user.getUserid());
		criteria.andPasswordEqualTo(user.getPassword());

		List<User> users = userMapper.selectByExample(example);

		if (!users.isEmpty()) {
			return users.get(0);
		} else {
			return null;
		}
	}

	@Override
	public User getUserByPrimaryKey(int no) {
		return userMapper.selectByPrimaryKey(no);
	}

	@Override
	public int getCount() {
		UserExample example = new UserExample();
		int count = (int) userMapper.countByExample(example);
		return count;
	}

	@Override
	public int checkExistenceByUserId(User user) {
		UserExample example = new UserExample();
		UserExample.Criteria criteria = example.createCriteria();
		criteria.andUseridEqualTo(user.getUserid());
		int count = (int) userMapper.countByExample(example);

		return count;
	}
}
