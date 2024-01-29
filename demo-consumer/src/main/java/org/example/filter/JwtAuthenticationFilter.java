package org.example.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.BearerHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.example.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * jwt 认证过滤器
 */
public class JwtAuthenticationFilter extends BearerHttpAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String authorizationHeader = httpRequest.getHeader("Demo-Access-Token");

        // AuthorizationHeader为空，则伪装一个，使之通过“尝试认证”判断
        if (ObjectUtil.isEmpty(authorizationHeader)) {
            LOGGER.warn("Authentication failed, token is empty. AuthorizationHeader: {}",
                    "Demo-Access-Token");
            authorizationHeader = getAuthzScheme();
        }

        // AuthorizationHeader开头标识错误，则伪装一个，使之通过“尝试认证”判断
        if (!authorizationHeader.startsWith(getAuthzScheme())) {
            LOGGER.warn("Authentication failed, token auth scheme error. Should be {}", getAuthzScheme());
            authorizationHeader = getAuthzScheme();
        }

        return authorizationHeader;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        if (ObjectUtil.isNull(e)) {
            return false;
        }

        Result<Void> result;
        if (e.getClass().equals(ExpiredCredentialsException.class)) {
            // token 过期
            LOGGER.debug("The token is expired.");
            result = Result.failed(101, "The token is expired");
        } else if (e.getClass().equals(CredentialsException.class)) {
            // token 解析错误
            LOGGER.debug("Token parsing error.");
            result = Result.failed(102, "Token parsing error");
        } else {
            // 其它异常
            LOGGER.error("Login operation failed.", e);
            result = Result.failed(103, "Login operation failed");
        }

        // 输出异常
        this.printSendException(response, result);

        return false;
    }

    /**
     * 输出异常信息
     */
    private void printSendException(ServletResponse response, Result<?> result) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.setContentType(ContentType.JSON.getValue());
        try {
            PrintWriter writer = response.getWriter();
            // 输出异常
            writer.write(JSONUtil.toJsonStr(result));
            writer.flush();
        } catch (IOException ex) {
            LOGGER.error("Failed to output exception.", ex);
        }
    }
}
