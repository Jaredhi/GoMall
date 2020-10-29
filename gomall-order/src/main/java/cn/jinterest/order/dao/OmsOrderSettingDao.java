package cn.jinterest.order.dao;

import cn.jinterest.order.entity.OmsOrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 22:48:20
 */
@Mapper
public interface OmsOrderSettingDao extends BaseMapper<OmsOrderSettingEntity> {
	
}
