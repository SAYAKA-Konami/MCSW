package mcsw.offer.service.extend.startegy.getInfo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.http.HttpStatus;
import mcsw.offer.client.UserClient;
import mcsw.offer.entity.Master;
import mcsw.offer.model.vo.MixFlauntVo;
import mcsw.offer.model.vo.UserOfFlauntVo;
import mcsw.offer.service.MasterService;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import mscw.common.api.CommonResult;
import mscw.common.domain.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import static mscw.common.config.Constants.SERVER_ERROR;
import java.util.List;


@Component
public class GetMasterInfoStrategy implements GetInfoStrategy {

    private final MasterService masterService;

    private final UserClient userClient;

    private final String RECOMMENDATION = "保研";

    public GetMasterInfoStrategy(MasterService masterService, UserClient userClient) {
        this.masterService = masterService;
        this.userClient = userClient;
    }

    @Override
    public int getCategory() {
        return FlauntStrategy.Category.MASTER.getTag();
    }

    @Override
    public CommonResult<MixFlauntVo> getInfo(int id) {
        Master dao = masterService.getById(id);
        Integer userId = dao.getUserId();
        CommonResult<List<UserVO>> userByIds = userClient.getUserByIds(ListUtil.of(userId));
        if (userByIds.getCode() == HttpStatus.HTTP_OK) {
            UserVO userVO = userByIds.getData().get(0);
            MixFlauntVo build = MixFlauntVo.builder()
                    .title(dao.getUniversity())
                    .subtitle(dao.getMasterMajor())
                    .remarks(dao.getRemarks())
                    .createDay(DateTime.of(dao.getCreateTime()).toDateStr())
                    .type(FlauntStrategy.Category.MASTER.getTag())
                    .build();
            // 如果是保研的那么“薪水”一栏就写保研。如果是考研的则在薪水一栏写分数
            if (dao.getType() == 1){
                build.setSalary(RECOMMENDATION);
            }else{
                build.setSalary(dao.getScore().toString() + "分");
            }
            UserOfFlauntVo userOfFlauntVo = new UserOfFlauntVo();
            BeanUtils.copyProperties(userVO, userOfFlauntVo);
            build.setUserOfFlauntVo(userOfFlauntVo);
            return CommonResult.success(build);
        }else{
            return CommonResult.failed(SERVER_ERROR);
        }
    }
}
