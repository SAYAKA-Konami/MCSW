package mcsw.offer.service.extend.startegy;

import mcsw.offer.model.dto.RequestShowOffDto;
import mscw.common.api.CommonResult;

import java.util.Map;

public interface FlauntStrategy{
    enum Category {
        OFFER(0), MASTER(1), CIVIL_SERVANT(2);
        private final int tag;

        Category(int tag) {
            this.tag = tag;
        }

        public int getTag() {
            return tag;
        }
    }
    CommonResult<String> flaunt(Map<String, String> header, RequestShowOffDto requestShowOffDto);

    int getCategory();

}


