package mcsw.account.util.filter.register;

import mcsw.account.model.dto.UserDto;
import mcsw.account.util.filter.HandleRegister;
import mscw.common.api.CommonResult;
import org.springframework.stereotype.Component;

@Component("ConvertDegree")
public class ConvertDegree extends HandleRegister {

    @Override
    public CommonResult<String> handle(UserDto userDto) {
        String degreeVar = userDto.getDegreeVar();
        switch (degreeVar) {
            case "本科" :
                userDto.setDegree(0);
                break;
            case "研究生":
                userDto.setDegree(1);
                break;
            case "博士":
                userDto.setDegree(2);
                break;
            case "教职工":
                userDto.setDegree(3);
                break;
            default:
                return CommonResult.failed("请选择您所攻读的学位");
        }
        return pass(next, userDto);
    }
}
