package com.softjourn.coin.auth.config;


import com.softjourn.coin.auth.entity.User;
import com.softjourn.coin.auth.ldap.LdapAuthoritiesPopulationBean;
import com.softjourn.coin.auth.service.ILdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;

import java.util.Collections;

@Configuration
public class LdapConfiguration {

    @Bean
    public DefaultSpringSecurityContextSource contextSource(@Value("${ldapServerURL}") String ldapURL,
                                                            @Value("${ldapRoot}") String ldapRoot) {
        return new DefaultSpringSecurityContextSource(Collections.singletonList(ldapURL), ldapRoot);
    }

    @Bean
    @Autowired
    public BindAuthenticator ldapAuthenticator(@Value("${ldapUsersBase}") String ldapUsersBase,
                                               DefaultSpringSecurityContextSource contextSource) {

        BindAuthenticator authenticator = new BindAuthenticator(contextSource);
        authenticator.setUserSearch(new FilterBasedLdapUserSearch(ldapUsersBase, "(uid={0})", contextSource));

        return authenticator;
    }

    @Bean
    @Autowired
    public AuthenticationProvider authenticationProvider(LdapAuthenticator authenticator, LdapAuthoritiesPopulationBean ldapAuthoritiesPopulationBean) {
        return new LdapAuthenticationProvider(authenticator, ldapAuthoritiesPopulationBean);
    }

    @Bean
    @Profile({"prod", "dev", "test"})
    @Autowired
    public UserDetailsService userDetailsService(ILdapService ldapService, LdapAuthoritiesPopulationBean ldapAuthoritiesPopulationBean) {
        return ldapLogin -> {
            User user = ldapService.getUser(ldapLogin);
            return new org.springframework.security.core.userdetails.User(
                    user.getLdapId(),
                    "[HIDEN]",
                    ldapAuthoritiesPopulationBean.getGrantedAuthorities(null, ldapLogin)
            );
        };
    }

    @Bean
    @Autowired
    LdapTemplate ldapTemplate(LdapContextSource ldapContextSource) {
        LdapTemplate template = new LdapTemplate(ldapContextSource);
        template.setIgnorePartialResultException(true);

        return template;
    }
}
