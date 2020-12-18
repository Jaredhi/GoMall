package cn.jinterest.product.service;

import cn.jinterest.product.vo.AttrGroupRelationVo;
import cn.jinterest.product.vo.AttrRespVo;
import cn.jinterest.product.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.product.entity.AttrEntity;

import java.util.List;
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

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrDetail(Long attrId);

    void updateAttrDetail(AttrVo attr);
    /**
     * 根据分组id查询关联的所有属性
     * @param attrgroupId
     * @return
     */
    List<AttrEntity> getAttrRelation(Long attrgroupId);

    /**
     * 获取分组未关联的属性
     * @param params
     * @param attrgroupId
     * @return
     */
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    void deleteAttrRelation(AttrGroupRelationVo[] attrGroupRelationVos);
    /**
     * 指定的的所有属性集合里，查询出可被检索的属性
     * @param attrIds 用来查询的属性id集合
     * @return
     */
    List<Long> selectSearchAttrs(List<Long> attrIds);


}

