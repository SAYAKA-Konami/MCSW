package mcsw.account.util.filter.register;

import mcsw.account.dao.UserDao;
import mcsw.account.model.dto.UserDto;
import mcsw.account.util.filter.HandleRegister;
import mscw.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static mcsw.account.config.Constant.REPEAT_REGISTER;

@Component
public class CheckAccount extends HandleRegister {

    @Autowired
    UserDao userDao;

    @Override
    public CommonResult<String> handle(UserDto userDto) {
        int count = userDao.countByAccount(userDto.getId());
        if (count > 0) return CommonResult.failed(REPEAT_REGISTER);
        else return pass(next, userDto);
    }
}
