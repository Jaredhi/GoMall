package cn.jinterest.product.service.impl;

import cn.jinterest.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

}
