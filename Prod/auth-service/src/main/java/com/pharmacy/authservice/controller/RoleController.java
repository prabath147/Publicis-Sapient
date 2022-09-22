package com.pharmacy.authservice.controller;

import com.pharmacy.authservice.model.ERole;
import com.pharmacy.authservice.model.Role;
import com.pharmacy.authservice.model.UserMaster;
import com.pharmacy.authservice.payload.response.MessageResponse;
import com.pharmacy.authservice.repository.RoleRepository;
import com.pharmacy.authservice.repository.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserMasterRepository userMasterRepository;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRoles() {
        List<Role> roles = new ArrayList<>(roleRepository.findAll());
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveManager(@PathVariable("id") Long userId) {

        Optional<UserMaster> checkUser = userMasterRepository.findById(userId);
        if(checkUser.isEmpty())
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

        UserMaster user = checkUser.get();
        Set<Role> role = new HashSet<>();

        Role managerRole = roleRepository.findByName(ERole.ROLE_MANAGER);
        role.add(managerRole);

        user.setRoles(role);
        userMasterRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Manager has been approved."));
    }
}
