import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import mcsw.account.util.CrawlerUtil;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;
import java.util.Map;

public class GetPasswdTest {

    @Test
    public void printPasswd() {
        String[] path = new String[]{"js/code.js", "js/RSA.js", "js/BigInt.js", "js/Barrett.js"};
        String passwd = "wn485233";
        //用脚本解释器，使用javascript解释器
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            for (String p : path) {
                ClassPathResource classPathResource = new ClassPathResource(p);
                engine.eval(new InputStreamReader(classPathResource.getInputStream()));
            }
            // 操作js函数
            passwd = (String) engine.eval("generatePasswd('" + passwd + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(passwd);
    }

    @Test
    public void testLogin(){
        CrawlerUtil crawlerUtil = new CrawlerUtil();
//        GetRSAPasswdUtil getRSAPasswdUtil = new GetRSAPasswdUtil();
        Map<String, Object> map = crawlerUtil.getHtml();
        map.put("username", "201925710123");
        map.put("password", "44cc0a532aab87509c42c1a66f009306bccb5799c5e83cfcb06e2108d25e05036a3dd9ceb56ab3449baf74cc3eeba96bc5cb26fbfd410c0c4e87668d97cace5b20d8d4e1195980fc536cf834996e1e1e0da53ba4b5ebaaf5c3e8cd2af291c66c876462bf5f7bdab10c594e51dd310c0005250d04709f6496d7528169ca534e5c");
        HttpResponse execute = HttpRequest.post("https://cas.scau.edu.cn/lyuapServer/login")
                .header("Connection", "keep-alive")//头信息，多个头信息多次调用此方法即可
                .header("Accept", "*/*")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                .form(map)//表单内容
                .execute();
        int status = execute.getStatus();
        System.out.println(status);
        String body = execute.body();
        System.out.println(body);
    }


}


