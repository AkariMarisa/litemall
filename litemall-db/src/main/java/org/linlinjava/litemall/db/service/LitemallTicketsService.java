package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallTicketsMapper;
import org.linlinjava.litemall.db.domain.LitemallTickets;
import org.linlinjava.litemall.db.domain.LitemallTicketsExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LitemallTicketsService {
    @Resource
    private LitemallTicketsMapper ticketsMapper;

    public int add(LitemallTickets tickets) {
        tickets.setCreateTime(LocalDateTime.now());
        tickets.setUsed(false);
        return ticketsMapper.insertSelective(tickets);
    }

    public LitemallTickets findById(Integer ticketId) {
        return ticketsMapper.selectByPrimaryKey(ticketId);
    }

    public List<LitemallTickets> queryByUser(Integer userId, Integer page, Integer limit, String sort, String order) {
        LitemallTicketsExample example = new LitemallTicketsExample();
        example.setOrderByClause(LitemallTickets.Column.createTime.desc());
        LitemallTicketsExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return ticketsMapper.selectByExample(example);
    }

    public List<LitemallTickets> querySelective(Integer id, Integer page, Integer limit, String sort, String order) {
        LitemallTicketsExample example = new LitemallTicketsExample();
        LitemallTicketsExample.Criteria criteria = example.createCriteria();

        if (id != null) {
            criteria.andIdEqualTo(id);
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return ticketsMapper.selectByExample(example);
    }

    public int updateById(LitemallTickets tickets) {
        return ticketsMapper.updateByPrimaryKeySelective(tickets);
    }

}
