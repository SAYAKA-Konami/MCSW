package mcsw.account.util.filter.register;

import mcsw.account.dao.UserDao;
import mcsw.account.model.dto.UserDto;
import mcsw.account.util.filter.HandleRegister;
import mscw.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static mcsw.account.config.Constant.REPEAT_NAME;
/**
 * @apiNote 校验是否有重复名
 * @author wu nan
 * @since  2022/11/26
 **/

@Component
public class CheckName extends HandleRegister {

    @Autowired
    UserDao userDao;

    @Override
    public CommonResult<String> handle(UserDto userDto) {
        int count = userDao.countByName(userDto.getName());
        if (count == 1) return CommonResult.failed(REPEAT_NAME);
        else return pass(next, userDto);
    }


}
