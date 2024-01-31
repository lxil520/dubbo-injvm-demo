package org.example.handler;

import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.bo.PermissionBo;
import org.example.dto.DemoRequestDto;
import org.example.dto.DemoResponseDto;
import org.example.service.DemoService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityManagerHandler implements ISecurityHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityManagerHandler.class);
    @DubboReference
    private DemoService demoService;
    @Override
    public List<PermissionBo> getAllPermission() {
        DemoRequestDto requestDto = new DemoRequestDto();
        requestDto.setName("testDemo");
        requestDto.setAge(123);
        List<DemoResponseDto> responseDtos = demoService.getDemoList(requestDto);
        LOGGER.info("rpc producer ..."+ responseDtos.size());
        List<PermissionBo> permissionBos = new ArrayList<>();
        PermissionBo permissionBo = new PermissionBo();
        permissionBo.setPermissionCode("aaa");
        permissionBo.setPermissionName("test");
        permissionBos.add(permissionBo);
        return permissionBos;
    }
}
