package cn.jinterest.coupon.dao;

import cn.jinterest.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:08:51
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
