package mcsw.account.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * @apiNote 该类用于对用户输入的密码进行RSA加密
 * @author wu nan
 * @since  2022/11/25
 **/
@Component
@Slf4j
public class GetRSAPasswdUtil {
    private final static String[] PATH = new String[]{"js/code.js", "js/RSA.js", "js/BigInt.js", "js/Barrett.js"};

    private final static ScriptEngine ENGIN = new ScriptEngineManager().getEngineByName("javascript");

    @PostConstruct
    public void loadAllJS() throws IOException, ScriptException {
        // 加载所有JS类
        for (String p : PATH) {
            ClassPathResource classPathResource = new ClassPathResource(p);
            ENGIN.eval(new InputStreamReader(classPathResource.getInputStream()));
        }
    }
    //用脚本解释器，使用javascript解释器
    public String RSAPasswd(String passwd) {
        try {
            // 操作js函数
            passwd = (String) ENGIN.eval("generatePasswd('" + passwd + "')");
        } catch (Exception e) {
            log.error("加密失败！！！" + "密码为:" + passwd);
            // 失败时返回空
            return null;
        }
        return passwd;
    }

}
