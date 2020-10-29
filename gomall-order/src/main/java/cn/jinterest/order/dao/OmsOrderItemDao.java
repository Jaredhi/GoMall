package cn.jinterest.order.dao;

import cn.jinterest.order.entity.OmsOrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 22:48:20
 */
@Mapper
public interface OmsOrderItemDao extends BaseMapper<OmsOrderItemEntity> {
	
}
