package cn.jinterest.ware.dao;

import cn.jinterest.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:16:36
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
