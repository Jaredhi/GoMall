package cn.jinterest.product.service.impl;

import cn.jinterest.product.service.CategoryBrandRelationService;
import cn.jinterest.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
     * 查询所有一级分类
     * <p>
     * 1、每一个需要缓存的数据我们都要来指定放到哪个名字的缓存；【按照业务类型来划分取名】
     * 2、@Cacheable({"category"})
     * 当前方法的结果需要缓存 如果缓存中有，方法不调用；
     * 如果缓存中没有，会调用该方法，最后将方法的结果放入缓存
     * 3、默认行为
     * 1)、默认缓存不过期
     * 4、自定义
     * 1)、指定缓存生成指定的key
     * 2)、指定缓存的过期时间  配置文件修改ttl
     * 3)、将缓存的value保存为json格式
     *
     * @return
     */
    //@Cacheable(value = {"category"}, key = "#root.method.name", sync = true)
    @Override
    public List<CategoryEntity> getLeve1Categorys() {
        System.out.println("CategoryServiceImpl.getLeve1Categorys (获取一级分类)调用了");
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }
    /**
     * 根据分类的parent_cid获取分类数据
     */
    private List<CategoryEntity> getParentCid(List<CategoryEntity> categoryEntityList, Long parentCid) {
        // return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
        // 优化 不重复查数据库，通过传进来的数据进行过滤
        return categoryEntityList.stream().filter(item -> item.getParentCid() == parentCid).collect(Collectors.toList());
    }

    /**
     * 查询前台需要显示的分类数据 - 使用spring cache框架缓存
     */
    //@Cacheable(value = {"category"}, key = "#root.methodName", sync = true)
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {

        System.out.println("查询了数据库。。。。");
        List<CategoryEntity> categoryEntityList = baseMapper.selectList(null);

        // 1、查询所有一级分类
        List<CategoryEntity> leve1Categorys = getParentCid(categoryEntityList, 0L);

        // 2、封装需要的数据  Collectors.toMap(key,value)
        Map<String, List<Catelog2Vo>> map = leve1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {

            // 根据每个一级分类，查询到他的二级分类
            List<CategoryEntity> category2EntityList = getParentCid(categoryEntityList, v.getCatId());

            // 抽取出前台需要的的二级分类vo
            List<Catelog2Vo> l2VoList = null;

            if (category2EntityList != null) {

                l2VoList = category2EntityList.stream().map(l2 -> {
                    // 当前一级分类的2级分类vo
                    Catelog2Vo l2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());

                    // 封装当前二级分类vo的三级分类vo
                    List<CategoryEntity> l3EntityList = getParentCid(categoryEntityList, l2.getCatId());
                    List<Catelog2Vo.Catelog3Vo> l3VoList = null;
                    if (l3EntityList != null) {
                        l3VoList = l3EntityList.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo l3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return l3Vo;
                        }).collect(Collectors.toList());
                    }
                    // 给二级分类vo设置封装好的三级分类vo
                    l2Vo.setCatalog3List(l3VoList);
                    return l2Vo;
                }).collect(Collectors.toList());
            }
            return l2VoList;
        }));

        return map;
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
