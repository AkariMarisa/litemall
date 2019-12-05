package org.linlinjava.litemall.wx.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.JacksonUtil;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallUser;
import org.linlinjava.litemall.db.service.LitemallOrderGoodsService;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 代理服务
 */
@Service
public class WxAgentService {
    private final Log logger = LogFactory.getLog(WxAgentService.class);
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private LitemallOrderService orderService;
    @Autowired
    private LitemallOrderGoodsService orderGoodsService;

    /**
     * 确认用户购买的代理
     *
     * @param userId 用户ID
     * @param body   {agentType: xxx, orderId: xxx}
     * @return 处理结果
     */
    @Transactional
    public Object confirmBuy(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        if (body == null) {
            return ResponseUtil.badArgument();
        }
        Integer agentType = JacksonUtil.parseInteger(body, "agentType");
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");

        // AkariMarisa 在收到请求时，确认当前用户代理状态与购买订单状态，然后修改用户的代理级别到对应级别上
        if (orderId == null || orderId <= 0) {
            return ResponseUtil.badArgument();
        }

        // AkariMarisa 查询订单状态
        LitemallOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }

        // AkariMarisa 获取购买的代理类型
        List<LitemallOrderGoods> orderGoodsList = orderGoodsService.findByOidAndGid(orderId, agentType);
        LitemallOrderGoods orderGoods = null;
        if (orderGoodsList.size() > 1) {
            return ResponseUtil.serious();
        } else if (orderGoodsList.size() == 1) {
            orderGoods = orderGoodsList.get(0);
        } else {
            return ResponseUtil.fail();
        }

        if (!orderGoods.getGoodsId().equals(agentType)) {
            return ResponseUtil.fail();
        }

        // AkariMarisa 更新用户对应的代理等级
        LitemallUser user = userService.findById(userId);
        user.setAgentLevel(agentType);
        userService.updateById(user);

        return ResponseUtil.ok();
    }
}
