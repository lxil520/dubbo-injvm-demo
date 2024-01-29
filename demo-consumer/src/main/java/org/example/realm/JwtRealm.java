package org.example.realm;

import com.google.common.collect.Sets;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.example.bo.AccountAuthorizationInfo;
import org.example.bo.LoginAccountBo;
import org.example.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * jwt realm
 *
 */
@Component
public class JwtRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRealm.class);


    public JwtRealm() {
        // jwt 校验策略
        super((authenticationToken, authenticationInfo) -> {
            BearerToken bearerToken = (BearerToken) authenticationToken;
            String jwtStr = bearerToken.getToken();
            // token 过期直接抛异常
            if (!JwtUtil.validate(jwtStr)) {
                throw new ExpiredCredentialsException();
            }

            return true;
        });
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LoginAccountBo accountBo = (LoginAccountBo) SecurityUtils.getSubject().getPrincipal();
        AccountAuthorizationInfo accountAuthorizationInfo = new AccountAuthorizationInfo();
        accountAuthorizationInfo.setPermissions(accountBo.getPermissionCodes());

        return accountAuthorizationInfo;
    }

    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        BearerToken bearerToken = (BearerToken) authenticationToken;
        String jwtStr = bearerToken.getToken();
        LoginAccountBo accountBo = JwtUtil.parseJwt(jwtStr);
        return new SimpleAuthenticationInfo(accountBo, jwtStr, this.getName());
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        LoginAccountBo accountBo = (LoginAccountBo) principals.getPrimaryPrincipal();
        HashSet<String> permissionCodes = Sets.newHashSet(accountBo.getPermissionCodes());
        return permissionCodes.contains(permission);
    }

    @Override
    public boolean isPermittedAll(PrincipalCollection subjectIdentifier, String... permissions) {
        LoginAccountBo accountBo = (LoginAccountBo) subjectIdentifier.getPrimaryPrincipal();
        HashSet<String> permissionCodes = Sets.newHashSet(accountBo.getPermissionCodes());
        for (String permission : permissions) {
            if (permissionCodes.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Class<? extends AuthenticationToken> getAuthenticationTokenClass() {
        return BearerToken.class;
    }
}
