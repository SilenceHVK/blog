package me.hvkcoder.mybatis.practice;

import lombok.extern.slf4j.Slf4j;
import me.hvkcoder.mybatis.practice.domain.User;
import me.hvkcoder.mybatis.practice.repository.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * MyBatis 操作练习
 *
 * @author h_vk
 * @since 2022/2/15
 */
@Slf4j
public class MyBatisPractice {
  public static void main(String[] args) throws IOException {
    // 1. 加载配置文件
    InputStream config = Resources.getResourceAsStream("mybatis-config.xml");

    // 2. 获取 SqlSession 对象
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
    SqlSession sqlSession = sqlSessionFactory.openSession();
    // 通过指定 Mapper statement ID
    //    List<User> userList = sqlSession.selectList("selectAll");

    // 通过获取 Mapper 执行
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    List<User> userList = userMapper.selectAll();
    userList.forEach(o -> log.info("{}", o));
    sqlSession.close();
  }
}
