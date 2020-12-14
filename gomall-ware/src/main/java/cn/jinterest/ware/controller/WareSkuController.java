package cn.jinterest.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.jinterest.common.exception.BizCodeEnume;
import cn.jinterest.common.exception.NoStockException;
import cn.jinterest.common.to.SkuHasStockVo;
import cn.jinterest.ware.vo.WareSkuLockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.jinterest.ware.entity.WareSkuEntity;
import cn.jinterest.ware.service.WareSkuService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.R;



/**
 * 商品库存
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:16:36
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 锁定某个订单的库存
     * @param wareSkuLockVo
     * @return
     */
    @PostMapping("/lock/order")
    public R orderLocKStock(@RequestBody WareSkuLockVo wareSkuLockVo) {

        try {
            Boolean flag = wareSkuService.orderLocKStock(wareSkuLockVo);
            return R.ok();
        } catch (NoStockException e) {
            return R.error(BizCodeEnume.NO_STOCK_EXCEPTION.getCode(), BizCodeEnume.NO_STOCK_EXCEPTION.getMsg());
        }

    }

    /**
     * 查询sku是否有库存
     */
    @PostMapping("/hasStock")
    public R getSkusHasStock(@RequestBody List<Long> skuIds) {
        List<SkuHasStockVo> skuHasStockVos = wareSkuService.getSkusHasStock(skuIds);
        return R.ok().data(skuHasStockVos);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
