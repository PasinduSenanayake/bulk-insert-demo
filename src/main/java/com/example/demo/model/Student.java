package com.example.demo.model;

import com.example.demo.util.CustomInteger;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "student")
public class Student implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1080187374859011413L;

    @Id
    @Column(name = "student_id", unique = true, nullable = false, precision = 10)
    @Type(type = "com.example.demo.util.CustomInteger")
    private CustomInteger studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "student_age",nullable = false)
    private int studentAge;

    public Student(){
        studentId = new CustomInteger();
    }

    public int getStudentAge() {
        return studentAge;
    }

    public Integer getStudentId() {
        return studentId.getValue();
    }

    public String getStudentName() {
        return studentName;
    }

    public void clearPrimaryId() {
        this.studentId.setValue(null);
    }

    public void setStudentAge(int studentAge) {
        this.studentAge = studentAge;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = new CustomInteger(studentId);
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
