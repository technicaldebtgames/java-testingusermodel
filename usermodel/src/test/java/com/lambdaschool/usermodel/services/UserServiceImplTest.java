package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

// testing service implementation using the db, assuming such that models and repos work
@RunWith(SpringRunner.class) // tells junit that we are running a spring application
@SpringBootTest(classes = UserModelApplication.class) // use this since it's just java testing
public class UserServiceImplTest // note the suffix Test naming convention (some might put as prefix)
{

    @Autowired
    private UserService userService; // need to use our service so we bring this in like in the normal impl

    @Before
    public void setUp() throws Exception
    {
        // mocks -> fake data
        // stubs -> fake methods
        // the above naming distinction might matter in other languages, but java has used mocks as term for both
        MockitoAnnotations.initMocks(this); // tell mokito to init this class

        /*
        // h2 is going to use the same algo to create the data, so once it runs once we know IDs/data/etc.
        // we are making an assumption that findAll works here in order to get the data
        // grabbing all the users for any data we might need
        List<User> userList = userService.findAll();

        // printing the user data we are interested in
        for (User u : userList)
        {
            System.out.println(u.getUserid() + " " + u.getUsername() + " " + u.getPassword());
        }
        */
        /*
        Results from above:
            4 admin password
            7 cinnamon 1234567
            11 barnbarn ILuvM4th!
            13 puttat password
            14 misskitty password
        */
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void findUserById()
    { // pretty sure the issue is that data is persisting... i think? how to fix?
        assertEquals("admin", userService.findUserById(4).getUsername());
    }

    // we need a test that will also handle not finding a user, since this method might
    // return an exception instead of a value
    @Test(expected = EntityNotFoundException.class) // we are expecting an exception, list the class here
    public void findUserByIdNoResult()
    {
        assertEquals("admin", userService.findUserById(50).getUsername());
    }

    @Test
    public void findByNameContaining()
    {
        // this is saying "I expect the size of the returned value to be 1"
        assertEquals(1, userService.findByNameContaining("admin").size());
    }

    @Test
    public void findByNameContainingNoResult()
    {
        // this is saying "I expect the size of the returned value to be 0"
        assertEquals(0, userService.findByNameContaining("zzzzzzzzz").size());
    }

    @Test
    public void findAll()
    {
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void delete()
    {
        userService.delete(14);
        assertEquals(4, userService.findAll().size());
    }

    @Test
    public void findByName()
    {
        assertEquals("admin", userService.findByName("admin").getUsername());
    }

    @Test(expected = EntityNotFoundException.class) // throws error since no user found that matches
    public void findByNameNoResult()
    {
        assertEquals("zzzzz", userService.findByName("zzzzz").getUsername());
    }

    // for post functionality of save method
    @Test
    public void saveAdd()
    {
        User u = new User("testuser", "password", "testuser@lambda.com");
        Role r = new Role("admin"); // create roles
        r.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);
        UserRoles ur = new UserRoles(u, r); // create userroles
        UserRoles ur2 = new UserRoles(u, r2);
        Set<UserRoles> sur = new HashSet<>();
        sur.add(ur); // put userroles in a set
        sur.add(ur2);
        u.setRoles(sur); // pass set to user
        Useremail ue = new Useremail(u, "testemail1@lambda.com"); // create useremails
        Useremail ue2 = new Useremail(u, "testemail2@lambda.com");
        List<Useremail> lue = new ArrayList<>();
        lue.add(ue); // add useremails to list
        lue.add(ue2);
        u.setUseremails(lue); // set useremails in user

        User savedUser = userService.save(u); // save user via service

        assertNotNull(savedUser); // test to make sure the object is there, tho I rly think this is redundant?
        assertEquals("testuser", savedUser.getUsername());

    }

    // for put functionality of save method
    @Test
    public void savePut() // this is failing only on full class run? not sure why yet.
    {
        User u = new User("testuser", "password", "testuser@lambda.com");
        u.setUserid(4); // this is the seed admin userid
        Role r = new Role("admin"); // create roles
        r.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);
        UserRoles ur = new UserRoles(u, r); // create userroles
        UserRoles ur2 = new UserRoles(u, r2);
        Set<UserRoles> sur = new HashSet<>();
        sur.add(ur); // put userroles in a set
        sur.add(ur2);
        u.setRoles(sur); // pass set to user
        Useremail ue = new Useremail(u, "testemail1@lambda.com");
        Useremail ue2 = new Useremail(u, "testemail2@lambda.com");
        List<Useremail> lue = new ArrayList<>();
        lue.add(ue);
        lue.add(ue2);
        u.setUseremails(lue);

        User savedUser = userService.save(u); // save user via service

        assertNotNull(savedUser); // test to make sure the object is there, tho I rly think this is redundant?
        assertEquals("testuser", savedUser.getUsername());
    }

    // patch functionality
    @Test
    public void update()
    {
        User u = new User("testuser", "password", "testuser@lambda.com");
        u.setUserid(4); // this is the seed admin userid
        Role r = new Role("admin"); // create roles
        r.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);
        UserRoles ur = new UserRoles(u, r); // create userroles
        UserRoles ur2 = new UserRoles(u, r2);
        Set<UserRoles> sur = new HashSet<>();
        sur.add(ur); // put userroles in a set
        sur.add(ur2);
        u.setRoles(sur); // pass set to user
        Useremail ue = new Useremail(u, "testemail1@lambda.com");
        Useremail ue2 = new Useremail(u, "testemail2@lambda.com");
        List<Useremail> lue = new ArrayList<>();
        lue.add(ue);
        lue.add(ue2);
        u.setUseremails(lue);

        User savedUser = userService.update(u, 4); // update user via service

        assertNotNull(savedUser); // test to make sure the object is there, tho I rly think this is redundant?
        assertEquals("testuser", savedUser.getUsername());
    }

    @Test
    public void deleteAll()
    {
        userService.deleteAll();
        assertEquals(0, userService.findAll().size());
    }
}