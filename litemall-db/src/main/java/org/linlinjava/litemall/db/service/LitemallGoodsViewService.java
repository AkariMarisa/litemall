package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallGoodsViewMapper;
import org.linlinjava.litemall.db.domain.LitemallGoodsView;
import org.linlinjava.litemall.db.domain.LitemallGoodsViewExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class LitemallGoodsViewService {
    @Resource
    private LitemallGoodsViewMapper goodsViewMapper;

    public LitemallGoodsView findTodayRecord(Integer goodsId) {
        LitemallGoodsViewExample example = new LitemallGoodsViewExample();
        example.or().andAddTimeGreaterThanOrEqualTo(Date.from(LocalDate.now().atStartOfDay(ZoneOffset.ofHours(8)).toInstant()).toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDateTime()).andGoodsIdEqualTo(goodsId);

        return this.goodsViewMapper.selectOneByExample(example);
    }

    public void update(LitemallGoodsView goodsView) {
        this.goodsViewMapper.updateByPrimaryKey(goodsView);
    }

    public void add(LitemallGoodsView goodsView) {
        goodsView.setAddTime(LocalDateTime.now());
        this.goodsViewMapper.insertSelective(goodsView);
    }
}
