package com.pharmacy.authservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.pharmacy.authservice.model.UserMaster;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
class UserMasterImplTest {
    @Test
    void testBuild() {
        UserMaster userMaster = new UserMaster();
        userMaster.setEmail("jane.doe@example.org");
        userMaster.setId(123L);
        userMaster.setPassword("iloveyou");
        userMaster.setRoles(new HashSet<>());
        userMaster.setUsername("janedoe");
        UserMasterImpl buildResult = UserMasterImpl.build(userMaster);

        UserMaster userMaster1 = new UserMaster();
        userMaster1.setEmail("jane.doe@example.org");
        userMaster1.setId(123L);
        userMaster1.setPassword("iloveyou");
        userMaster1.setRoles(new HashSet<>());
        userMaster1.setUsername("janedoe");
        UserMasterImpl actualBuildResult = buildResult.build(userMaster1);
        assertEquals(buildResult.getAuthorities(), actualBuildResult.getAuthorities());
        assertEquals(buildResult.getEmail(), actualBuildResult.getEmail());
        assertEquals(buildResult.getUsername(), actualBuildResult.getUsername());
        assertEquals(buildResult.getPassword(), actualBuildResult.getPassword());
        assertEquals("jane.doe@example.org", actualBuildResult.getEmail());
        assertEquals(123L, actualBuildResult.getId().longValue());
        assertEquals("iloveyou", actualBuildResult.getPassword());
        assertEquals("janedoe", actualBuildResult.getUsername());
        assertTrue(actualBuildResult.isAccountNonExpired());
        assertTrue(actualBuildResult.isAccountNonLocked());
        assertTrue(actualBuildResult.isCredentialsNonExpired());
        assertTrue(actualBuildResult.isEnabled());
    }


    @Test
    void testEquals() {
        assertNotEquals(UserMasterImpl.build(new UserMaster()), null);
        assertNotEquals(UserMasterImpl.build(new UserMaster()), "Different type to UserMasterImpl");
    }


    @Test
    void testEquals2() {
        UserMasterImpl buildResult = UserMasterImpl.build(new UserMaster());
        assertEquals(buildResult, buildResult);
        int expectedHashCodeResult = buildResult.hashCode();
        assertEquals(expectedHashCodeResult, buildResult.hashCode());
    }


    @Test
    void testEquals3() {
        UserMasterImpl buildResult = UserMasterImpl.build(new UserMaster());
        UserMasterImpl buildResult1 = UserMasterImpl.build(new UserMaster());
        assertEquals(buildResult.getUsername(), buildResult1.getUsername());
        assertEquals(buildResult.getEmail(), buildResult1.getEmail());
        assertEquals(buildResult.getPassword(), buildResult1.getPassword());
        assertEquals(buildResult.getAuthorities(), buildResult1.getAuthorities());
        int notExpectedHashCodeResult = buildResult.hashCode();
        assertNotEquals(notExpectedHashCodeResult, buildResult1.hashCode());
    }


    @Test
    void testEquals4() {
        UserMaster userMaster = mock(UserMaster.class);
        when(userMaster.getId()).thenReturn(123L);
        when(userMaster.getEmail()).thenReturn("jane.doe@example.org");
        when(userMaster.getPassword()).thenReturn("iloveyou");
        when(userMaster.getUsername()).thenReturn("janedoe");
        when(userMaster.getRoles()).thenReturn(new HashSet<>());
        UserMasterImpl buildResult = UserMasterImpl.build(userMaster);
        assertNotEquals(buildResult, UserMasterImpl.build(new UserMaster()));
    }
}

