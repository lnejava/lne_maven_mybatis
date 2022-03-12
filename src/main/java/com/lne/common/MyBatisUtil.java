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

    //提供公有的获取手动管理事务对象的方法
    public static SqlSession getSession(){
        return getSession(false);
    }
    //让生成事务对象访问私有化
    private static SqlSession getSession(Boolean isAuto){
        SqlSession sqlSession = local.get();
        if(sqlSession == null){
            return factory.openSession(isAuto);      //是否开启自动事务管理，默认为false
        }
        return sqlSession;
    }

    public static <T extends Object>T getMapper(Class<T> c){
        /**
         *
         * 功能描述: 开启自动事务事务管理
         *  每执行一条sql都会自动提交，如果需要同时多条sql执行关联的话，可能会导致有sql成功，有的失败，是操作不一致，这时使用手动事务管理
         *
         * @param: [c]
         * @return: T
         * @date: 2022/3/12 10:41
         */
        SqlSession sqlSession = getSession(true);
        return sqlSession.getMapper(c);
    }
}
