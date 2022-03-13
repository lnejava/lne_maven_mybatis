# MyBatis

> 本次学习笔记源码地址：
>
> [https://github.com/lnejava/lne_maven_mybatis](https://github.com/lnejava/lne_maven_mybatis)

## 一、MyBatis简介

### 1.1框架概念

> 框架，就是软件的半成品，完成了软件开发中的通用操作，只需要很少或者不用进行加工就能实现特定的功能，从而简化开发步骤，提高开发效率

### 1.2常用框架

- MVC框架：简化了servlet的开发步骤
  - struts2
  - springMVC
- 持久层框架：完成数据库操作的框架
  - apache DBUtils
  - Hibernet
  - spring JPA
  - MyBatis
- 胶水框架：spring--整合前端和数据库

### 1.3MyBatis介绍

>  MyBatis是一个半自动的ORM框架
>
> ORM（Object Relational Mapping）：对象关系映射，将java中的一个对象与数据表中的一行记录对应。提供了实体类与数据表的映射关系，通过映射文件的配置，实现对象的持久化。

- MyBatis的前身是Ibatis

MyBatis的特定：

- 支持自定义SQLA，存储过程
- 对原有原有的JDBC进行封装，几乎消除了原有的JDBC代码，让开发者只需关注SQL本身
- 支持XML和注解配置

## 二、MyBatis框架部署

将框架引入到项目中

### 2.1 创建Maven项目

- java工程
- web工程

### 2.2 在项目中添加依赖

- 在pom中添加依赖
  - mybatis
  - mysql driver

2.3 创建mybatis配置文件

- 在resource文件下创建mybatis配置文件

- 在配置文件中添加数据库连接信息tag为environments的environment中

- ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
      
  <!--在environments配置数据库连接信息-->
  <!--    在environments标签中可以定义多个environment，每个environment标签可以定义一套连接信息
  -->
  <!--    default表示使用那一套连接信息environment-->
      <environments default="mysql">
          <environment id="mysql">
  <!--  transactionManager用于数据库管理方式-->
              <transactionManager type="JDBC"></transactionManager>
  <!-- dataSource用来配置数据连接信息-->
              <dataSource type=""></dataSource>
          </environment>
          <environment id="oracle">
              <transactionManager type=""></transactionManager>
              <dataSource type="POOLED">
                  <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                  <property name="url" value="192.168.230.175:30336/db_2022_mybatis?serverTimeZone=UTC"/>
                  <property name="username" value="root"/>
                  <property name="password" value="123456"/>
              </dataSource>
                
          </environment>
      </environments>
  </configuration>
  
  ```

  ## 三、MyBatis框架的使用

  ### 3.1 创建数据表

  ```mysql
  create table tb_students(
  sid int primary key auto_increment,
  stu_num char(5) not null unique,
  stu_name varchar(20) not null,
  stu_gender char(2) not null,
  stu_age int not null
  );
  ```

  ### 3.2 创建实体类

  ### 3.3 创建dao接口，定义操作方法

  ```java
  public interface StudentDao {
      //插入
      public int insertStudent(Student student);
  
      //删除
      public int deleteStudent(String stuNum);
  }
  ```

  

  ### 3.4 创建dao接口的映射文件，相当于接口实现类

  - 在resource目录下新建文件夹mappers
  - 在mappers中信息配置文件
  - 

```xml
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<!--mapper文件相当于DAO接口的实现类，namespace属性要指定 要实现的dao接口的全限定名-->
<mapper namespace="com.lne.dao.StudentDao">
    <insert id="insertStudent" parameterType="com.lne.pojo.Student">
        insert into tb_students(stu_num,stu_name,stu_gender,stu_age)
        values(#{stuNum},#{stuName},#{stuGender},#{stuAge})
    </insert>
    <delete id="deleteStudent">
        delete from tb_students where stu_num = #{stuNum}
    </delete>
</mapper>
```

3.5 将映射文件添加到主配置文件中

```xml
  <mappers>
        <mapper resource="mappers/StudentMapper.xml"></mapper>
    </mappers>
```



## 四、单元测试

- 添加junit依赖

- 生成会话对象（SqlSession），根据会话对象调用getMapper（）方法生成dao接口实例对象

- dao接口对象调用对应的方法

- 手动提交事务SqlSession.commit（）

- ```java
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
  ```

  

## 五、MyBatis的CRUD操作

> 案例：学生信息的增删改查操作

### 5.1 添加操作

***对返回的结果进行映射***

> 在mapper（xml）中可以设置属性resultType=”返回参数类型“，即封装查询结果到对象的实体类--不可省略，因为实体对象可能不止一个
>
> resultSets 指定当前操作返回集合类型java.util.List或set（自动识别，通常可省略）
>
> resultMap 标签，可用来定义返回结果个实体对象的映射关系
>
> ```xml
> <!--    resultMap 定义查询结果字段与实体类属性的对应关系-->
>     <resultMap id="studentMap" type="com/lne/pojo/Student">
>         <id column="sid" property="stuId"/>
>         <result column="stu_num" property="stuNum"/>
>         <result column="stu_name" property="stuName"/>
>         <result column="stu_gender" property="stuGender"/>
>         <result column="stu_age" property="stuAge"/>
>     </resultMap>
> 
> ```
>
> ```xml
>     <select id="listStudentByPage" resultMap="studentMap">
>         select sid,stu_num,stu_gender,stu_age
>         from tb_student
>         limit #{start},#{pageSize}
>     </select>
> 
> ```
>
> 

### 5.2 删除操作

。。。

### 5.3 分页查询

1. mybaits操作参数为一个，其参数类型为基本数据类型和字符串类型，在mapper（xml）中#{key}，key可以随意写

2. 参数为一个，类型为map类型时，mapper中的参数为Map的key

   ```
   Map map = new hashMap()
   map.put("start":0)
   map.put("pageSize":5)
   ```

3. 当参数为多个是使用注解@param（”key“）类型 参数，key对应参数，mapper中使用ke

   ```java
        //分页查询多参数的处理，需要两个参数分页开始和每页展示多少
       public List<Student> listStudentByPage(@Param("start")int start,
                                              @Param("pageSize")int pageSize);
   ```

   

   ```xml
       <select id="listStudentByPage" resultMap="studentMap">
           select sid,stu_num,stu_gender,stu_age
           from tb_student
           limit #{start},#{pageSize}
       </select>
   
   ```

   

4. 参数为多个时，直接可以在mapper中使用mybatis内置属性arg或param，arg从0开始，param从1开始，例如arg0和param1

### 5.4 插入数据操作回填生成的主键

***只有添加操作有***

```xml
<!--    useGeneratedKeys="true" 设置添加操作是否需要回填生成的主键-->
<!--     keyProperty="stuId" 设置回填主键值赋值到参数对象的那个属性-->
    <insert id="insertStudent" parameterType="com.lne.pojo.Student" useGeneratedKeys="true" keyProperty="stuId">
        insert into tb_students(stu_num,stu_name,stu_gender,stu_age)
        values(#{stuNum},#{stuName},#{stuGender},#{stuAge})
    </insert>
```

六、封装MyBatis创建会话对象工具类

```java
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

```

## 七、事务管理

> 当获取一个Sqlsession对象，就开启了一个事务

### 7.1 手动事务管理

多适用于同时执行多条相互关联的sql

```java
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

```

### 7.2 自动事务管理

适用于单挑sql执行

```java
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

```

## 八、MyBatis主配置文件

> mybatis-config.xml是mybatis框架得主配置文件，主要用于配置mybatis数据源和属性信息

### 8.1 properties标签

> 用于设置键值对，或者加载属性文件

- 在resources目录下创建jdbc.properties文件，配置键值对如下：

  ```properties
  driver=com.mysql.cj.jdbc.Driver
  url=jdbc:mysql://192.168.230.135:30336/db_2022_mybatis?serverTimeZone=UTC
  username=root
  password=123456
  ```

- 在mybatis-config.xml中通过properties标签引用

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
  <!--    引用配置文件-->
      <properties resource="jdbc.properties"></properties>
  
  <!--在environments配置数据库连接信息-->
  <!--    在environments标签中可以定义多个environment，每个environment标签可以定义一套连接信息
  -->
  <!--    default表示使用那一套连接信息environment-->
      <environments default="mysql">
          <environment id="mysql">
  <!--  transactionManager用于数据库管理方式-->
              <transactionManager type="JDBC"></transactionManager>
  <!-- dataSource用来配置数据连接信息-->
              <dataSource type="POOLED">
                  <property name="driver" value="${driver}"/>
                  <property name="url" value="${url}"/>
                  <property name="username" value="${username}"/>
                  <property name="password" value="${password}"/>
  
              </dataSource>
          </environment>
  
      </environments>
  
      <mappers>
          <mapper resource="mappers/StudentMapper.xml"></mapper>
      </mappers>
  </configuration>
  
  ```

  ### 8.2 setting标签

  ```xml
      <settings>
  <!--        开启二级缓存-->
          <setting name="cacheEnabled" value="true"/>
  <!--        开启延迟加载-->
          <setting name="lazyLoadingEnabled" value="true"/>
      </settings>
  
  ```

  

### 8.3 typeAliases标签

```xml
<!--    用于给实体类取名，在映射文件中可以直接使用别名来代替全类名-->
    <typeAliases>
        <typeAlias type="com.lne.pojo.Student" alias="Student"></typeAlias>
    </typeAliases>
```

### 8.4 plugins标签

```xml
<!--    定义插件，例如分页插件-->
    <plugins>
        <plugin interceptor=""></plugin>
    </plugins>

```

### 8.5 environments标签

```xml
<!--在environments配置数据库连接信息-->
<!--    在environments标签中可以定义多个environment，每个environment标签可以定义一套连接信息
-->
<!--    default表示使用那一套连接信息environment-->
    <environments default="mysql">
        <environment id="mysql">
<!-- 
 transactionManager用于数据库管理方式
 type="JDBC" 可以进行事务的提交和回滚
 type="MANAGED" 依赖容器完成事务管理，本身不进行事务提交和回滚操作
-->
            <transactionManager type="JDBC"></transactionManager>
<!-- dataSource用来配置数据连接信息  UNPOOLED|POOLED|JNDI-->
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>

    </environments>

</configuration>

```
## 9、mapper映射文件

## 10、分页插件

> 分页插件是一个独立于MaBatis框架之外的第三方插件
>
> PageHelper

### 10.1 添加依赖

```xml
<!--      PageHelper  -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>5.3.0</version>
        </dependency>
```

### 10.2 配置插件

> 在主配置文件中通过plugins标签设置

```xml
<!--    定义插件，例如分页插件-->
    <plugins>
        <plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
    </plugins>
```

```java
    //分页查询
    @Test
    public void pageHelperTest(){
        StudentDao studentDao = MyBatisUtil.getMapper(StudentDao.class);
        PageHelper.startPage(2,3);	// 必须将拦截器定义在查询之前
        List<Student> list = studentDao.listStudentsByPage();
        PageInfo<Student> pageInfo = new PageInfo<>(list);
        List<Student> list1 = pageInfo.getList();
        list1.forEach((lis)->{
            System.out.println(lis);
        });
    }

```
