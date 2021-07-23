package com.example.demo.dao;

import com.example.demo.model.Student;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StudentDao {

    @PersistenceContext(unitName = "datasourceEntityManager")
    @Qualifier("datasourceEntityManager")
    private EntityManager entityManager;

    @Transactional("datasourceTransactionManager")
    public void insertStudents(List<Student> students) {
        for(Student student : students){
            entityManager.persist(student);
        }

    }


}
