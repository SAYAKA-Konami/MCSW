package mscw.common.util;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @apiNote 学院字典。抽取到公告模块以便网关能够使用其进行转化。究其原因是如果直接用中文设置请求头的话解析得到的是乱码...
 *          目前不知道如何解决。只能将字典抽取过来。
 * @author wu nan
 * @since  2022/11/28
 **/

public class DictionaryOfUser {
    /**
     *  数字对应相应的名称
     */
    @Getter
    private static Map<Integer, String> code2collegeName = new HashMap<>();

    @Getter
    private static Map<String, Integer> collegeName_code = new HashMap<>();

    @Getter
    private static Map<String, Integer> DEGREECZ_CODE = new HashMap<>();

    @Getter
    private static Map<Integer, String> CODE_DEGREECZ = new HashMap<>();

    static {
        CollegeNameToCode[] values = CollegeNameToCode.values();
        for (CollegeNameToCode value : values) {
            collegeName_code.put(value.getCollege(), value.getCode());
            code2collegeName.put(value.getCode(), value.getCollege());
        }
        Degree[] degrees = Degree.values();
        for (Degree value : degrees) {
            DEGREECZ_CODE.put(value.getName(), value.getCode());
            CODE_DEGREECZ.put(value.getCode(), value.getName());
        }
    }
    public enum Degree{
        UNDERGRADUATE("本科", 0), POSTGRADUATE("研究生", 1),
        DOCTOR("博士生", 2), TEACHER("教职工", 3), OTHER("其他", -1);
        private String name;
        private Integer code;

        Degree(String name, Integer code) {
            this.name = name;
            this.code = code;
        }
        public String getName() {
            return name;
        }

        public Integer getCode() {
            return code;
        }
    }
    /**
     *  学院字典
     */
    public enum CollegeNameToCode{
        SHU_XIN("数学与信息学院、软件学院", 0), DIAN_ZI("电子信息学院", 1), YI_SHU("艺术学院", 2),
        GONG_GUAN("公共管理学院", 3), SHI_PIN("食品学院", 4), LIN_FENG("林园与风景学院", 5),
        SHENG_KE("生命与科学学院", 6), NONG("农学院", 7), SHUI_LI("水利学院", 8),
        JING_GUAN("经济管理学院", 9), REN_WEN("人文与法学院", 10), ZI_HUAN("资源与环境学院", 11), WAI_GUO_YU("外国语学院", 12),
        OTHER("其他", -1);
        private String college;

        private Integer code;

        CollegeNameToCode(String college, Integer code) {
            this.college = college;
            this.code = code;
        }

        public String getCollege() {
            return college;
        }

        public Integer getCode() {
            return code;
        }
    }

    public static Integer genderCzToCode(String gender){
        switch (gender) {
            case "男": return 1;
            case "女": return 0;
            default: return -1;
        }
    }

    public static String genderToChinese(int gender){
        switch (gender) {
            case 0: return "男";
            case 1: return "女";
            default: return "未定义";
        }
    }


}

