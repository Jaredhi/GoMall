package cn.jinterest.ware.service.impl;

import cn.jinterest.common.utils.R;
import cn.jinterest.common.vo.SkuInfoVo;
import cn.jinterest.ware.entity.WareSkuEntity;
import cn.jinterest.ware.feign.ProductFeignService;
import cn.jinterest.ware.service.WareSkuService;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.Query;

import cn.jinterest.ware.dao.WareInfoDao;
import cn.jinterest.ware.entity.WareInfoEntity;
import cn.jinterest.ware.service.WareInfoService;
import org.springframework.util.StringUtils;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    WareSkuService wareSkuService;
    /**
     * 条件分页查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.eq("id",key).or()
                    .like("name",key)
                    .or().like("address",key)
                    .or().like("areacode",key);
        }

        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params), wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoVo> getSkuInfo() {
        R r = productFeignService.getSkuInfo();
        List<SkuInfoVo> result = new LinkedList<>();

        if (r.getCode()==0){
            //传入fastJson的TypeReference 进行泛型转换
            TypeReference<List<SkuInfoVo>> typeReference = new TypeReference<List<SkuInfoVo>>() {
            };

            List<SkuInfoVo> data = r.getData("skus",typeReference);
            List<Long> skuIds  = wareSkuService.list().stream().map(item-> item.getSkuId()).collect(Collectors.toList());
            //将已有库存的排除
            if (data!= null ){
                for (SkuInfoVo s:data
                ) {
                    if (!skuIds.contains(s.getSkuId())){
                        result.add(s);
                    }
                }
            }
        }

        return result;
    }

}
