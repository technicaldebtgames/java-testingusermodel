package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class) // this is a web testing environment
public class UserControllerTest
{

    @Autowired
    private MockMvc mockMvc; // allows us to mock up "the web"

    // note the mockbean instead of autowired
    @MockBean
    private UserService userService;

    private List<User> userList = new ArrayList<>(); // have to assume the model works correctly

    @Before
    public void setUp() throws Exception
    {
        //userService.deleteAll();

        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);


        // admin, data, user
        User u1 = new User("admin",
            "password",
            "admin@lambdaschool.local");
        u1.setUserid(1);

        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));
        u1.getRoles().add(new UserRoles(u1, r3));

        Useremail ue1 = new Useremail(u1, "admin@email.local");
        Useremail ue2 = new Useremail(u1, "admin@mymail.local");

        ue1.setUseremailid(1);
        ue2.setUseremailid(2);

        u1.getUseremails()
            .add(ue1);
        u1.getUseremails()
            .add(ue2);


        // data, user
        User u2 = new User("cinnamon",
            "1234567",
            "cinnamon@lambdaschool.local");
        u2.setUserid(2);

        u2.getRoles().add(new UserRoles(u2, r2));
        u2.getRoles().add(new UserRoles(u2, r3));

        Useremail ue3 = new Useremail(u2, "cinnamon@mymail.local");
        Useremail ue4 = new Useremail(u2, "hops@mymail.local");
        Useremail ue5 = new Useremail(u2, "bunny@email.local");

        ue3.setUseremailid(3);
        ue4.setUseremailid(4);
        ue5.setUseremailid(5);

        u2.getUseremails()
            .add(ue3);
        u2.getUseremails()
            .add(ue4);
        u2.getUseremails()
            .add(ue5);


        // user
        User u3 = new User("barnbarn",
            "ILuvM4th!",
            "barnbarn@lambdaschool.local");
        u3.setUserid(3);

        u3.getRoles().add(new UserRoles(u3, r2));

        Useremail ue6 = new Useremail(u3, "barnbarn@email.local");

        ue6.setUseremailid(6);

        u3.getUseremails()
            .add(ue6);

        User u4 = new User("puttat",
            "password",
            "puttat@school.lambda");
        u4.setUserid(4);
        u4.getRoles().add(new UserRoles(u4, r2));

        User u5 = new User("misskitty",
            "password",
            "misskitty@school.lambda");
        u5.setUserid(5);
        u5.getRoles().add(new UserRoles(u5, r2));

        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void listAllUsers() throws Exception // mockMvc.perform can throw an exception so tell java this can throw exceptions
    {
        String apiUrl = "/users/users"; // endpoint to test
        Mockito.when(userService.findAll()).thenReturn(userList); // when this method is called return this list

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON); // a get request at endpoint gets back app/json
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper(); // this is a manual way to invoke jackson
        String er = mapper.writeValueAsString(userList);

        assertEquals(er, tr); // compare expected result to test result
    }

    @Test
    public void getUserById() throws Exception
    {
        String apiUrl = "/users/user/1";
        Mockito.when(userService.findUserById(1)).thenReturn(userList.get(1));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON); // a get request at endpoint gets back app/json
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper(); // this is a manual way to invoke jackson
        String er = mapper.writeValueAsString(userList.get(1));

        assertEquals(er, tr); // compare expected result to test result
    }

    @Test // don't need to use expected (like in service)
    public void getUserByIdNoResult() throws Exception
    {
        String apiUrl = "/users/10";
        Mockito.when(userService.findUserById(10)).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON); // a get request at endpoint gets back app/json
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper(); // this is a manual way to invoke jackson
        String er = ""; // expected "null" return of empty string

        assertEquals(er, tr); // compare expected result to test result
    }

    @Test
    public void getUserByName()
    {
    }

    @Test
    public void getUserLikeName()
    {
    }

    @Test
    public void addNewUser() throws Exception
    {
        String apiUrl = "/users/user";

        Role r1 = new Role("testadmin");
        Role r2 = new Role("testuser");
        Role r3 = new Role("testdata");

        r1.setRoleid(20);
        r2.setRoleid(21);
        r3.setRoleid(22);

        User u1 = new User("testadmin",
            "testpassword",
            "testadmin@lambdaschool.local");
        u1.setUserid(20);

        u1.getRoles().add(new UserRoles(u1, r1));
        u1.getRoles().add(new UserRoles(u1, r2));
        u1.getRoles().add(new UserRoles(u1, r3));

        Useremail ue1 = new Useremail(u1, "testadmin@email.local");
        Useremail ue2 = new Useremail(u1, "testadmin@mymail.local");

        ue1.setUseremailid(20);
        ue2.setUseremailid(21);

        u1.getUseremails()
            .add(ue1);
        u1.getUseremails()
            .add(ue2);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u1);

        Mockito.when(userService.save(any(User.class))).thenReturn(u1);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userString);

        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void updateFullUser()
    {
    }

    @Test
    public void updateUser()
    {
    }

    @Test
    public void deleteUserById()
    {
    }
}