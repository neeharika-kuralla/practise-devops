package com.example.StudentManagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Students")
public class StudentController {
    
    private List<Student> students=new  ArrayList<>();

    public StudentController(){
        students.add(new Student(1,"Neeharika"));
        students.add(new Student(2,"Riya"));
    }

    @GetMapping
    public List<Student> getStudents(){
        return students;
    }

    @PostMapping
    public Student addStudents(@RequestBody Student stu){
        students.add(stu);
        return stu;
    }
}
