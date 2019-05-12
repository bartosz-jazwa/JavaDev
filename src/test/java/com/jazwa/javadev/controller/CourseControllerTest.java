package com.jazwa.javadev.controller;

import com.jazwa.javadev.model.CourseClass;
import com.jazwa.javadev.repository.ClassRepo;
import com.jazwa.javadev.service.ClassService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.collection.IsCollectionWithSize.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class CourseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ClassService classService;

    @MockBean
    ClassRepo classRepo;

    @Test
    public void getAllClassesOk() throws Exception {
        LocalDateTime courseClassId1 = LocalDateTime.of(2019,6,1,18,0);
        CourseClass courseClass1 = new CourseClass(courseClassId1,"Java","pgs java dev","Jan Kowalski");
        LocalDateTime courseClassId2 = LocalDateTime.of(2019,6,2,19,0);
        CourseClass courseClass2 = new CourseClass(courseClassId2,"Front","pgs front dev","Adam Nowak");

        Set<CourseClass> classSet = new HashSet<>();
        classSet.add(courseClass1);
        classSet.add(courseClass2);

        List<CourseClass> classList = new ArrayList<>();
        classList.add(courseClass1);
        classList.add(courseClass2);

        when(classRepo.findAll()).thenReturn(classList);

        mockMvc.perform(get("/classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    public void getAllClassesNoContent() throws Exception{
        List<CourseClass> classList = new ArrayList<>();
        when(classRepo.findAll()).thenReturn(classList);

        mockMvc.perform(get("/classes"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getClassesByDateOk() throws Exception{
        LocalDateTime courseClassId1 = LocalDateTime.of(2019,6,1,18,0);
        CourseClass courseClass1 = new CourseClass(courseClassId1,"Java","pgs java dev","Jan Kowalski");
        LocalDateTime courseClassId2 = LocalDateTime.of(2019,6,1,19,0);
        CourseClass courseClass2 = new CourseClass(courseClassId2,"Front","pgs front dev","Adam Nowak");

        Set<CourseClass> classSet = new HashSet<>();
        classSet.add(courseClass1);
        classSet.add(courseClass2);

        when(classService.findByDate(courseClass1.getStartTime().toLocalDate())).thenReturn(classSet);

        mockMvc.perform(get("/classes/2019-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }
    @Test
    public void getClassesByDateNoContent() throws Exception{
        Set<CourseClass> classSet = new HashSet<>();

        when(classService.findByDate(LocalDate.of(2019,6,1))).thenReturn(classSet);

        mockMvc.perform(get("/classes/2019-06-01"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getSingleClass() {
    }

    @Test
    public void addNewClass() {
    }

    @Test
    public void deleteClass() {
    }

    @Test
    public void deleteClass1() {
    }

    @Test
    public void updateClass() {
    }
}