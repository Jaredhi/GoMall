package cn.jinterest.ware.service.impl;

import cn.jinterest.ware.entity.PurchaseDetailEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.Query;

import cn.jinterest.ware.dao.PurchaseDao;
import cn.jinterest.ware.entity.PurchaseEntity;
import cn.jinterest.ware.service.PurchaseService;
import org.springframework.util.StringUtils;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {


        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<PurchaseEntity>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            //purchase_id  assignee_name
            queryWrapper.and(w->{
                w.eq("purchase_id",key).or().eq("sku_id",key).or().like("assignee_name",key);
            });
        }

        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            //purchase_id  sku_id
            queryWrapper.eq("status",status);
        }

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}
