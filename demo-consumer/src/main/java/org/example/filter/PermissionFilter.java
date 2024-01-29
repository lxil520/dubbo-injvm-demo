package org.example.filter;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.example.Result;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class PermissionFilter extends PermissionsAuthorizationFilter {
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {
        boolean isPermitted = super.isAccessAllowed(request, response, mappedValue);

        if (!isPermitted) {
            Result<Void> result = Result.failed(102, "Token parsing error");
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.setContentType(ContentType.JSON.getValue());
            PrintWriter writer = response.getWriter();
            // 输出异常
            writer.write(JSONUtil.toJsonStr(result));
            writer.flush();
        }

        return isPermitted;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return false;
    }
}
