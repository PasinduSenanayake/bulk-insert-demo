package com.example.demo;

import com.example.demo.dao.StudentDao;
import com.example.demo.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    private StudentDao studentDao;

    @Override
    public void run(ApplicationArguments args) {
        Student studentA = new Student();
        studentA.setStudentName("testA");
        studentA.setStudentAge(20);

        Student studentB = new Student();
        studentB.setStudentName("testB");
        studentB.setStudentAge(25);

        studentDao.insertStudents(Arrays.asList(studentA, studentB));
    }
}