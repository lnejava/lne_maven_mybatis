package com.lne.common;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * FileName: MyBatisUtil
 * Author:   fengsulin
 * Date:     2022/3/8 23:39
 * Description: MyBatis事务和会话对象操作工具类
 */
public class MyBatisUtil {

    private static  SqlSessionFactory factory;
    private static final ThreadLocal<SqlSession> local = new ThreadLocal();
    static {
        try {
            InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            //会话工厂
            factory = builder.build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static SqlSession getSession(){
        SqlSession sqlSession = local.get();
        if(sqlSession == null){
            return factory.openSession();
        }
        return sqlSession;
    }

    public static <T extends Object>T getMapper(Class<T> c){
        SqlSession sqlSession = getSession();
        return sqlSession.getMapper(c);
    }
}
