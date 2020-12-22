package cn.jinterest.product.dao;

import cn.jinterest.common.vo.SkuInfoVo;
import cn.jinterest.product.entity.SkuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * sku信息
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:50
 */
@Mapper
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {

    List<SkuInfoVo> getSkus();
}
