package com.aadhikat.springrsocket.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository // for demo purposes
public class UserRepository {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Map<String, UserDetails> db;

    @PostConstruct
    private void init() {
        this.db = new HashMap<>();
        this.db.put("user", User.withUsername("user").password(passwordEncoder.encode("password")).roles("USER").build());
        this.db.put("admin", User.withUsername("admin").password(passwordEncoder.encode("password")).roles("ADMIN").build());
        this.db.put("client", User.withUsername("client").password(passwordEncoder.encode("password")).roles("TRUSTED_CLIENT").build());
    }

    public UserDetails findByUsername(String userName) {
        return this.db.get(userName);
    }
}
