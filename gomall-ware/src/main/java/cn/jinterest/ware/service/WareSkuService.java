package cn.jinterest.ware.service;

import cn.jinterest.common.to.SkuHasStockVo;
import cn.jinterest.common.to.mq.OrderTo;
import cn.jinterest.common.to.mq.StockLockedTo;
import cn.jinterest.ware.vo.WareSkuLockVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.ware.entity.WareSkuEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:16:36
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 将成功采购项的进行入库
     * @param skuId 商品skuid
     * @param wareId 仓库id
     * @param skuNum 采购数量
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);
    /**
     * 查询sku是否有库存
     * @param skuIds 要查询的skuId集合
     * @return
     */
    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);
    /**
     * 锁定某个订单的库存
     * @param wareSkuLockVo
     * @return
     */
    Boolean orderLocKStock(WareSkuLockVo wareSkuLockVo);

    /**
     * 构建解锁库存
     * @param stockLockedTo
     */
    void buidUnlockStock(StockLockedTo stockLockedTo);

    /**
     * 订单关闭解锁库存
     * @param orderTo
     */
    void buidUnlockStock(OrderTo orderTo);


}

