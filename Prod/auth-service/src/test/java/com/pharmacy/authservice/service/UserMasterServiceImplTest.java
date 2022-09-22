package com.pharmacy.authservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pharmacy.authservice.model.ERole;
import com.pharmacy.authservice.model.Role;
import com.pharmacy.authservice.model.UserMaster;
import com.pharmacy.authservice.repository.UserMasterRepository;

import java.util.Collection;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
class UserMasterServiceImplTest {
    @MockBean
    private UserMasterRepository userMasterRepository;

    @Autowired
    private UserMasterServiceImpl userMasterServiceImpl;


    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        UserMaster userMaster = new UserMaster();
        userMaster.setEmail("jane.doe@example.org");
        userMaster.setId(123L);
        userMaster.setPassword("iloveyou");
        userMaster.setRoles(new HashSet<>());
        userMaster.setUsername("janedoe");
        when(userMasterRepository.findByUsername((String) any())).thenReturn(userMaster);
        UserDetails actualLoadUserByUsernameResult = userMasterServiceImpl.loadUserByUsername("janedoe");
        assertTrue(actualLoadUserByUsernameResult.getAuthorities().isEmpty());
        assertEquals("janedoe", actualLoadUserByUsernameResult.getUsername());
        assertEquals("iloveyou", actualLoadUserByUsernameResult.getPassword());
        assertEquals(123L, ((UserMasterImpl) actualLoadUserByUsernameResult).getId().longValue());
        assertEquals("jane.doe@example.org", ((UserMasterImpl) actualLoadUserByUsernameResult).getEmail());
        verify(userMasterRepository).findByUsername((String) any());
    }


    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_CUSTOMER);

        HashSet<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        UserMaster userMaster = new UserMaster();
        userMaster.setEmail("jane.doe@example.org");
        userMaster.setId(123L);
        userMaster.setPassword("iloveyou");
        userMaster.setRoles(roleSet);
        userMaster.setUsername("janedoe");
        when(userMasterRepository.findByUsername((String) any())).thenReturn(userMaster);
        UserDetails actualLoadUserByUsernameResult = userMasterServiceImpl.loadUserByUsername("janedoe");
        Collection<? extends GrantedAuthority> authorities = actualLoadUserByUsernameResult.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("jane.doe@example.org", ((UserMasterImpl) actualLoadUserByUsernameResult).getEmail());
        assertEquals("iloveyou", actualLoadUserByUsernameResult.getPassword());
        assertEquals("janedoe", actualLoadUserByUsernameResult.getUsername());
        assertEquals(123L, ((UserMasterImpl) actualLoadUserByUsernameResult).getId().longValue());
        assertEquals("ROLE_CUSTOMER", ((List<? extends GrantedAuthority>) authorities).get(0).getAuthority());
        verify(userMasterRepository).findByUsername((String) any());
    }


    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_CUSTOMER);

        Role role1 = new Role();
        role1.setId(1);
        role1.setName(ERole.ROLE_CUSTOMER);

        HashSet<Role> roleSet = new HashSet<>();
        roleSet.add(role1);
        roleSet.add(role);

        UserMaster userMaster = new UserMaster();
        userMaster.setEmail("jane.doe@example.org");
        userMaster.setId(123L);
        userMaster.setPassword("iloveyou");
        userMaster.setRoles(roleSet);
        userMaster.setUsername("janedoe");
        when(userMasterRepository.findByUsername((String) any())).thenReturn(userMaster);
        UserDetails actualLoadUserByUsernameResult = userMasterServiceImpl.loadUserByUsername("janedoe");
        Collection<? extends GrantedAuthority> authorities = actualLoadUserByUsernameResult.getAuthorities();
        assertEquals(2, authorities.size());
        assertEquals("jane.doe@example.org", ((UserMasterImpl) actualLoadUserByUsernameResult).getEmail());
        assertEquals("iloveyou", actualLoadUserByUsernameResult.getPassword());
        assertEquals("janedoe", actualLoadUserByUsernameResult.getUsername());
        assertEquals(123L, ((UserMasterImpl) actualLoadUserByUsernameResult).getId().longValue());
        assertEquals("ROLE_CUSTOMER", ((List<? extends GrantedAuthority>) authorities).get(0).toString());
        assertEquals("ROLE_CUSTOMER", ((List<? extends GrantedAuthority>) authorities).get(1).toString());
        verify(userMasterRepository).findByUsername((String) any());
    }


    @Test
    void testLoadUserByUsername4() throws UsernameNotFoundException {
        when(userMasterRepository.findByUsername((String) any()))
                .thenThrow(new UsernameNotFoundException("ERR: User not found!"));
        assertThrows(UsernameNotFoundException.class, () -> userMasterServiceImpl.loadUserByUsername("janedoe"));
        verify(userMasterRepository).findByUsername((String) any());
    }
}

