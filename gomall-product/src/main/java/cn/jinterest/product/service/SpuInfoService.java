package cn.jinterest.product.service;

import cn.jinterest.product.vo.SpuSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:49
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo spuSaveVo);

    void saveBaseSpuInfo(SpuInfoEntity infoEntity);
    /**
     * 条件分页查询
     * @param params
     * @return
     */
    PageUtils queryPageByCondition(Map<String, Object> params);
    /**
     * 商品上架
     * @param spuId
     */
    void up(Long spuId);
    /**
     * 根据skuId查询spu信息
     * @param skuId
     * @return
     */
    SpuInfoEntity getSpuInfoBySkuId(Long skuId);

    /**
     * 商品下架
     * @param spuId
     */
    void down(Long spuId);
}

