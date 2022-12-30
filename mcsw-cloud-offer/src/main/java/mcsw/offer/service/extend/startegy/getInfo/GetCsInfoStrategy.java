package mcsw.offer.service.extend.startegy.getInfo;

import cn.hutool.core.date.DateTime;
import mcsw.offer.entity.CivilServant;
import mcsw.offer.model.vo.MixFlauntVo;
import mcsw.offer.service.CivilServantService;
import mcsw.offer.service.extend.startegy.flaunt.FlauntStrategy;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class GetCsInfoStrategy implements GetInfoStrategy{

    private final CivilServantService civilServantService;

    public GetCsInfoStrategy(CivilServantService civilServantService) {
        this.civilServantService = civilServantService;
    }

    @Override
    public int getCategory() {
        return FlauntStrategy.Category.CIVIL_SERVANT.getTag();
    }

    @Override
    public Optional<MixFlauntVo> getInfo(int id) {
        CivilServant dao = civilServantService.getById(id);
        if (Objects.isNull(dao)) {
            // 如果数据库中没有该帖子信息则返回空
            return Optional.empty();
        }
        MixFlauntVo build = MixFlauntVo.builder()
                .title(dao.getPositionName())
                .createDay(DateTime.of(dao.getCreateTime()).toDateStr())
                .category(FlauntStrategy.Category.CIVIL_SERVANT.getTag())
                .remarks(dao.getRemarks())
                .userId(dao.getUserId())
                .csSalary(dao.getSalary())
                .build();
        return Optional.of(build);

    }
}
