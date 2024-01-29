package org.example.realm;

import cn.hutool.core.util.ObjectUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.example.bo.AccountAuthorizationInfo;
import org.example.bo.LoginAccountBo;
import org.example.handler.ISecurityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 账号密码 realm
 *
 */
@Component
public class UsernamePasswordRealm extends AuthorizingRealm {

    @Autowired
    private ISecurityHandler securityHandler;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        String username = userToken.getUsername();
        if (ObjectUtil.isEmpty(username)) {
            throw new UnknownAccountException();
        }

        // 根据账号查询账号详情
        LoginAccountBo loginAccountBo = null;
        if (ObjectUtil.isNull(loginAccountBo)) {
            throw new UnknownAccountException();
        }

        return new SimpleAuthenticationInfo(loginAccountBo, loginAccountBo.getPassword(), this.getName());
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        LoginAccountBo accountBo = (LoginAccountBo) SecurityUtils.getSubject().getPrincipal();
        AccountAuthorizationInfo accountAuthorizationInfo = new AccountAuthorizationInfo();
        accountAuthorizationInfo.setPermissions(accountBo.getPermissionCodes());

        return accountAuthorizationInfo;
    }

    @Override
    public Class<? extends AuthenticationToken> getAuthenticationTokenClass() {
        return UsernamePasswordToken.class;
    }
}
