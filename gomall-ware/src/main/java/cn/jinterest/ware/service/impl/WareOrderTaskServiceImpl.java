package cn.jinterest.ware.service.impl;

import cn.jinterest.ware.entity.PurchaseDetailEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.Query;

import cn.jinterest.ware.dao.WareOrderTaskDao;
import cn.jinterest.ware.entity.WareOrderTaskEntity;
import cn.jinterest.ware.service.WareOrderTaskService;


@Service("wareOrderTaskService")
public class WareOrderTaskServiceImpl extends ServiceImpl<WareOrderTaskDao, WareOrderTaskEntity> implements WareOrderTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<PurchaseDetailEntity>();

        IPage<WareOrderTaskEntity> page = this.page(
                new Query<WareOrderTaskEntity>().getPage(params),
                new QueryWrapper<WareOrderTaskEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据订单号查询库存工作单信息
     * @param orderSn
     * @return
     */
    @Override
    public WareOrderTaskEntity getByOrderSn(String orderSn) {
        return this.getOne(new QueryWrapper<WareOrderTaskEntity>().eq("order_sn", orderSn));
    }

}
