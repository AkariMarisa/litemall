package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallPlatformViewMapper;
import org.linlinjava.litemall.db.domain.LitemallPlatformView;
import org.linlinjava.litemall.db.domain.LitemallPlatformViewExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class LitemallPlatformViewService {
    @Resource
    private LitemallPlatformViewMapper platformViewMapper;

    public LitemallPlatformView findTodayRecord() {
        LitemallPlatformViewExample example = new LitemallPlatformViewExample();
        example.or().andAddTimeGreaterThanOrEqualTo(Date.from(LocalDate.now().atStartOfDay(ZoneOffset.ofHours(8)).toInstant()).toInstant().atZone(ZoneOffset.ofHours(8)).toLocalDateTime());

        return this.platformViewMapper.selectOneByExample(example);
    }

    public void add(LitemallPlatformView platformView) {
        platformView.setAddTime(LocalDateTime.now());
        this.platformViewMapper.insertSelective(platformView);
    }

    public void update(LitemallPlatformView platformView) {
        this.platformViewMapper.updateByPrimaryKey(platformView);
    }
}
