package mcsw.offer.client;

import mscw.common.api.CommonResult;
import mscw.common.domain.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

/**
 * @apiNote 调用账号服务的接口
 * @author wu nan
 * @since  2022/11/30
 **/
@FeignClient("mcsw-cloud-account")
public interface UserClient {

    @GetMapping("/getUserInfo")
    CommonResult<UserVO> getUserInfo(@RequestParam("name") String name);

    @PostMapping("/getUsersByIds")
    CommonResult<List<UserVO>> getUserByIds(@RequestBody Collection<Integer> ids);
}
