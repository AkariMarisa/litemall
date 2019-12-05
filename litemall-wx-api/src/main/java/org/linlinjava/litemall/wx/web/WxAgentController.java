package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.WxAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代理服务
 */
@RestController
@RequestMapping("/wx/agent")
@Validated
public class WxAgentController {
    private final Log logger = LogFactory.getLog(WxAgentController.class);
    @Autowired
    private WxAgentService agentService;

    /**
     * 确认用户购买的代理
     *
     * @param userId 用户ID
     * @param body   {agentType: xxx, orderId: xxx}
     * @return 处理结果
     */
    @PutMapping("/confirmBuy")
    public Object confirmBuy(@LoginUser Integer userId, @RequestBody String body) {
        return agentService.confirmBuy(userId, body);
    }
}