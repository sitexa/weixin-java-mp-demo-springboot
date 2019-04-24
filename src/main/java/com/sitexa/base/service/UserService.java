package com.sitexa.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sitexa.base.entity.User;

public interface UserService extends IService<User> {

    Page<User> queryUserIncludeRoles(Page page, String nick);

}
