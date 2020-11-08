package cn.jinterest.product.service.impl;

import cn.jinterest.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.Query;

import cn.jinterest.product.dao.CategoryDao;
import cn.jinterest.product.entity.CategoryEntity;
import cn.jinterest.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查出所有分类以及子分类，以树形结构组装
     *
     * @return
     */
    @Override
    public List<CategoryEntity> listWithTree() {

        // 1、查出所有分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        // 2、组装树形的父子树形结构
        //  ①找到所有的一级分类
        //  ②递归查询所有子菜单

        List<CategoryEntity> level1Menus = categoryEntities.stream().filter((categoryEntity) -> { // 进行过滤 得到一级分类
            return categoryEntity.getParentCid() == 0; // 如果分类的父id==0 则返回
        }).map((menu) -> { //map 方法用于映射每个元素到对应的结果，每个一级分类得到所有子菜单
            menu.setChildren(getChildren(menu, categoryEntities));
            return menu;
        }).sorted((menu1, menu2) -> {
            // menu1前面的菜单 menu2后面的菜单 对比排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public boolean removeByMuneByIds(List<Long> asList) {
        // TODO 检查当前删除的菜单，是否被别的地方引用,例如 属性分组有引用分类,品牌也有引用
        return baseMapper.deleteBatchIds(asList) > 0;
    }

    /**
     * 根据三级分类id，找到三级分类的完整路径
     *
     * @param id 三级分类id
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long id) {
        List<Long> list = new ArrayList<Long>();

        List<Long> paths = findParentPath(id, list);
        // 逆序集合
        Collections.reverse(paths);
        return paths.toArray(new Long[paths.size()]);
    }
    /**
     * 根据三级分类id，找到第三级分类的路径
     *
     * @param id 三级分类id
     * @return
     */
    @Override
    public String findCatelogPathName(Long id) {
        //得到三级分类名
        CategoryEntity ce = this.getById(id);
        return ce.getName();
    }

    /**
     * 级联更新
     * @param category
     */
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        // 更新关联表
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    /**
     * 递归查询分类路径
     *
     * @param id   分类id
     * @param list 存储分类id的集合
     * @return [三级, 二级, 一级]
     */
    private List<Long> findParentPath(Long id, List<Long> list) {
        // 存储当前分类的id
        list.add(id);
        CategoryEntity categoryEntity = this.getById(id);
        if (categoryEntity.getParentCid() != 0) {
            findParentPath(categoryEntity.getParentCid(), list);
        }
        return list;
    }
    /**
     * 递归查询所有菜单的子菜单
     *
     * @param root 当前菜单
     * @param all  所有菜单
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map((categoryEntity) -> {
            // 递归找到子菜单
            categoryEntity.setChildren(getChildren(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            // 菜单排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }

}
