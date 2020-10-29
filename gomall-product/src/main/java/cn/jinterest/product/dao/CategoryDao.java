package cn.jinterest.product.dao;

import cn.jinterest.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:49
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
