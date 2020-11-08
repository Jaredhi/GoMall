package cn.jinterest.product.service;

import cn.jinterest.product.vo.AttrGroupWithAttrsVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:49
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据分类id分页查询
     * @param params
     * @param catelogId
     * @return
     */
    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    /**
     * 获取分类下所有分组&关联属性
     * @param catelogId
     * @return
     */
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);
}

