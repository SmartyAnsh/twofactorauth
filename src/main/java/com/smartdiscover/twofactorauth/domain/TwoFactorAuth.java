package com.smartdiscover.twofactorauth.domain;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="TWO_FACTOR_AUTH")
public class TwoFactorAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String authToken;

    private Date dateCreated;
    private Date dateUpdated;

    private Boolean isVerified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Boolean getVerified() {
        return isVerified == null || isVerified == false ? false : true;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
