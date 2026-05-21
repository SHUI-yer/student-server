package com.suiye.studentserver.service.impl;

import com.suiye.studentserver.entity.Student;
import com.suiye.studentserver.mapper.StudentMapper;
import com.suiye.studentserver.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Map<String, Object> getStudentPage(int pageNum, int pageSize, String keyword) {
        int offset = (pageNum - 1) * pageSize;
        List<Student> list;
        int total;

        if (keyword != null && !keyword.isEmpty()) {
            // 注意：这里为了简化，搜索时不带分页，或者可以根据需求实现搜索分页
            list = studentMapper.search(keyword);
            total = list.size();
        } else {
            list = studentMapper.findByPage(offset, pageSize);
            total = studentMapper.count();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    @Override
    public int saveStudent(Student student) {
        if (student.getId() == null) {
            return studentMapper.insert(student);
        } else {
            return studentMapper.update(student);
        }
    }

    @Override
    public int deleteStudent(Integer id) {
        return studentMapper.delete(id);
    }

    @Override
    public Student getStudentById(Integer id) {
        return studentMapper.findById(id);
    }
}
