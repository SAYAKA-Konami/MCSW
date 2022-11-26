package mcsw.account.util.filter;

import mcsw.account.model.dto.UserDto;
import mscw.common.api.CommonResult;


public abstract class HandleRegister {

    protected HandleRegister next;

    public abstract CommonResult<String> handle(UserDto userDto);

    public void setNext(HandleRegister handleRegister){
        next = handleRegister;
    }

    public CommonResult<String> pass(HandleRegister handleRegister, UserDto userDto){
        if (handleRegister == null) return null;
        else return handleRegister.handle(userDto);
    }


}
