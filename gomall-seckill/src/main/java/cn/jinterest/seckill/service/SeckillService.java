package cn.jinterest.seckill.service;


import cn.jinterest.seckill.to.SecKillSkuRedsTo;

import java.util.List;


public interface SeckillService {
    /**
     * 每天晚上3点，上架最近三天需要参与秒杀的商品
     */
    void uploadSeckillSkuLatetst3Days();

    /**
     * 返回当前时间可以参与的秒杀的商品信息
     * @return
     */
    List<SecKillSkuRedsTo> getCurrentSecKillSkus();

    /**
     * 查询某个sku的秒杀信息
     * @param skuId
     * @return
     */
    SecKillSkuRedsTo getSkuSecKillInfo(Long skuId);

    /**
     * 秒杀商品
     * @param killId 场次id_商品skuid
     * @param key 秒杀需要携带的令牌
     * @param num 秒杀的数量
     * @return
     */
    String kill(String killId, String key, Integer num);
}

