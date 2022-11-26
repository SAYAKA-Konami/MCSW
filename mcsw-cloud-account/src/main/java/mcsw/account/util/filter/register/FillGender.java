package mcsw.account.util.filter.register;

import mcsw.account.model.dto.UserDto;
import mcsw.account.util.filter.HandleRegister;
import mscw.common.api.CommonResult;
import org.springframework.stereotype.Component;

@Component("FillGender")
public class FillGender extends HandleRegister {


    @Override
    public CommonResult<String> handle(UserDto userDto) {
        if (userDto.getGender() == null) userDto.setGender(3);
        return pass(next, userDto);
    }
}
