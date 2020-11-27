package cn.jinterest.product.service.impl;

import cn.jinterest.product.entity.AttrEntity;
import cn.jinterest.product.service.AttrService;
import cn.jinterest.product.service.CategoryService;
import cn.jinterest.product.vo.AttrGroupWithAttrsVo;
import cn.jinterest.product.vo.SpuItemAttrGroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.Query;

import cn.jinterest.product.dao.AttrGroupDao;
import cn.jinterest.product.entity.AttrGroupEntity;
import cn.jinterest.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((obj) -> {
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }
        if (catelogId == 0) { // 如果分类id不等于0  当前是查询所有的分组信息
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),wrapper
            );
            //添加所属分类路径名
            List<AttrGroupEntity> records = page.getRecords();
            Long id = null;
            for (AttrGroupEntity a:records
                 ) {
                id = a.getCatelogId();
                String catelogPathName = categoryService.findCatelogPathName(id);
                a.setCatelogPathName(catelogPathName);
            }

            return new PageUtils(page);
        } else {
            wrapper.eq("catelog_id", catelogId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),wrapper);

            //添加所属分类路径名
            List<AttrGroupEntity> records = page.getRecords();
            Long id = null;
            for (AttrGroupEntity a:records
            ) {
                id = a.getCatelogId();
                String catelogPathName = categoryService.findCatelogPathName(id);
                a.setCatelogPathName(catelogPathName);
            }

            return new PageUtils(page);
        }
    }

    /**
     * 根据分类id查出所有的分组以及这些组里面的属性
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {

        //1、查询当前分类下的分组信息
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        //2、查询所有属性
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(group -> {
            AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group,attrsVo);
            List<AttrEntity> attrs = attrService.getAttrRelation(attrsVo.getAttrGroupId());
            attrsVo.setAttrs(attrs);
            return attrsVo;
        }).collect(Collectors.toList());

        return collect;


    }

    /**
     * 根据spuid获取所有分组&关联属性
     * @param catalogId
     * @param spuId
     * @return
     */
    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long catalogId, Long spuId) {

        //1、查询分组信息
        //2、查询所有属性
        List<SpuItemAttrGroupVo> spuItemAttrGroupVos = this.baseMapper.getAttrGroupWithAttrsBySpuId(catalogId, spuId);


        return spuItemAttrGroupVos;
    }

}
