package com.lne.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * FileName: Student
 * Author:   18311
 * Date:     2022/3/6 21:25
 * Description: student实体类
 */
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Student {
    private int stuId;
    private String stuNum;
    private String stuName;
    private String stuGender;
    private int stuAge;


}
