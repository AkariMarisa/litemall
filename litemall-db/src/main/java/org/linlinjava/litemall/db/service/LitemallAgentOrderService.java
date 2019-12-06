package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.LitemallAgentOrderMapper;
import org.linlinjava.litemall.db.domain.LitemallAgentOrder;
import org.linlinjava.litemall.db.domain.LitemallAgentOrderExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class LitemallAgentOrderService {
    @Resource
    private LitemallAgentOrderMapper agentOrderMapper;

    public void add(LitemallAgentOrder agentOrder){
        agentOrderMapper.insertSelective(agentOrder);
    }

    public List<Integer> getOrderIdsByUserId(Integer userId){
        LitemallAgentOrderExample agentOrderExample = new LitemallAgentOrderExample();
        agentOrderExample.or().andUserIdEqualTo(userId);
        List<LitemallAgentOrder> agentOrderList = agentOrderMapper.selectByExample(agentOrderExample);
        List<Integer> orderIdList = new ArrayList<>(agentOrderList.size());
        for (LitemallAgentOrder a : agentOrderList) {
            orderIdList.add(a.getOrderId());
        }
        return orderIdList;
    }

    public LitemallAgentOrder findByOrderId(Integer orderId){
        LitemallAgentOrderExample agentOrderExample = new LitemallAgentOrderExample();
        agentOrderExample.or().andOrderIdEqualTo(orderId);
        return agentOrderMapper.selectOneByExample(agentOrderExample);
    }
}
