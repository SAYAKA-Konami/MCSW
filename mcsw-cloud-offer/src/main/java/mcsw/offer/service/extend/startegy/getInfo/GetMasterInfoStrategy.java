package mcsw.offer.service.extend.startegy.getInfo;

import cn.hutool.core.date.DateTime;
import mcsw.offer.entity.Master;
import mcsw.offer.model.vo.MixFlauntVo;
import mcsw.offer.service.MasterService;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;


@Component
public class GetMasterInfoStrategy implements GetInfoStrategy {

    private final MasterService masterService;

    private final static String RECOMMENDATION = "保研";

    public GetMasterInfoStrategy(MasterService masterService) {
        this.masterService = masterService;
    }

    @Override
    public int getCategory() {
        return FlauntStrategy.Category.MASTER.getTag();
    }

    @Override
    public Optional<MixFlauntVo> getInfo(int id) {
        Master dao = masterService.getById(id);
        if (Objects.isNull(dao)) {
            return Optional.empty();
        }
        MixFlauntVo build = MixFlauntVo.builder()
                .title(dao.getUniversity())
                .remarks(dao.getRemarks())
                .createDay(DateTime.of(dao.getCreateTime()).toDateStr())
                .category(FlauntStrategy.Category.MASTER.getTag())
                .userId(dao.getUserId())
                .score(dao.getScore())
                .code(dao.getCode())
                .masterMajor(dao.getMasterMajor())
                .build();
        // 如果是保研的那么master type一栏就写保研。如果是考研的填写分数
        if (dao.getType() == 1) {
            build.setMasterType(RECOMMENDATION);
        } else {
            build.setMasterType(dao.getScore().toString() + "分");
        }
        return Optional.of(build);

    }
}
