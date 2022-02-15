package me.hvkcoder.mybatis.practice.repository;

import me.hvkcoder.mybatis.practice.domain.User;

import java.util.List;

/**
 * @author h_vk
 * @since 2022/2/15
 */
public interface UserMapper {
  /**
   * 获取全部数据
   *
   * @return
   */
  List<User> selectAll();
}
