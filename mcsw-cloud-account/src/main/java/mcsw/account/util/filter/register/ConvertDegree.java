package mcsw.account.util.filter.register;

import mcsw.account.model.dto.UserDto;
import mcsw.account.util.filter.HandleRegister;
import mscw.common.api.CommonResult;
import mscw.common.domain.DictionaryOfCollegeAndDegree;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConvertDegree extends HandleRegister {


    @Override
    public CommonResult<String> handle(UserDto userDto) {
        String degreeVar = userDto.getDegreeCz();
        Map<String, Integer> degreecz_code = DictionaryOfCollegeAndDegree.getDEGREECZ_CODE();
        if (degreecz_code.containsKey(degreeVar)) {
            userDto.setDegree(degreecz_code.get(degreeVar));
        } else {
            userDto.setDegree(DictionaryOfCollegeAndDegree.Degree.OTHER.getCode());
        }
        return pass(next, userDto);
    }
}
