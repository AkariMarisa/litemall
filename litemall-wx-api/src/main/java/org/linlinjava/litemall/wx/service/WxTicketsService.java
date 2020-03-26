package org.linlinjava.litemall.wx.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallTickets;
import org.linlinjava.litemall.db.service.LitemallTicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.linlinjava.litemall.wx.util.WxResponseCode.TICKET_INVALID;
import static org.linlinjava.litemall.wx.util.WxResponseCode.TICKET_UNKNOWN;

@Service
public class WxTicketsService {
    private final Log logger = LogFactory.getLog(WxTicketsService.class);

    @Autowired
    private LitemallTicketsService ticketsService;

    public Object list(Integer userId, Integer page, Integer limit, String sort, String order) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        List<LitemallTickets> ticketsList = ticketsService.queryByUser(userId, page, limit, sort, order);

        List<Map<String, Object>> ticketsVoList = new ArrayList<>(ticketsList.size());
        for (LitemallTickets t : ticketsList) {
            Map<String, Object> ticketsVo = new HashMap<>();
            ticketsVo.put("id", t.getId());
            ticketsVo.put("picUrl", t.getPicUrl());
            ticketsVo.put("goodsName", t.getGoodsName());
            ticketsVo.put("price", t.getPrice());
            ticketsVo.put("createTime", t.getCreateTime());
            ticketsVo.put("isUsed", t.getUsed());

            ticketsVoList.add(ticketsVo);
        }

        return ResponseUtil.okList(ticketsVoList, ticketsList);
    }

    public Object detail(Integer userId, Integer ticketId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        LitemallTickets ticket = ticketsService.findById(ticketId);
        if (null == ticket) {
            return  ResponseUtil.fail(TICKET_UNKNOWN, "门票不存在");
        }
        if (!ticket.getUserId().equals(userId)) {
            return ResponseUtil.fail(TICKET_INVALID, "不是当前用户的门票");
        }
        Map<String, Object> ticketVo = new HashMap<String, Object>();
        ticketVo.put("id", ticket.getId());

        Map<String, Object> result = new HashMap<String ,Object>();
        result.put("ticketInfo", ticketVo);
        return ResponseUtil.ok(result);

    }
}
