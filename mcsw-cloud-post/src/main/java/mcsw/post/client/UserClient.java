package mcsw.post.client;

import mscw.common.api.CommonResult;
import mscw.common.domain.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @apiNote 调用账号服务的接口
 * @author wu nan
 * @since  2022/11/30
 **/
@FeignClient("mcsw-cloud-account")
public interface UserClient {

    @GetMapping("/getUserInfo")
    CommonResult<UserVO> getUserInfo(@RequestParam("name") String name);

}
