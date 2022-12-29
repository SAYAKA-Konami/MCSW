package mcsw.offer.service.extend.startegy.getInfo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.http.HttpStatus;
import mcsw.offer.client.UserClient;
import mcsw.offer.entity.Offer;
import mcsw.offer.model.vo.MixFlauntVo;
import mcsw.offer.model.vo.UserOfFlauntVo;
import mcsw.offer.service.OfferService;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import mscw.common.api.CommonResult;
import mscw.common.domain.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static mscw.common.config.Constants.SERVER_ERROR;

@Component
public class GetOfferInfoStrategy implements GetInfoStrategy{

    private final OfferService offerService;
    private final UserClient userClient;

    public GetOfferInfoStrategy(OfferService offerService, UserClient userClient) {
        this.offerService = offerService;
        this.userClient = userClient;
    }

    @Override
    public int getCategory() {
        return FlauntStrategy.Category.OFFER.getTag();
    }

    @Override
    public CommonResult<MixFlauntVo> getInfo(int id) {
        Offer offer = offerService.getById(id);
        Integer userId = offer.getUserId();
        CommonResult<List<UserVO>> userByIds = userClient.getUserByIds(ListUtil.of(userId));
        if (userByIds.getCode()== HttpStatus.HTTP_OK) {
            UserVO userVO = userByIds.getData().get(0);
            MixFlauntVo build = MixFlauntVo.builder().title(offer.getCompany())
                    .remarks(offer.getRemarks())
                    .salary(offer.getSalary())
                    .subtitle(offer.getPositionName())
                    .city(offer.getCity())
                    .createDay(DateTime.of(offer.getCreateTime()).toDateStr())
                    .type(FlauntStrategy.Category.OFFER.getTag())
                    .build();
            UserOfFlauntVo userOfFlauntVo = new UserOfFlauntVo();
            BeanUtils.copyProperties(userVO, userOfFlauntVo);
            build.setUserOfFlauntVo(userOfFlauntVo);
            return CommonResult.success(build);
        }else{
            return CommonResult.failed(SERVER_ERROR);
        }
    }
}
