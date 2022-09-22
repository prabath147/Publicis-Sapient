package com.pharmacy.authservice.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.pharmacy.authservice.model.ERole;
import com.pharmacy.authservice.model.Role;
import com.pharmacy.authservice.model.UserMaster;
import com.pharmacy.authservice.repository.RoleRepository;
import com.pharmacy.authservice.repository.UserMasterRepository;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {RoleController.class})
@ExtendWith(SpringExtension.class)
class RoleControllerTest {
    @Autowired
    private RoleController roleController;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserMasterRepository userMasterRepository;


    @Test
    void testApproveManager() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_CUSTOMER);
        when(roleRepository.findByName((ERole) any())).thenReturn(role);

        UserMaster userMaster = new UserMaster();
        userMaster.setEmail("jane.doe@example.org");
        userMaster.setId(123L);
        userMaster.setPassword("iloveyou");
        userMaster.setRoles(new HashSet<>());
        userMaster.setUsername("janedoe");
        Optional<UserMaster> ofResult = Optional.of(userMaster);

        UserMaster userMaster1 = new UserMaster();
        userMaster1.setEmail("jane.doe@example.org");
        userMaster1.setId(123L);
        userMaster1.setPassword("iloveyou");
        userMaster1.setRoles(new HashSet<>());
        userMaster1.setUsername("janedoe");
        when(userMasterRepository.save((UserMaster) any())).thenReturn(userMaster1);
        when(userMasterRepository.findById((Long) any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/role/approve/{id}", 123L);
        MockMvcBuilders.standaloneSetup(roleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Manager has been approved.\"}"));
    }


    @Test
    void testApproveManager2() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_CUSTOMER);
        when(roleRepository.findByName((ERole) any())).thenReturn(role);

        UserMaster userMaster = new UserMaster();
        userMaster.setEmail("jane.doe@example.org");
        userMaster.setId(123L);
        userMaster.setPassword("iloveyou");
        userMaster.setRoles(new HashSet<>());
        userMaster.setUsername("janedoe");
        when(userMasterRepository.save((UserMaster) any())).thenReturn(userMaster);
        when(userMasterRepository.findById((Long) any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/role/approve/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(roleController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("User not found"));
    }


    @Test
    void testGetAllRoles() throws Exception {
        when(roleRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/role/");
        MockMvcBuilders.standaloneSetup(roleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testGetAllRoles2() throws Exception {
        when(roleRepository.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/role/");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(roleController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}

