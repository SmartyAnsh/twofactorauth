package com.smartdiscover.twofactorauth.repository;

import com.smartdiscover.twofactorauth.domain.TwoFactorAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TwoFactorAuthRepo extends CrudRepository<TwoFactorAuth, Long> {

    @Query("SELECT t FROM TwoFactorAuth t WHERE t.username = :username and t.authToken = :authToken order by t.id desc")
    public TwoFactorAuth findTwoFactorAuthByUsernameAndAuthToken(@Param("username") String username, @Param("authToken") String authToken);

}
