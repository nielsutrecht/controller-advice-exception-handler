package com.nibado.example.baseerrorhandler.service.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserControllerTest {
    private final UUID USER_0 = new UUID(0, 0);
    private final UUID USER_UNKNOWN = new UUID(0, 999);
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testGetUserById() throws Exception {
        asyncGet("/user/" + USER_0)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USER_0.toString())))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Johnson")))
                .andExpect(jsonPath("$.dateOfBirth", is("1980-02-19")))
                .andExpect(jsonPath("$.age").isNumber());
    }

    @Test
    public void testGetUserById_UserUnknown() throws Exception {
        asyncGet("/user/" + USER_UNKNOWN)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("USER001")))
                .andExpect(jsonPath("$.message", is("User not found")));
    }

    @Test
    public void testGetUserById_InvalidId() throws Exception {
        mockMvc.perform(get("/user/foo"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("USER903")))
                .andExpect(jsonPath("$.message", is("Argument type mismatch")));
    }

    @Test
    public void testGetUserByHeader() throws Exception {
        asyncGet("/user/me", userHeaders(USER_0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USER_0.toString())))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Johnson")))
                .andExpect(jsonPath("$.dateOfBirth", is("1980-02-19")))
                .andExpect(jsonPath("$.age").isNumber());
    }

    @Test
    public void testGetUserByHeader_UserUnknown() throws Exception {
        asyncGet("/user/me", userHeaders(USER_UNKNOWN))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("USER001")))
                .andExpect(jsonPath("$.message", is("User not found")));
    }

    @Test
    public void testGetUserByHeader_MissingHeader() throws Exception {
        mockMvc.perform(get("/user/me"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("USER901")))
                .andExpect(jsonPath("$.message", is("Missing header in request")));
    }

    @Test
    public void testGetUserByHeader_InvalidHeader() throws Exception {
        mockMvc.perform(get("/user/me").header("user-id", "foo"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("USER903")))
                .andExpect(jsonPath("$.message", is("Argument type mismatch")));
    }

    @Test
    public void testFindUser() throws Exception {
        asyncGet("/user/find?yearOfBirth=1980&limit=100")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(USER_0.toString())))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Johnson")))
                .andExpect(jsonPath("$[0].dateOfBirth", is("1980-02-19")))
                .andExpect(jsonPath("$[0].age").isNumber());
    }

    @Test
    public void testFindUser_All() throws Exception {
        asyncGet("/user/find?limit=100")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void testFindUser_One() throws Exception {
        asyncGet("/user/find?limit=1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testFindUser_BadParam() throws Exception {
        asyncGet("/user/find?yearOfBirth=42&limit=100")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("USER002")))
                .andExpect(jsonPath("$.message", is("Invalid search parameter")));
    }

    @Test
    public void testFindUser_MissingLimit() throws Exception {
        mockMvc.perform(get("/user/find"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("USER904")))
                .andExpect(jsonPath("$.message", is("Missing request parameter")));
    }

    @Test
    public void testPostUser_Get() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code", is("USER902")))
                .andExpect(jsonPath("$.message", is("Method not supported")));
    }

    @Test
    public void testPostUser_NotImplemented() throws Exception {
        mockMvc.perform(post("/user").content("{}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code", is("USER900")))
                .andExpect(jsonPath("$.message", is("Internal server error")));
    }

    private static HttpHeaders userHeaders(UUID id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-id", id.toString());

        return headers;
    }

    private ResultActions asyncGet(String route) throws Exception {
        return asyncGet(route, null);
    }

    private ResultActions asyncGet(String route, HttpHeaders headers) throws Exception {
        MockHttpServletRequestBuilder builder = get(route);

        if(headers != null) {
            builder.headers(headers);
        }

        MvcResult resultActions = mockMvc.perform(builder)
                .andExpect(request().asyncStarted())
                .andReturn();

        return mockMvc.perform(asyncDispatch(resultActions));
    }

}