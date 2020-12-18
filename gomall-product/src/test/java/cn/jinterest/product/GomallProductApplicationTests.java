package cn.jinterest.product;

import cn.jinterest.product.entity.AttrAttrgroupRelationEntity;
import cn.jinterest.product.entity.BrandEntity;
import cn.jinterest.product.service.AttrAttrgroupRelationService;
import cn.jinterest.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class GomallProductApplicationTests {
    @Autowired
    BrandService brandService;
    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;

//    @Test
//    void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();
////        brandEntity.setName("华为");
////        brandService.save(brandEntity);
////        System.out.println("保存成功....");
///*         brandEntity.setBrandId(1L);
//         brandEntity.setDescript("中华有为");
//         brandService.updateById(brandEntity);*/
//
//        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
//        list.forEach((item) -> {
//            System.out.println(item);
//        });
//    }




}
