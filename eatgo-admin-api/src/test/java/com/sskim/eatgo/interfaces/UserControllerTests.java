package com.sskim.eatgo.interfaces;

import com.sskim.eatgo.application.UserService;
import com.sskim.eatgo.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private UserService userService;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    public void list() throws Exception {

        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .email("test@test.com")
                .name("테스터")
                .level(100L)
                .build());

        given(userService.getUsers()).willReturn(users);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("테스터")));
    }

    @Test
    public void create() throws Exception {

        String email = "admin@exmaple.com";
        String name = "Administrator";

        User user = User.builder()
                .email(email)
                .name(name)
                .build();

        given(userService.addUser(email,name)).willReturn(user);

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@exmaple.com\",\"name\":\"Administrator\"}"))
                .andExpect(status().isCreated());

        verify(userService).addUser(email, name);
    }

    @Test
    public void update() throws Exception {

        Long id = 1004L;
        String email = "admin@exmaple.com";
        String name = "Administrator";
        Long level = 100L;

        User user = User.builder()
                .email(email)
                .name(name)
                .level(level)
                .build();

        given(userService.updateUser(id, email,name, level)).willReturn(user);

        mvc.perform(patch("/users/1004")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@exmaple.com\",\"name\":\"Administrator\", \"level\":100}"))
                .andExpect(status().isCreated());

        verify(userService).updateUser(eq(id), eq(email),eq(name), eq(level));
    }

    @Test
    public void deactivate() throws Exception {

        Long id = 1004L;
        String email = "admin@exmaple.com";
        String name = "Administrator";
        Long level = 100L;

        User user = User.builder()
                .email(email)
                .name(name)
                .level(level)
                .build();

        mvc.perform(delete("/users/1004"))
                .andExpect(status().isOk());

        verify(userService).deactivateUser(id);
    }
}