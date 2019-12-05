package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallLackeysMapper;
import org.linlinjava.litemall.db.domain.LitemallLackeys;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LitemallLackeysService {
    @Resource
    private LitemallLackeysMapper lackeysMapper;

    public void add(LitemallLackeys lackeys){
        lackeysMapper.insertSelective(lackeys);
    }
}
