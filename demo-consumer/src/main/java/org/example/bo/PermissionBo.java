package org.example.bo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

/**
 * 权限信息
 *
 */
public class PermissionBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 8833770581787813775L;
    /**
     * 权限id
     */
    private Long permissionId;
    /**
     * 权限码
     */
    private String permissionCode;
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 权限类型
     */
    private String permissionType;
    /**
     * 父级权限id；-1为顶级
     *
     */
    private Long parentPermissionId;
    /**
     * 权限限制的url
     */
    private Collection<String> urls;

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public Long getParentPermissionId() {
        return parentPermissionId;
    }

    public void setParentPermissionId(Long parentPermissionId) {
        this.parentPermissionId = parentPermissionId;
    }

    public Collection<String> getUrls() {
        return urls;
    }

    public void setUrls(Collection<String> urls) {
        this.urls = urls;
    }
}
