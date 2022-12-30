package mcsw.offer.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mcsw.offer.client.UserClient;
import mcsw.offer.dao.CommentDao;
import mcsw.offer.entity.Comment;
import mcsw.offer.model.dto.QueryOfferDto;
import mcsw.offer.model.dto.RequestCommentDto;
import mcsw.offer.model.vo.CommentVo;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import mscw.common.api.CommonResult;
import mscw.common.domain.vo.UserVO;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static mscw.common.config.Constants.SERVER_ERROR;

/**
 * (Comment)表服务实现类
 *
 * @author Nan
 * @since 2022-12-29 14:35:56
 */
@Service
public class CommentService extends ServiceImpl<CommentDao, Comment> implements IService<Comment> {
    @Resource
    private OfferService offerService;
    @Resource
    private CivilServantService civilServantService;
    @Resource
    private MasterService masterService;
    @Resource
    private ThreadPoolTaskExecutor offerTaskExecutor;
    private CommentDao commentDao;
    private UserClient userClient;

    private static final String UNKNOW_NAME = "佚名";
    private static final String OFFER_SUFFIX = "o";
    private  static final String MASTER_SUFFIX = "m";
    private  static final String CIVIL_SERVANT_SUFFIX = "c";


    public CommonResult<String> comment(Map<String, String> header, RequestCommentDto commentDto){
        Comment comment = new Comment();
        int userId = Integer.parseInt(header.get("id"));
        comment.setUserId(userId).setContent(commentDto.getContent()).setParentId(commentDto.getParentId());
        Integer type = commentDto.getType();
        String mcswId = generateMcswId(commentDto.getOfferId(), commentDto.getType());
        comment.setMcswId(mcswId);
        boolean save = this.save(comment);
        if (save) {
            return CommonResult.success("操作成功");
        }else{
            return CommonResult.failed(SERVER_ERROR);
        }
    }

    public List<CommentVo> getCommentFacade(QueryOfferDto queryOfferDto){
        Integer category = queryOfferDto.getCategory();
        String mcswId = generateMcswId(queryOfferDto.getId(), category);
        Optional<List<CommentVo>> commentsForOne = getCommentsForOne(mcswId);
        return commentsForOne.orElse(null);
    }

    @Nullable
    private static String generateMcswId(Integer id, Integer category) {
        String mcswId = id.toString();
        switch (category) {
            case 0:
                mcswId = mcswId.concat(OFFER_SUFFIX);
                break;
            case 1:
                mcswId = mcswId.concat(MASTER_SUFFIX);
                break;
            case 2:
                mcswId = mcswId.concat(CIVIL_SERVANT_SUFFIX);
                break;
            default:
                mcswId = null;
        }
        return mcswId;
    }

    /**
     * @param mcswId 帖子id
     */
    private Optional<List<CommentVo>> getCommentsForOne(String mcswId){
        List<Comment> comments = commentDao.queryByMcswId(mcswId);
        if (CollectionUtil.isEmpty(comments)) {
            return Optional.empty();
        }
        Map<String, String> userVoMap = getUserVo(comments);
        List<Comment> top = comments.stream().filter(c -> c.getParentId() == null).collect(Collectors.toList());
        List<CommentVo> result = new ArrayList<>(top.size());
        top.forEach(t -> {
            CommentVo convert = convert(t, userVoMap);
            result.add(convert);
        });
        List<Comment> collect = comments.stream().filter(c -> c.getParentId() != null).collect(Collectors.toList());
        Map<Integer, List<Comment>> parentId2Comments = new HashMap<>();
        for (Comment comment : collect) {
            parentId2Comments.putIfAbsent(comment.getParentId(), new ArrayList<>());
            parentId2Comments.get(comment.getParentId()).add(comment);
        }
        merge(result, parentId2Comments, userVoMap);

        // TODO 填写用户昵称
        return Optional.of(result);
    }

    private Map<String, String> getUserVo(List<Comment> comments) {
        Set<Integer> userId = comments.stream().map(Comment::getUserId).collect(Collectors.toSet());
        CommonResult<List<UserVO>> userByIds = userClient.getUserByIds(userId);
        if (userByIds.getCode() == HttpStatus.HTTP_OK) {
            List<UserVO> data = userByIds.getData();
            Map<String, String> collect = data.stream()
                    .collect(Collectors.toMap(userVo -> userVo.getId(), userVo -> userVo.getName()));
            return collect;
        } else {
            log.error("查询用户信息出错。");
            // 出错则直接使用佚名代替
            return userId.stream().collect(Collectors.toMap(Object::toString, u -> UNKNOW_NAME));
        }
    }

    private void merge(List<CommentVo> tops, Map<Integer, List<Comment>> parentId2Comments, Map<String, String> userVoMap) {
        if (CollectionUtil.isEmpty(tops)) {
            return;
        }
        for (CommentVo top : tops) {
            List<Comment> comments = parentId2Comments.get(top.getId());
            List<CommentVo> subComment = top.getSubComment();
            for (Comment comment : comments) {
                CommentVo convert = convert(comment, userVoMap);
                subComment.add(convert);
            }
            merge(top.getSubComment(), parentId2Comments, userVoMap);
        }
    }

    /**
     *  转换成VO类
     */
    private static CommentVo convert(Comment comment, Map<String, String> userVoMap){
        return CommentVo.builder()
                .id(comment.getId())
                .createTime(DateTime.of(comment.getCreateTime()).toDateStr())
                .content(comment.getContent())
                .subComment(new ArrayList<>())
                .userName(userVoMap.get(comment.getUserId().toString()))
                .build();
    }


    @Autowired
    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }
    @Autowired
    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }
}

