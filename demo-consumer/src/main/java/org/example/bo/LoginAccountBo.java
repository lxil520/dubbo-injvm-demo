package org.example.bo;

import cn.hutool.core.lang.Opt;
import cn.hutool.json.JSONObject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 授权账号详情
 *
 */
public class LoginAccountBo implements Serializable {

    @Serial
    private static final long serialVersionUID = -8444092772529935207L;
    /**
     * 账号id
     */
    private Long accountId;
    /**
     * 账号名称
     */
    private String username;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 账号密码(密文)
     */
    private String password;
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 是否超管
     */
    private Boolean isSuper;
    /**
     * 账号权限码
     */
    private Collection<String> permissionCodes;
    /**
     * 作为token的唯一标识
     */
    private String uid;
    /**
     * 扩展字段
     */
    private JSONObject extend;
    /**
     * 访问令牌过期时间
     */
    private LocalDateTime tokenExpireTime;

    private Integer enableStatus;

    public <T> T getExtend(Class<T> clazz) {
        return Opt.ofNullable(extend).map(item -> item.toBean(clazz)).get();
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getSuper() {
        return isSuper;
    }

    public void setSuper(Boolean aSuper) {
        isSuper = aSuper;
    }

    public Collection<String> getPermissionCodes() {
        return permissionCodes;
    }

    public void setPermissionCodes(Collection<String> permissionCodes) {
        this.permissionCodes = permissionCodes;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public JSONObject getExtend() {
        return extend;
    }

    public void setExtend(JSONObject extend) {
        this.extend = extend;
    }

    public LocalDateTime getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(LocalDateTime tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }
}
