package mcsw.post.task;

import mcsw.post.client.UserClient;
import mcsw.post.entity.Reply;
import mscw.common.domain.vo.UserVO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


public class QueryMapOfUserName implements Callable<Map<String, String>> {
    private UserClient userClient;

    private List<Reply> replyList;

    public QueryMapOfUserName(UserClient userClient, List<Reply> replyList) {
        this.userClient = userClient;
        this.replyList = replyList;
    }

    @Override
    public Map<String, String> call() throws Exception {
        List<Integer> collect = replyList.stream().distinct().map(Reply::getUserId).collect(Collectors.toList());
        List<UserVO> data = userClient.getUserByIds(collect).getData();
        return data.stream().collect(Collectors.toMap(UserVO::getId, UserVO::getName));
    }
}
