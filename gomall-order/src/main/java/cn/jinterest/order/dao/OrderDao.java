package cn.jinterest.order.dao;

import cn.jinterest.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:14:31
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
