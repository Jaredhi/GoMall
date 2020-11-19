package cn.jinterest.product.dao;

import cn.jinterest.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:49
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {
    /**
     * 修改spu的状态
     * @param spuId 商品spuid
     * @param code spu的状态
     */
    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
