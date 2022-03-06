package com.lne.dao;

import com.lne.pojo.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * FileName: StudentDaoTest
 * Author:   18311
 * Date:     2022/3/6 22:06
 * Description:
 */
public class StudentDaoTest {
    //加载mybatis配置文件
    InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
    //会话工厂
    SqlSessionFactory factory = builder.build(in);
    //会话（连接）,SqlSession表示mybatis与数据库之间的会话
    SqlSession sqlSession = factory.openSession();
    StudentDao studentDao = sqlSession.getMapper(StudentDao.class);

    public StudentDaoTest() throws IOException {
    }

    /**
     * 功能描述: <br>
     *想要测试方法必须要获取实例化对象，但是接口不能内实例化，同时dao接口以xml配置的形式实现，只能通过会话获取dao对象
     * @param:
     * @return:
     * @date: 2022/3/6 22:17
     */

    @Test
    public void insertStudent() {

            //测试studentDao中的方法
            int i = studentDao.insertStudent(new Student(0,"1001","张三","男",22));
            //事务提交
            sqlSession.commit();
            System.out.println(i);
    }

    @Test
    public void deleteStudent() {
    }
}