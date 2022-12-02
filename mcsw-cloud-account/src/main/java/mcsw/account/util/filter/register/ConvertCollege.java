package mcsw.account.util.filter.register;

import mcsw.account.model.dto.UserDto;
import mcsw.account.util.filter.HandleRegister;
import mscw.common.api.CommonResult;
import mscw.common.util.DictionaryOfUser;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @apiNote 学院字典。将学院名称转化成对应的数字，节省存储空间。
 * @author wu nan
 * @since  2022/11/28
 **/
@Component
public class ConvertCollege extends HandleRegister {

    @Override
    public CommonResult<String> handle(UserDto userDto) {
        String collegeCz = userDto.getCollegeCz();
        Map<Integer, String> code2collegeName = DictionaryOfUser.getCode2collegeName();
        Map<String, Integer> collegeName_code = DictionaryOfUser.getCollegeName_code();
        // 如果在字典中找不到的话，标记为-1。
        if (collegeName_code.containsKey(collegeCz)) {
            userDto.setCollege(collegeName_code.get(collegeCz));
        } else {
            userDto.setCollege(DictionaryOfUser.CollegeNameToCode.OTHER.getCode());
        }
        return pass(next, userDto);
    }
}
