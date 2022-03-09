package com.lne.dao;

import com.lne.pojo.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * FileName: StudentDao
 * Author:   18311
 * Date:     2022/3/6 21:31
 * Description: dao接口
 */
public interface StudentDao {
    //插入
    public int insertStudent(Student student);
    //删除
    public int deleteStudent(String stuNum);
    //修改
    public int updateStudent(Student student);
     //分页查询多参数的处理，需要两个参数分页开始和每页展示多少
    public List<Student> listStudentByPage(@Param("start")int start,
                                           @Param("pageSize")int pageSize);
}
