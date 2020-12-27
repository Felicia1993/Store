package org.mybatis.demo.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.demo.entity.User;
import org.mybatis.demo.mapper.UserMapper;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

public class Test {
    public static void main(String[] args) {
        String resource = "mybatis-config.xml";
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);

            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            //获取sqlSession，sqlSession相当于JDBC的Connction
            SqlSession sqlSession = sqlSessionFactory.openSession();
            //使用sqlSession获得对应的mapper.mapper用来执行sql语句
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.selectById(1);

            System.out.println(user.getUserName());

            sqlSession.commit();
            sqlSession.flushStatements();
            sqlSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
