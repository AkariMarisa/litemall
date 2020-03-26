package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.LitemallTickets;
import org.linlinjava.litemall.db.service.LitemallTicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/tickets")
@Validated
public class AdminTicketsController {
    private final Log logger = LogFactory.getLog(AdminTicketsController.class);

    @Autowired
    private LitemallTicketsService ticketsService;

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

        LitemallTickets tickets2 = ticketsService.findById(tickets.getId());
        if (tickets2 == null) {
            return ResponseUtil.badArgument();
        }

        tickets2.setUsed(true);
        tickets2.setUseTime(LocalDateTime.now());
        ticketsService.updateById(tickets2);

        return ResponseUtil.ok(tickets2);
    }
}
