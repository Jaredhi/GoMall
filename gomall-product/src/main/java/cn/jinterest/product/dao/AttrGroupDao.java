package cn.jinterest.product.dao;

import cn.jinterest.product.entity.AttrGroupEntity;
import cn.jinterest.product.vo.SpuItemAttrGroupVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:49
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    /**
     * 根据spuid获取所有分组&关联属性
     * @param catalogId
     * @param spuId
     * @return
     */
    List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(@Param("catalogId") Long catalogId, @Param("spuId") Long spuId);
}
