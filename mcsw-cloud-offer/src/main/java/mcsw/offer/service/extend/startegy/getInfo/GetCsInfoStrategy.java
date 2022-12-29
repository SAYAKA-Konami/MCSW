package mcsw.offer.service.extend.startegy.getInfo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.http.HttpStatus;
import mcsw.offer.client.UserClient;
import mcsw.offer.entity.CivilServant;
import mcsw.offer.model.vo.MixFlauntVo;
import mcsw.offer.service.CivilServantService;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import mscw.common.api.CommonResult;
import mscw.common.domain.vo.UserVO;
import org.springframework.stereotype.Component;

import java.util.List;

import static mscw.common.config.Constants.SERVER_ERROR;

@Component
public class GetCsInfoStrategy implements GetInfoStrategy{

    private final CivilServantService civilServantService;

    private final UserClient userClient;

    public GetCsInfoStrategy(CivilServantService civilServantService, UserClient userClient) {
        this.civilServantService = civilServantService;
        this.userClient = userClient;
    }

    @Override
    public int getCategory() {
        return FlauntStrategy.Category.CIVIL_SERVANT.getTag();
    }

    @Override
    public CommonResult<MixFlauntVo> getInfo(int id) {
        CivilServant dao = civilServantService.getById(id);
        Integer userId = dao.getUserId();
        CommonResult<List<UserVO>> userByIds = userClient.getUserByIds(ListUtil.of(userId));
        if (userByIds.getCode() == HttpStatus.HTTP_OK) {
            MixFlauntVo build = MixFlauntVo.builder()
                    .title(dao.getPositionName())
                    .salary(dao.getSalary())
                    .createDay(DateTime.of(dao.getCreateTime()).toDateStr())
                    .subtitle(dao.getCity())
                    .type(FlauntStrategy.Category.CIVIL_SERVANT.getTag())
                    .build();
            return CommonResult.success(build);
        }else{
            return CommonResult.failed(SERVER_ERROR);
        }
    }
}
