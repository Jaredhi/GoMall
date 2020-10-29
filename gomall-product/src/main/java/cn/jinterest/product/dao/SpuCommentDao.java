package cn.jinterest.product.dao;

import cn.jinterest.product.entity.SpuCommentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价
 * 
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:50
 */
@Mapper
public interface SpuCommentDao extends BaseMapper<SpuCommentEntity> {
	
}
