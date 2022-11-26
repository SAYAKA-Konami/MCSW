package mcsw.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import mcsw.account.entity.User;
import mcsw.account.model.dto.UserDto;
import mscw.common.api.CommonResult;

/**
 * (User)表服务接口
 *
 * @author Nan
 * @since 2022-11-26 16:00:54
 */
public interface UserService extends IService<User> {

    /**
     *  注册
     */
    CommonResult<String> register(UserDto userDto);
}

