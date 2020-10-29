package cn.jinterest.ware.dao;

import cn.jinterest.ware.entity.WmsWareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 22:48:49
 */
@Mapper
public interface WmsWareSkuDao extends BaseMapper<WmsWareSkuEntity> {
	
}
