package org.linlinjava.litemall.admin.web;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.admin.service.LogHelper;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallTickets;
import org.linlinjava.litemall.db.service.LitemallGoodsProductService;
import org.linlinjava.litemall.db.service.LitemallOrderGoodsService;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.service.LitemallTicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.linlinjava.litemall.admin.util.AdminResponseCode.*;
import static org.linlinjava.litemall.admin.util.AdminResponseCode.ORDER_REFUND_FAILED;
import static org.linlinjava.litemall.db.util.OrderUtil.STATUS_REFUND_CONFIRM;

@RestController
@RequestMapping("/admin/tickets")
@Validated
public class AdminTicketsController {
    private final Log logger = LogFactory.getLog(AdminTicketsController.class);

    @Autowired
    private LitemallTicketsService ticketsService;
    @Autowired
    private LitemallOrderService orderService;
    @Autowired
    private LitemallOrderGoodsService orderGoodsService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private LitemallGoodsProductService productService;
    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("admin:tickets:list")
    @RequiresPermissionsDesc(menu = {"门票管理", "门票管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(Integer id,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "create_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<LitemallTickets> ticketList = ticketsService.querySelective(id, page, limit, sort, order);
        return ResponseUtil.okList(ticketList);
    }

    @RequiresPermissions("admin:tickets:use")
    @RequiresPermissionsDesc(menu = {"门票管理", "门票管理"}, button = "核销")
    @PostMapping("/use")
    public Object update(@RequestBody LitemallTickets tickets) {
        if (tickets == null) {
            return ResponseUtil.badArgument();
        }

//        LitemallTickets tickets2 = ticketsService.findById(tickets.getId());
        LitemallTickets tickets2 = ticketsService.findBySelect(tickets.getId(), tickets.getUserId(), tickets.getOrderId());
        if (tickets2 == null) {
            return ResponseUtil.fail(TICKET_INVALID, "查无此门票");
        }

        if (tickets2.getUsed()) {
            return ResponseUtil.fail(TICKET_IS_USED, "门票已被使用");
        }

        if (tickets2.getDeleted()) {
            return ResponseUtil.fail(TICKET_INVALID, "门票已失效");
        }

        tickets2.setUsed(true);
        tickets2.setUseTime(LocalDateTime.now());
        ticketsService.updateById(tickets2);

        return ResponseUtil.ok(tickets2);
    }

    @RequiresPermissions("admin:tickets:refund")
    @RequiresPermissionsDesc(menu = {"门票管理", "门票管理"}, button = "退票")
    @PostMapping("/refund")
    public Object refund(@RequestBody LitemallTickets tickets) {
        if (tickets == null) {
            return ResponseUtil.badArgument();
        }

        LitemallTickets tickets2 = ticketsService.findBySelect(tickets.getId(), tickets.getUserId(), tickets.getOrderId());
        if (tickets2 == null) {
            return ResponseUtil.fail(TICKET_INVALID, "查无此门票");
        }

        if (tickets2.getUsed()) {
            return ResponseUtil.fail(TICKET_IS_USED, "门票已被使用");
        }

        if (tickets2.getDeleted()) {
            return ResponseUtil.fail(TICKET_INVALID, "门票已失效");
        }

        // AkariMarisa 获取门票对应的订单
        Integer orderId = tickets2.getOrderId();
        LitemallOrder order = orderService.findById(orderId);

        // AkariMarisa 确认这次退的门票对应订单的哪个商品，并得到商品价格
        List<LitemallOrderGoods> orderGoodsList = orderGoodsService.findByOidAndGid(orderId, tickets2.getGoodsId());
        if (orderGoodsList == null || orderGoodsList.size() <= 0) {
            return ResponseUtil.fail(TICKET_INVALID, "查无此门票");
        }
        LitemallOrderGoods orderGoods = orderGoodsList.get(0);
        BigDecimal ticketPrice = orderGoods.getPrice();

        // AkariMarisa 发起微信退款申请，退回部分门票金额
        // 微信退款
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        wxPayRefundRequest.setOutTradeNo(order.getOrderSn());
        wxPayRefundRequest.setOutRefundNo("refund_" + UUID.randomUUID().toString().replaceAll("-", ""));
        // 元转成分
        Integer totalFee = order.getActualPrice().multiply(new BigDecimal(100)).intValue();
        Integer ticketFee = ticketPrice.multiply(new BigDecimal(100)).intValue();
        wxPayRefundRequest.setTotalFee(totalFee);
        wxPayRefundRequest.setRefundFee(ticketFee);

        WxPayRefundResult wxPayRefundResult;
        try {
            wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
        } catch (WxPayException e) {
            logger.error(e.getMessage(), e);
            return ResponseUtil.fail(ORDER_REFUND_FAILED, "订单退款失败");
        }
        if (!wxPayRefundResult.getReturnCode().equals("SUCCESS")) {
            logger.warn("refund fail: " + wxPayRefundResult.getReturnMsg());
            return ResponseUtil.fail(ORDER_REFUND_FAILED, "订单退款失败");
        }
        if (!wxPayRefundResult.getResultCode().equals("SUCCESS")) {
            logger.warn("refund fail: " + wxPayRefundResult.getReturnMsg());
            return ResponseUtil.fail(ORDER_REFUND_FAILED, "订单退款失败");
        }

        // AkariMarisa 获取订单的可退款金额，并减去门票价格，更新订单剩余可退款金额
        BigDecimal refundablePrice = order.getRefundablePrice();
        refundablePrice = refundablePrice.subtract(ticketPrice);
        order.setRefundablePrice(refundablePrice);
        if (refundablePrice.compareTo(new BigDecimal(0)) == 0) {
            order.setOrderStatus(STATUS_REFUND_CONFIRM);
        }
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            throw new RuntimeException("更新数据已失效");
        }

        // AkariMarisa 删除对应的门票
        tickets2.setDeleted(true);
        ticketsService.updateById(tickets2);

        // 商品货品数量增加
        Integer productId = orderGoods.getProductId();
        Short number = orderGoods.getNumber();
        if (productService.addStock(productId, number) == 0) {
            throw new RuntimeException("商品货品库存增加失败");
        }

        logHelper.logOrderSucceed("退款", "订单编号 " + orderId);

        return ResponseUtil.ok();
    }
}
