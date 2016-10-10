package com.softjourn.coin.auth.controller;

import com.softjourn.coin.auth.entity.User;
import com.softjourn.coin.auth.service.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class ApiController {

    private final LdapService ldapService;

    @Autowired
    public ApiController(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    @RequestMapping(value = "/{ldapId}", method = RequestMethod.GET)
    public User userExist(@PathVariable final String ldapId) {
        return ldapService.getUser(ldapId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll(){
        return ldapService.getAllUsers();
    }
}
