package org.lessons.milestone.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.lessons.milestone.model.User;
import org.lessons.milestone.model.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class DatabaseUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final Set<GrantedAuthority> authorities;

    
    public DatabaseUserDetails(User user) {
        this.username = user.getEmail();  
        this.password = user.getPassword();
        this.authorities = new HashSet<>();
        
        // Aggiunge i ruoli dell'utente
        Role role = user.getRole(); 
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  
    }

    @Override
    public boolean isEnabled() {
        return true;  
    }
}
