package org.example.bo;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;

import java.util.Collection;
import java.util.stream.Collectors;

public class AccountAuthorizationInfo implements AuthorizationInfo {
    private Collection<String> permissionCodes;

    public void setPermissions(Collection<String> permissionCodes) {
        this.permissionCodes = permissionCodes;
    }

    /**
     * 目前基于权限粒度，暂时不需要用到角色
     */
    @Override
    public Collection<String> getRoles() {
        return Lists.newArrayList();
    }

    @Override
    public Collection<String> getStringPermissions() {
        return this.permissionCodes;
    }

    @Override
    public Collection<Permission> getObjectPermissions() {
        return this.permissionCodes.stream().map(item -> new Permission() {
            @Override
            public boolean implies(Permission p) {
                if (ObjectUtil.isNull(p)) {
                    return false;
                }

                return p.implies(this);
            }
        }).collect(Collectors.toList());
    }
}
