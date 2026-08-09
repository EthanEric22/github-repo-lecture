package com.kyh.system.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.kyh.system.Impl.UserServiceImpl;
import com.kyh.system.mapper.UserMapper;
import com.kyh.system.model.User;
import com.kyh.system.model.UserExample;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Test
    void getCount_returnsMapperCount() {
        // given
        when(userMapper.countByExample(any(UserExample.class))).thenReturn(42L);

        // when
        int result = userService.getCount();

        // then
        assertEquals(42, result);
        verify(userMapper, times(1)).countByExample(any(UserExample.class));
    }

    @Test
    void checkExistenceByUserId_whenExists_returnsPositive() {
        // given
        User u = new User();
        u.setUserid("alice");
        when(userMapper.countByExample(any(UserExample.class))).thenReturn(1L);

        // when
        int result = userService.checkExistenceByUserId(u);

        // then
        assertEquals(1, result);
        verify(userMapper).countByExample(any(UserExample.class));
    }

}
