package cn.jinterest.product.service.impl;

import cn.jinterest.common.constant.pms.ProductConstant;
import cn.jinterest.product.dao.AttrAttrgroupRelationDao;
import cn.jinterest.product.dao.AttrGroupDao;
import cn.jinterest.product.dao.CategoryDao;
import cn.jinterest.product.entity.AttrAttrgroupRelationEntity;
import cn.jinterest.product.entity.AttrGroupEntity;
import cn.jinterest.product.entity.CategoryEntity;
import cn.jinterest.product.service.CategoryService;
import cn.jinterest.product.vo.AttrGroupRelationVo;
import cn.jinterest.product.vo.AttrRespVo;
import cn.jinterest.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.Query;

import cn.jinterest.product.dao.AttrDao;
import cn.jinterest.product.entity.AttrEntity;
import cn.jinterest.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }
    /**
     * 保存属性的基本信息和关联的表信息
     * @param attrVo
     */
    @Override
    public void saveAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.save(attrEntity);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attrVo.getAttrGroupId() != null) {
            // 保存关联关系的表信息
            // 查询是否已经有该条关联关系了
            relationDao.selectById(attrEntity.getAttrId());

            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrSort(0);
            relationDao.insert(attrAttrgroupRelationEntity);
        }

    }

    /**
     * 获取分类规格参数
     *
     * @param params
     * @param catelogId
     * @param type
     * @return
     */
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        // 定义查询条件
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("attr_type", "base".equalsIgnoreCase(type) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        if (catelogId != 0) {
            wrapper.eq("catelog_id", catelogId);
        }

        String key = (String) params.get("key");

        if (!StringUtils.isEmpty(key)) {
            wrapper.and( (obj)->{
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        // 开始处理分页信息一集表关联数据
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        PageUtils pageUtils = new PageUtils(page);

        List<AttrEntity> records = page.getRecords();

        List<AttrRespVo> respVoList = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            if ("base".equalsIgnoreCase(type)) { // 如果本次操作查询的是规格信息 才需要查询分组关联信息
                // 设置分组的名字
                // 1、查询当前属性的groupid pms_attr_attrgroup_relation
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrAttrgroupRelationEntity != null && attrAttrgroupRelationEntity.getAttrGroupId() != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                    if (attrGroupEntity != null){
                        attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                    }
                }
            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVoList);

        return pageUtils;
    }
    /**
     * 查询详细信息
     * @param attrId
     * @return
     */
    @Cacheable(value = "attr", key = "'attrinfo:'+#root.args[0]")
    @Override
    public AttrRespVo getAttrDetail(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);

        BeanUtils.copyProperties(attrEntity,attrRespVo);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) { // 如果当前的基本属性才需要查询属性关联分组信息
            // 关联查询分组的信息
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (attrAttrgroupRelationEntity != null) {
                attrRespVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getId());
                if (attrGroupEntity != null) {
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }

        // 设置分类的完整路径
        Long[] catelogPath = categoryService.findCatelogPath(attrEntity.getCatelogId());
        attrRespVo.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
        if (categoryEntity != null) {
            attrRespVo.setGroupName(categoryEntity.getName());
        }

        return attrRespVo;
    }

    /**
     * 保存详细信息
     * @param attr
     * 更新操作 使用失效模式 删除缓存，避免读取老数据
     */
    @CacheEvict(value = {"attr"}, allEntries = true)
    @Transactional
    @Override
    public void updateAttrDetail(AttrVo attr) {
        // 保存基本信息
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);


        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) { // 如果当前的基本属性才需要修改属性关联分组信息
            QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId());

            //由于属性在新增的时候可能没有选择分组，所以要先判断属性和分组关联表是否有当前需要修改的这条信息
            // 1、如果没有,执行新增操作，如果有则执行修改操作

            Integer count = relationDao.selectCount(wrapper);

            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
            if (count > 0) {
                // 修改分组关联信息
                relationDao.update(attrAttrgroupRelationEntity, wrapper);
            } else {
                attrAttrgroupRelationEntity.setAttrSort(0);
                relationDao.insert(attrAttrgroupRelationEntity);
            }
        }
    }
    /**
     * 根据分组id查询关联的所有属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getAttrRelation(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        if (attrAttrgroupRelationEntityList == null) return null;

        List<Long> attrIdList = attrAttrgroupRelationEntityList.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        if (attrIdList == null || attrIdList.size() == 0) {
            return null;
        }
        Collection<AttrEntity> attrEntities = this.listByIds(attrIdList);

        return (List<AttrEntity>) attrEntities;
    }
    /**
     * 获取分组未关联的属性
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        // 1、当前分组只能关联自己所属分类里的属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        // 2、当前分组只能关联别的分组没有引用的属性
        // 查询当前分类下的所有分组
        List<AttrGroupEntity> groupEntityList = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        // 获取这些分组的id
        List<Long> groupIds = groupEntityList.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        // 获取这些分组关联属性信息
        List<AttrAttrgroupRelationEntity> AttrAttrgroupRelationEntity = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", groupIds));

        // 获取这些分组属性id
        List<Long> attrIds = AttrAttrgroupRelationEntity.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        // 1、获取，除去当前分类的所有属性中移除这些属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds != null && attrIds.size() > 0) {
            wrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w)->{
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public void deleteAttrRelation(AttrGroupRelationVo[] attrGroupRelationVos) {
        //将vo转换成entity
        List<AttrAttrgroupRelationEntity> relationEntityList = Arrays.asList(attrGroupRelationVos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        relationDao.deleteBatchRelation(relationEntityList);
    }

    /**
     * 指定的的所有属性集合里，查询出可被检索的属性
     * @param attrIds 用来查询的属性id集合
     * @return
     */
    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {

        return this.baseMapper.selectSearchAttrs(attrIds);
    }
}
