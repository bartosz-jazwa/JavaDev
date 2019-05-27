package com.jazwa.javadev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jazwa.javadev.model.CourseClass;
import com.jazwa.javadev.repository.ClassRepo;
import com.jazwa.javadev.service.ClassService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
//@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc
public class CourseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ClassService classService;

    @MockBean
    ClassRepo classRepo;

    @WithMockUser(value = "123")
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

        when(classService.getAll()).thenReturn(classSet);

        mockMvc.perform(get("/classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @WithMockUser(value = "123")
    @Test
    public void getAllClassesNoContent() throws Exception{
        List<CourseClass> classList = new ArrayList<>();
        when(classRepo.findAll()).thenReturn(classList);

        mockMvc.perform(get("/classes"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(value = "123")
    @Test
    public void getClassesByDateOk() throws Exception{
        LocalDateTime courseClassId1 = LocalDateTime.of(2019,6,1,18,0);
        CourseClass courseClass1 = new CourseClass(courseClassId1,"Java","pgs java dev","Jan Kowalski");
        LocalDateTime courseClassId2 = LocalDateTime.of(2019,6,1,19,0);
        CourseClass courseClass2 = new CourseClass(courseClassId2,"Front","pgs front dev","Adam Nowak");

        Set<CourseClass> classSet = new HashSet<>();
        classSet.add(courseClass1);
        classSet.add(courseClass2);
        /*when(classRepo.findCourseClassesByStartTimeIsAfterAndStartTimeIsBefore(LocalDateTime.of(courseClassId1.toLocalDate(), LocalTime.MIN),(LocalDateTime.of(courseClassId1.toLocalDate(),LocalTime.MAX))))
                .thenReturn(classSet);*/

        when(classService.getByDate(courseClass1.getStartTime().toLocalDate())).thenReturn(classSet);

        mockMvc.perform(get("/classes?date=2019-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @WithMockUser(value = "123")
    @Test
    public void getClassesByDateNoContent() throws Exception{
        Set<CourseClass> classSet = new HashSet<>();

        when(classService.getByDate(LocalDate.of(2019,6,1))).thenReturn(classSet);

        mockMvc.perform(get("/classes?date=2019-06-01"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(value = "123")
    @Test
    public void getClassesByDateBadRequest() throws Exception{
        Set<CourseClass> classSet = new HashSet<>();

        when(classService.getByDate(LocalDate.of(2019,6,1))).thenReturn(classSet);

        mockMvc.perform(get("/classes?date=2019.06.01"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "123")
    @Test
    public void getSingleClassOK() throws Exception {
        LocalDateTime classDate = LocalDateTime.of(2019,6,1,18,0);
        CourseClass courseClass1 = new CourseClass(classDate,"Java","pgs java dev","Jan Kowalski");

        when(classService.getById(1L)).thenReturn(Optional.of(courseClass1));
        mockMvc.perform(get("/classes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.title").value("Java"));
    }

    @WithMockUser(value = "123")
    @Test
    public void getSingleClassNotFound() throws Exception{
        when(classService.getById(2L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/classes/2"))
                .andExpect(status().isNotFound());

    }

    @WithMockUser(value = "123")
    @Test
    public void addNewClassOk() throws Exception {
        CourseClass courseClass = new CourseClass();
        //courseClass.setId(1L);
        courseClass.setStartTime(LocalDateTime.now().plusDays(1L).withHour(12).withMinute(0).withSecond(0).withNano(0));
        courseClass.setTitle("Java");
        courseClass.setDescription("from scratch");
        courseClass.setTutor("Mr Bean");

        CourseClass newClass = new CourseClass();
        newClass.setStartTime(LocalDateTime.now().plusDays(1L).withHour(13).withMinute(0).withSecond(0).withNano(0));
        newClass.setTitle("cpp");
        newClass.setDescription("masters");
        newClass.setTutor("Jan Kowalski");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String postContent = objectMapper.writeValueAsString(courseClass);
        String returnContent = objectMapper.writeValueAsString(newClass);

        when(classService.addNewClass(any(CourseClass.class))).thenReturn(Optional.of(courseClass));
        mockMvc.perform(post("/classes")
                .content(postContent)
                .contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(postContent))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "123")
    @Test
    public void addNewClassNotAcceptableWhenServiceReturnsEmpty() throws Exception {
        CourseClass courseClass = new CourseClass();
        //courseClass.setStartTime(LocalDateTime.now().plusDays(1L).withHour(12).withMinute(0).withSecond(0).withNano(0));
        courseClass.setTitle("Java");
        courseClass.setDescription("from scratch");
        courseClass.setTutor("Mr Bean");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String postContent = objectMapper.writeValueAsString(courseClass);

        when(classService.addNewClass(any(CourseClass.class))).thenReturn(Optional.empty());
        mockMvc.perform(post("/classes")
                .content(postContent)
                .contentType(MediaType.APPLICATION_JSON_UTF8))

                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                //.andExpect(content().json(postContent))
                .andExpect(status().isNotAcceptable());
    }

    @WithMockUser(value = "123")
    @Test
    public void addNewClassBadRequestWhenEmptyBody() throws Exception {
        CourseClass courseClass = new CourseClass();
        //courseClass.setStartTime(LocalDateTime.now().plusDays(1L).withHour(12).withMinute(0).withSecond(0).withNano(0));
        courseClass.setTitle("Java");
        courseClass.setDescription("from scratch");
        courseClass.setTutor("Mr Bean");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String postContent = "";

        when(classService.addNewClass(any(CourseClass.class))).thenReturn(Optional.empty());
        mockMvc.perform(post("/classes")
                .content(postContent)
                .contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "123")
    @Test
    public void deleteClassByDateReturnsOk() throws Exception {
        CourseClass courseClass = new CourseClass();
        courseClass.setStartTime(LocalDateTime.of(2019,1,1,12,0));
        courseClass.setTitle("Java");
        courseClass.setDescription("from scratch");
        courseClass.setTutor("Mr Bean");

        CourseClass courseClass1 = new CourseClass();
        courseClass1.setStartTime(LocalDateTime.of(2019,1,1,13,0));
        courseClass1.setTitle("asembler");
        courseClass1.setDescription("from scratch");
        courseClass1.setTutor("Mr Bean");

        Set<CourseClass> classSet = new HashSet<>();
        classSet.add(courseClass);
        classSet.add(courseClass1);

        when(classService.cancelClasses(LocalDate.of(2019,1,1))).thenReturn(classSet);
        mockMvc.perform(delete("/classes?date=2019-01-01")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @WithMockUser(value = "123")
    @Test
    public void deleteClassByDateReturnsBadRequestWhenDateFormatInvalid() throws Exception {
        CourseClass courseClass = new CourseClass();
        courseClass.setStartTime(LocalDateTime.of(2019,1,1,12,0));
        courseClass.setTitle("Java");
        courseClass.setDescription("from scratch");
        courseClass.setTutor("Mr Bean");

        CourseClass courseClass1 = new CourseClass();
        courseClass1.setStartTime(LocalDateTime.of(2019,1,1,13,0));
        courseClass1.setTitle("asembler");
        courseClass1.setDescription("from scratch");
        courseClass1.setTutor("Mr Bean");

        Set<CourseClass> classSet = new HashSet<>();
        classSet.add(courseClass);
        classSet.add(courseClass1);

        when(classService.cancelClasses(LocalDate.of(2019,1,1))).thenReturn(classSet);
        mockMvc.perform(delete("/classes?date=2019.01.01")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "123")
    @Test
    public void deleteClassByIdeReturnsOk() throws Exception {
        CourseClass courseClass = new CourseClass();
        courseClass.setStartTime(LocalDateTime.of(2019,1,1,12,0));
        courseClass.setTitle("Java");
        courseClass.setDescription("from scratch");
        courseClass.setTutor("Mr Bean");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String postContent = objectMapper.writeValueAsString(courseClass);

        when(classService.cancelClass(1L)).thenReturn(Optional.ofNullable(courseClass));
        mockMvc.perform(delete("/classes/1")
                .content(postContent)
                .contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(status().isOk());
    }

    @WithMockUser(value = "123")
    @Test
    public void deleteClassByIdeReturnsNotFound() throws Exception {

        when(classService.cancelClass(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/classes/1"))

                .andExpect(status().isNotFound());
    }

    @WithMockUser(value = "123")
    @Test
    public void updateClassReturnsOk() throws Exception {
        CourseClass courseClass = new CourseClass();
        courseClass.setStartTime(LocalDateTime.of(2019,1,1,12,0));
        courseClass.setTitle("Java");
        courseClass.setDescription("from scratch");
        courseClass.setTutor("Mr Bean");

        courseClass.setId(1L);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String postContent = objectMapper.writeValueAsString(courseClass);

        when(classService.getById(1L)).thenReturn(Optional.ofNullable(courseClass));
        mockMvc.perform(put("/classes/1?date=2019-01-01&time=12:00&title=Java&description=from scratch&tutor=Mr Bean")
                .content(postContent)
                .contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(status().isOk());
    }

    @WithMockUser(value = "123")
    @Test
    public void updateClassReturnsBadRequestWhenInvalidDateFormat() throws Exception {
        CourseClass courseClass = new CourseClass();
        courseClass.setStartTime(LocalDateTime.of(2019,1,1,12,0));
        courseClass.setTitle("Java");
        courseClass.setDescription("from scratch");
        courseClass.setTutor("Mr Bean");

        courseClass.setId(1L);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String postContent = objectMapper.writeValueAsString(courseClass);

        when(classService.getById(1L)).thenReturn(Optional.ofNullable(courseClass));
        mockMvc.perform(put("/classes/1?date=2019.01-01&time=12:00&title=Java&description=from scratch&tutor=Mr Bean")
                .content(postContent)
                .contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "123")
    @Test
    public void updateClassReturnsBadRequestWhenInvalidIdFormat() throws Exception {
        CourseClass courseClass = new CourseClass();
        courseClass.setStartTime(LocalDateTime.of(2019,1,1,12,0));
        courseClass.setTitle("Java");
        courseClass.setDescription("from scratch");
        courseClass.setTutor("Mr Bean");

        courseClass.setId(1L);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String postContent = objectMapper.writeValueAsString(courseClass);

        when(classService.getById(1L)).thenReturn(Optional.ofNullable(courseClass));
        mockMvc.perform(put("/classes/a?date=2019-01-01&time=12:00&title=Java&description=from scratch&tutor=Mr Bean")
                .content(postContent)
                .contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(status().isBadRequest());
    }
}