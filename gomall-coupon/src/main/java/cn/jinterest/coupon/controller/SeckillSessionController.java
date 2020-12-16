package cn.jinterest.coupon.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.jinterest.coupon.service.SeckillSkuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import cn.jinterest.coupon.entity.SeckillSessionEntity;
import cn.jinterest.coupon.service.SeckillSessionService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.R;



/**
 * 秒杀活动场次
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:08:51
 */
@RestController
@RequestMapping("coupon/seckillsession")
public class SeckillSessionController {
    @Autowired
    private SeckillSessionService seckillSessionService;

    @Autowired
    private SeckillSkuRelationService seckillSkuRelationService;
    /**
     * 获取最近三天秒杀的活动场次
     * @return
     */
    @GetMapping("/latest3DaySession")
    public R getLatest3DaySession() {
        List<SeckillSessionEntity> list = seckillSessionService.getLatest3DaySession();

        return R.ok().data(list);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("coupon:seckillsession:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillSessionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:seckillsession:info")
    public R info(@PathVariable("id") Long id){
		SeckillSessionEntity seckillSession = seckillSessionService.getById(id);

        return R.ok().put("seckillSession", seckillSession);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("coupon:seckillsession:save")
    public R save(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.save(seckillSession);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("coupon:seckillsession:update")
    public R update(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.updateById(seckillSession);

        return R.ok();
    }

    /**
     * 删除
     */
    @Transactional
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:seckillsession:delete")
    public R delete(@RequestBody Long[] ids){
		seckillSessionService.removeByIds(Arrays.asList(ids));
		//同时删除 SeckillSkuRelation
        Boolean b = seckillSkuRelationService.delBySessionIds(ids);
        return R.ok();
    }

}
