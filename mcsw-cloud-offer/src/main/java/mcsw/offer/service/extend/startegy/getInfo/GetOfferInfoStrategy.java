package mcsw.offer.service.extend.startegy.getInfo;

import cn.hutool.core.date.DateTime;
import mcsw.offer.entity.Offer;
import mcsw.offer.model.vo.MixFlauntVo;
import mcsw.offer.service.OfferService;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import mscw.common.util.DictionaryOfUser;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class GetOfferInfoStrategy implements GetInfoStrategy{

    private final OfferService offerService;

    public GetOfferInfoStrategy(OfferService offerService) {
        this.offerService = offerService;
    }

    @Override
    public int getCategory() {
        return FlauntStrategy.Category.OFFER.getTag();
    }

    @Override
    public Optional<MixFlauntVo> getInfo(int id) {
        Offer offer = offerService.getById(id);
        if (Objects.isNull(offer)) {
            return Optional.empty();
        }
        MixFlauntVo build = MixFlauntVo.builder().title(offer.getCompany())
                .remarks(offer.getRemarks())
                .city(offer.getCity())
                .createDay(DateTime.of(offer.getCreateTime()).toDateStr())
                .category(FlauntStrategy.Category.OFFER.getTag())
                .title(offer.getSalary())
                .userId(offer.getUserId())
                .positionName(offer.getPositionName())
                .company(offer.getCompany())
                .build();
        Map<Integer, String> code2Industry = DictionaryOfUser.getCODE_INDUSTRY();
        build.setIndustry(code2Industry.get(offer.getIndustry()));
        return Optional.of(build);
    }
}
