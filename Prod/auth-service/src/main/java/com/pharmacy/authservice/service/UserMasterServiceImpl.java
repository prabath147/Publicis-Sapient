package com.pharmacy.authservice.service;

import com.pharmacy.authservice.model.UserMaster;
import com.pharmacy.authservice.repository.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserMasterServiceImpl implements UserDetailsService {
  @Autowired
  UserMasterRepository userMasterRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserMaster user = userMasterRepository.findByUsername(username);
    if (user == null)
      throw new UsernameNotFoundException("ERR: User not found!");
    return UserMasterImpl.build(user);
  }

}
