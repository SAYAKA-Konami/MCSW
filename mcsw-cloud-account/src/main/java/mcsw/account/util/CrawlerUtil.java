package mcsw.account.util;

import cn.hutool.http.HttpUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static mcsw.account.config.Constant.URL_LOGIN;

/**
 * @apiNote 爬取登录页面登录所需信息
 * @author wu nan
 * @since  2022/11/26
 **/
@Component
public class CrawlerUtil {

    private final static String REGEX = "<input.*name=.(lt|execution|_eventId|warn|submit|captcha)..*value=.(.*?\\\")";

    private final static Pattern PATTERN = Pattern.compile(REGEX);

    /**
     * @return Map: key: e.g. lt - value: e.g.  LT-582962-o43r5kQ7eWRmmmjQ9CsB2RfMR4DToF-cas01.example.org
     */
    public Map<String, Object> getHtml(){
        Map<String, Object> key_valueOfHttpHead = new HashMap<>();
        // 获取HTML文件
        String s = HttpUtil.get(URL_LOGIN);
        // 正则表达式子解析
        Matcher matcher = PATTERN.matcher(s);
        // 循环匹配
        while (matcher.find()){
            String val = matcher.group(2);
            // 上述的正则截取之后会多出一个 双引号在最后 e.g. true"
            val = val.substring(0, val.length() - 1);
            key_valueOfHttpHead.put(matcher.group(1), val);
        }
        return key_valueOfHttpHead;
    }

    /**
     *
     * @param htmlOfLogin 执行登录请求之后获取到的html页面
     * @return true: 登录成功
     */
    public boolean isLogin(String htmlOfLogin){
        return htmlOfLogin.contains("综合信息门户");
    }

    public static void main(String[] args) {
        CrawlerUtil crawlerUtil = new CrawlerUtil();
        Map<String, Object> html = crawlerUtil.getHtml();
        System.out.println(html);
    }
}
