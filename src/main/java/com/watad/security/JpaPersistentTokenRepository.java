package com.watad.security;

import com.watad.entity.PersistentLogin;
import com.watad.services.PersistentLoginService;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class JpaPersistentTokenRepository implements PersistentTokenRepository {

    private final PersistentLoginService persistentLoginService;


    public JpaPersistentTokenRepository(PersistentLoginService persistentLoginService) {
        this.persistentLoginService = persistentLoginService;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLogin login = new PersistentLogin();
        login.setSeries(token.getSeries());
        login.setUsername(token.getUsername());
        login.setToken(token.getTokenValue());
        login.setLastUsed(token.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        persistentLoginService.create(login);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        persistentLoginService.findBySeries(series).ifPresent(login -> {
            login.setToken(tokenValue);
            login.setLastUsed(lastUsed.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            persistentLoginService.update(login);
        });
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        return persistentLoginService.findBySeries(seriesId)
                .map(login -> new PersistentRememberMeToken(
                        login.getUsername(),
                        login.getSeries(),
                        login.getToken(),
                        Timestamp.valueOf(login.getLastUsed())
                ))
                .orElse(null);
    }

    @Override
    public void removeUserTokens(String username) {
        persistentLoginService.deleteByUsername(username);
    }
}
