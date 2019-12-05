package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallLackeysMapper;
import org.linlinjava.litemall.db.domain.LitemallLackeys;
import org.linlinjava.litemall.db.domain.LitemallLackeysExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LitemallLackeysService {
    @Resource
    private LitemallLackeysMapper lackeysMapper;

    public void add(LitemallLackeys lackeys){
        lackeysMapper.insertSelective(lackeys);
    }

    public List<LitemallLackeys> list(Integer agentId){
        LitemallLackeysExample lackeysExample = new LitemallLackeysExample();
        lackeysExample.or().andAgentUserIdEqualTo(agentId);
        return lackeysMapper.selectByExample(lackeysExample);
    }
}
