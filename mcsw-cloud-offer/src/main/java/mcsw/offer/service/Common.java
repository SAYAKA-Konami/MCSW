package mcsw.offer.service;

import mcsw.offer.model.vo.MixBrowserVo;

public interface Common <T>{
    MixBrowserVo convertToMixBrowserVo(T t);
}
