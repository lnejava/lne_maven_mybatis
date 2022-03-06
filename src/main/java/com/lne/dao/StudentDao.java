package com.lne.dao;

import com.lne.pojo.Student;

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
}
