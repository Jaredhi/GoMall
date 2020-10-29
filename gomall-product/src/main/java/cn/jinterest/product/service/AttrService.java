package cn.jinterest.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:50
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

