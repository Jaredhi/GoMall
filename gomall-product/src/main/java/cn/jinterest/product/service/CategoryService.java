package cn.jinterest.product.service;

import cn.jinterest.product.vo.Catelog2Vo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 21:09:49
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    boolean removeByMuneByIds(List<Long> asList);

    /**
     * 根据三级分类id，找到三级分类的完整路径
     * @param id 三级分类id
     * @return
     */
    Long[] findCatelogPath(Long id);



    /**
     * 根据三级分类id，找到第三级分类的路径名
     * @param id 三级分类id
     * @return
     */
    String findCatelogPathName(Long id);

    /**
     * 级联更新
     * @param category
     */
    void updateCascade(CategoryEntity category);

    /**
     * 查询所有一级分类
     * @return
     */
    List<CategoryEntity> getLeve1Categorys();

    /**
     * 查询前台需要的分类信息
     * @return
     */
    Map<String, List<Catelog2Vo>> getCatalogJson();
}

