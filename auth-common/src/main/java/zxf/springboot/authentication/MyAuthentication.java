package zxf.springboot.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.catalina.SessionIdGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@JsonSerialize
public class MyAuthentication implements Authentication {
    private transient Boolean needSave = false;
    private MyUser myUser;
    private String tokenId;
    private String accessToken;

    //Add for json serialize
    public MyAuthentication() {
    }

    public MyAuthentication(MyUser myUser) {
        this.needSave = true;
        this.myUser = myUser;
        this.tokenId = TokenIdGenerator.generateTokenId();
        this.setAccessToken(UUID.randomUUID().toString());
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public Object getCredentials() {
        return null;
    }

    @Override
    @JsonIgnore
    public Object getDetails() {
        return null;
    }

    @Override
    @JsonIgnore
    public Object getPrincipal() {
        return myUser;
    }

    @Override
    @JsonIgnore
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    @JsonIgnore
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    @JsonIgnore
    public String getName() {
        return myUser.getName();
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        this.needSave = true;
    }

    @JsonIgnore
    public Boolean getNeedSave() {
        return needSave;
    }

    @JsonSerialize
    public static class MyUser implements Serializable {
        private String name;
        private Integer age = 1;

        //Add for json serialize
        public MyUser() {
        }

        public MyUser(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
