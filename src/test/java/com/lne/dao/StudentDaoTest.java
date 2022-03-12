package com.lne.dao;

import com.lne.common.MyBatisUtil;
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

    //插入方法测试
    @Test
    public void insertStudent() {

            //测试studentDao中的方法
            int i = studentDao.insertStudent(new Student(0,"1001","张三","男",22));
            //事务提交
            sqlSession.commit();
            System.out.println(i);
            assertEquals(1,i);
    }

    //删除方法测试
    @Test
    public void deleteStudent() {
        int i = studentDao.deleteStudent("1001");
        sqlSession.commit();
        System.out.println(i);
        assertEquals(1,i);

    }
    //修改方法测试
    @Test
    public void updateStudent(){
        int i = studentDao.updateStudent(new Student(0,"1001","铁柱","男",33));
        assertEquals(1,i);
        sqlSession.commit();
    }

    // 手动事务管理,当获取一个sqlSession对象就开启了一个事务
    @Test
    public  void handleTransactionTest() {
        try{
            //获取事务对象
            SqlSession sqlSession = MyBatisUtil.getSession();
            //获取接口实现类的一个实列
            StudentDao studentDao =sqlSession.getMapper(StudentDao.class);
            int i = studentDao.updateStudent(new Student(0,"1001","铁柱","男",33));
            assertEquals(1,i);
            sqlSession.commit();

        }catch (Exception e){
            e.printStackTrace();
            //当执行异常，就回滚，防止错误操作数据
            sqlSession.rollback();
        }

    }

    //自动事务管理，适合单条sql
    @Test
    public  void autoTransactionTest() {
        try{
            //获取接口实现类的一个实列
            StudentDao studentDao =MyBatisUtil.getMapper(StudentDao.class);
            int i = studentDao.updateStudent(new Student(0,"1001","铁柱","男",33));
            assertEquals(1,i);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}