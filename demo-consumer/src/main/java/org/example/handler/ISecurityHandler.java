package org.example.handler;

import org.example.bo.PermissionBo;

import java.util.List;

public interface ISecurityHandler {

    List<PermissionBo> getAllPermission();
}
