package org.example.config;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.example.bo.PermissionBo;
import org.example.filter.JwtAuthenticationFilter;
import org.example.filter.PermissionFilter;
import org.example.handler.ISecurityHandler;
import org.example.realm.JwtRealm;
import org.example.realm.UsernamePasswordRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro 配置
 *
 */
@Configuration
public class ShiroConfig {
    @Autowired
    private ISecurityHandler securityHandler;
    @Autowired
    private CorsFilter corsFilter;

    @Bean
    public DefaultWebSecurityManager securityManager(UsernamePasswordRealm usernamePasswordRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(usernamePasswordRealm);
        // 关闭shiroDao功能
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        // 不需要将ShiroSession中的东西存到任何地方包括Http Session中）
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        // securityManager.setSubjectFactory(subjectFactory());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 1、注册自定义过滤器(覆盖Shiro自带)
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("cors", corsFilter);
        filterMap.put(DefaultFilter.perms.name(), new PermissionFilter());
        filterMap.put(DefaultFilter.authcBearer.name(), new JwtAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        // 2、配置全局过滤器NoSessionCreationFilter，切换成无session服务
        shiroFilterFactoryBean.setGlobalFilters(Lists.newArrayList(DefaultFilter.noSessionCreation.name()));

        // 3、配置访问权限
        // 获取系统所有权限
        List<PermissionBo> permissionList = securityHandler.getAllPermission();
        if (ObjectUtil.isEmpty(permissionList)) {
            permissionList = Lists.newArrayList();
        }

        List<String> permsCodeList;
        Map<String, List<String>> url2PermsCodeListMap = new HashMap<>();
        // key：url，value：permissionCode集合
        for (PermissionBo permissionDto : permissionList) {
            if (ObjectUtil.isEmpty(permissionDto.getUrls())) {
                continue;
            }

            for (String url : Sets.newHashSet(permissionDto.getUrls())) {
                permsCodeList = url2PermsCodeListMap.getOrDefault(url, Lists.newArrayList());
                permsCodeList.add(permissionDto.getPermissionCode());
                url2PermsCodeListMap.put(url, permsCodeList);
            }
        }

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 其余均走jwt登录态校验
        filterChainDefinitionMap.put("/**", "cors," + DefaultFilter.authcBearer.name());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    protected Authorizer authorizer(JwtRealm jwtRealm) {
        return jwtRealm;
    }

    @Bean
    public Authenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AbstractAuthenticationStrategy() {
            private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

            @Override
            public AuthenticationInfo afterAttempt(Realm realm,
                                                   AuthenticationToken token,
                                                   AuthenticationInfo singleRealmInfo,
                                                   AuthenticationInfo aggregateInfo,
                                                   Throwable t) throws AuthenticationException {
                if (ObjectUtil.isNotNull(t)) {
                    if (t instanceof AuthenticationException) {
                        throw (AuthenticationException) t;
                    }

                    throw new AuthenticationException(t);
                }

                return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
            }

            @Override
            public AuthenticationInfo afterAllAttempts(AuthenticationToken token,
                                                       AuthenticationInfo aggregate) throws AuthenticationException {
                if (aggregate == null || (aggregate.getPrincipals() == null || aggregate.getPrincipals().isEmpty())) {
                    this.LOGGER.error("There may not be a suitable realm to handle authentication.");
                    throw new AuthenticationException();
                }

                return aggregate;
            }
        });

        return authenticator;
    }
}
