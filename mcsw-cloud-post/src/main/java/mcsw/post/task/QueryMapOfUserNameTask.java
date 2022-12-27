package mcsw.post.task;

import cn.hutool.http.HttpStatus;
import mcsw.post.client.UserClient;
import mcsw.post.entity.Reply;
import mscw.common.api.CommonResult;
import mscw.common.domain.vo.UserVO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


public class QueryMapOfUserNameTask implements Callable<Optional<Map<String, String>>> {
    private UserClient userClient;

    private List<Reply> replyList;

    public QueryMapOfUserNameTask(UserClient userClient, List<Reply> replyList) {
        this.userClient = userClient;
        this.replyList = replyList;
    }

    @Override
    public Optional<Map<String, String>> call() throws Exception {
        List<Integer> collect = replyList.stream().distinct().map(Reply::getUserId).collect(Collectors.toList());
        CommonResult<List<UserVO>> userByIds = userClient.getUserByIds(collect);
        if (userByIds.getCode() == HttpStatus.HTTP_OK) {
            List<UserVO> data =  userByIds.getData();
            return Optional.of(data.stream().collect(Collectors.toMap(UserVO::getId, UserVO::getName)));
        }else{
            return Optional.empty();
        }
    }
}
