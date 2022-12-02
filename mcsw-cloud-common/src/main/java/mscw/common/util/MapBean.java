package mscw.common.util;

import mscw.common.domain.vo.UserVO;

import java.util.Map;

public class MapBean {
    /**
     * 将HttpHeader中的用户信息转化为实体类
     * @apiNote header必须被AOP过滤过
     * @param header Gateway中存储在请求头的信息
     * @return {@link UserVO}
     */
    public static UserVO convertHeaderToUserVo(Map<String, String> header){
        return new UserVO().setId(header.get("id"))
                .setName(header.get("name"))
                .setDegreeCz(header.get("degree"))
                .setCollegeCz(header.get("college"))
                .setMajor(header.get("major"))
                .setGenderCz(DictionaryOfUser.genderToChinese(Integer.parseInt(header.get("gender"))));
    }

}
