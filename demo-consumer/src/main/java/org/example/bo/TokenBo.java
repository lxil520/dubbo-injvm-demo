package org.example.bo;


import java.io.Serial;
import java.io.Serializable;
public class TokenBo implements Serializable {
    @Serial
    private static final long serialVersionUID = -1535570672616798906L;
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
