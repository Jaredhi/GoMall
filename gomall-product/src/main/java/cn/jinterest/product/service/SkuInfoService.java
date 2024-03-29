package cn.jinterest.product.service;

import cn.jinterest.common.vo.SkuInfoVo;
import cn.jinterest.product.vo.SkuItemVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.product.entity.SkuInfoEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:50
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 保存sku的基本信息；pms_sku_info
     * @param skuInfoEntity
     */
    void saveSkuInfo(SkuInfoEntity skuInfoEntity);
    /**
     * sku检索分页查询
     * @param params
     * @return
     */
    PageUtils queryPageByCondition(Map<String, Object> params);
    /**
     * 根据spuId查询所有sku信息
     * @param spuId
     * @return
     */
    List<SkuInfoEntity> getSkusBySpuId(Long spuId);
    /**
     * 前台查询商品详情页信息
     * @param skuId
     * @return
     */
    SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException;

    /**
     * 获取所有sku
     * @return
     */
    List<SkuInfoVo> getSkus();
}

