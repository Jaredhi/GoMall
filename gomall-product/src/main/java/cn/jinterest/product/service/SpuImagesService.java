package cn.jinterest.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:49
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(Long aLong, List<String> images);
}

