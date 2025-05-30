package com.kyh.system.service;

import java.util.List;

import com.kyh.system.model.User;

public interface UserService {

	public User getUserByUserIdAndPassword(User user);
	
	public int checkExistenceByUserId(User user);

	public User getUserByPrimaryKey(int no);

	List<User> selectAll(int pageNum, int pageSize);

	int delete(int no);
	
	int insert(User user);

	int update(User user);

	public int getCount();
}
