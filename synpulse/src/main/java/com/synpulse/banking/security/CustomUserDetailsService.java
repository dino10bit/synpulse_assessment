package com.synpulse.banking.security;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("inside CustomUserDetailsService");
		List<SimpleGrantedAuthority> roles = null;
		if(username.equals("synpulse"))
		{
			roles=Arrays.asList(new SimpleGrantedAuthority("ROLE_VIEW"));
			return new User("synpulse","$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",roles);
		}
		throw new UsernameNotFoundException("User not found with the name "+ username);
	}

}
