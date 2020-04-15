package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.admin.vo.StatVo;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/stat")
@Validated
public class AdminStatController {
    private final Log logger = LogFactory.getLog(AdminStatController.class);

    @Autowired
    private StatService statService;

    @RequiresPermissions("admin:stat:user")
    @RequiresPermissionsDesc(menu = {"统计管理", "用户统计"}, button = "查询")
    @GetMapping("/user")
    public Object statUser() {
        List<Map> rows = statService.statUser();
        String[] columns = new String[]{"day", "users"};
        StatVo statVo = new StatVo();
        statVo.setColumns(columns);
        statVo.setRows(rows);
        return ResponseUtil.ok(statVo);
    }

    @RequiresPermissions("admin:stat:order")
    @RequiresPermissionsDesc(menu = {"统计管理", "订单统计"}, button = "查询")
    @GetMapping("/order")
    public Object statOrder() {
        List<Map> rows = statService.statOrder();
        String[] columns = new String[]{"day", "orders", "customers", "amount", "pcr"};
        StatVo statVo = new StatVo();
        statVo.setColumns(columns);
        statVo.setRows(rows);

        return ResponseUtil.ok(statVo);
    }

    @RequiresPermissions("admin:stat:goods")
    @RequiresPermissionsDesc(menu = {"统计管理", "商品统计"}, button = "查询")
    @GetMapping("/goods")
    public Object statGoods() {
        List<Map> rows = statService.statGoods();
        String[] columns = new String[]{"day", "orders", "products", "amount"};
        StatVo statVo = new StatVo();
        statVo.setColumns(columns);
        statVo.setRows(rows);
        return ResponseUtil.ok(statVo);
    }

    @RequiresPermissions("admin:stat:complex")
    @RequiresPermissionsDesc(menu = {"统计管理", "商城统计"}, button = "查询")
    @GetMapping("/complex")
    public Object statComplex(Integer days, Long start, Long end,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit) {
        Date startDate = start == null ? null : new Date(start);
        Date endDate = end == null ? null : new Date(end);
        List<Map> rows = statService.statComplex(days, startDate, endDate, page, limit);
        String[] columns = new String[]{"day", "orders", "products", "amount", "goodsViews", "virtualGoodsViews", "nonVirtualGoodsViews", "platformViews"};
        StatVo statVo = new StatVo();
        statVo.setColumns(columns);
        statVo.setRows(rows);
        Map<String, Object> resultMap = new HashMap<String ,Object>();
        resultMap.put("chartData", statVo);
        resultMap.put("listData", ResponseUtil.okList(rows));
        return ResponseUtil.ok(resultMap);
    }

}
