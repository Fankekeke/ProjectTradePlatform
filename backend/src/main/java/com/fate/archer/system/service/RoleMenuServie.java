package com.fate.archer.system.service;

import com.fate.archer.system.domain.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleMenuServie extends IService<RoleMenu> {

    void deleteRoleMenusByRoleId(String[] roleIds);

    void deleteRoleMenusByMenuId(String[] menuIds);

    List<RoleMenu> getRoleMenusByRoleId(String roleId);
}
