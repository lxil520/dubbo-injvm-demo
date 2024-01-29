package org.example.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.example.bo.LoginAccountBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * Jwt工具
 *
 */
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    private static final String EXPIRE_TIME = "exp";
    private static final String PHONE_KEY = "phone";
    private static final String EXTEND_KEY = "extend";
    private static final String IS_SUPER_KEY = "isSuper";
    private static final String USERNAME_KEY = "username";
    private static final String ACCOUNT_ID_KEY = "accountId";
    private static final String TENANT_ID_KEY = "tenantId";
    private static final String PERMISSION_CODES_KEY = "permissionCodes";
    private static final String UUID = "uid";
    private static final String SECRET = "{cipher}63a8f4397bd243fd626c1327e52d1c42a0f3c13e357079c7c19b9aea445054d4";
    /**
     * jwt 颁发者
     */
    private static final String ISSUER = "paas";

    /**
     * jwt生成
     *
     * @return jwt字符串
     */
    public static String getJwt(LoginAccountBo accountBo) {
        return JWT.create()
                .setIssuer(ISSUER)
                .setExpiresAt(DateUtil.offsetSecond(new Date(), 3600))
                .setPayload(UUID, accountBo.getUid())
                .setPayload(TENANT_ID_KEY, accountBo.getTenantId())
                .setPayload(ACCOUNT_ID_KEY, accountBo.getAccountId())
                .setPayload(USERNAME_KEY, accountBo.getUsername())
                .setPayload(IS_SUPER_KEY, accountBo.getSuper())
                .setPayload(EXTEND_KEY, accountBo.getExtend())
                .setPayload(PHONE_KEY, accountBo.getPhone())
                .sign(JWTSignerUtil.hs256(SECRET.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * jwt解析
     *
     * @param jwtStr jwt字符串
     * @return 账号id、账号名称、租户id、是否超管、账号权限
     */
    public static LoginAccountBo parseJwt(String jwtStr) {
        return JwtUtil.parseJwt(jwtStr, true);
    }

    /**
     * jwt解析
     */
    public static LoginAccountBo parseJwt(String jwtStr, boolean validate) {
        // 校验jwt
        if (validate && !validate(jwtStr)) {
            throw new ExpiredCredentialsException();
        }

        JSONObject payloads;
        try {
            // 解析jwt
            payloads = JWT.of(jwtStr).getPayloads();
        } catch (Exception e) {
            throw new CredentialsException(e);
        }

        String uid = payloads.getStr(UUID);
        List<String> permissionCodes = List.of();

        // 构造 AccountBo 对象
        LoginAccountBo accountBo = new LoginAccountBo();
        accountBo.setPhone(payloads.getStr(PHONE_KEY));
        accountBo.setSuper(payloads.getBool(IS_SUPER_KEY));
        accountBo.setUsername(payloads.getStr(USERNAME_KEY));
        accountBo.setExtend(payloads.getJSONObject(EXTEND_KEY));
        accountBo.setAccountId(payloads.getLong(ACCOUNT_ID_KEY));
        accountBo.setTenantId(payloads.getLong(TENANT_ID_KEY));
        Date exprireDate = payloads.getDate(EXPIRE_TIME);
        accountBo.setTokenExpireTime(exprireDate == null ? null : DateUtil.toLocalDateTime(exprireDate));
        accountBo.setPermissionCodes(permissionCodes);
        accountBo.setUid(uid);
        return accountBo;
    }

    public static boolean validate(String jwtStr) {
        try {
            JWT jwt = JWT.of(jwtStr);

            // 判断该令牌是否被禁用
            String uid = jwt.getPayloads().getStr(UUID);

            // 判断令牌是否在有效期内
            return jwt
                    .setSigner(JWTSignerUtil.hs256(SECRET.getBytes(StandardCharsets.UTF_8)))
                    .validate(0);
        } catch (Exception e) {
            throw new CredentialsException();
        }
    }
}
