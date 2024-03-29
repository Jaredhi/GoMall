package cn.jinterest.ware.service.impl;

import cn.jinterest.common.constant.wms.WareConstant;
import cn.jinterest.ware.entity.PurchaseDetailEntity;
import cn.jinterest.ware.service.PurchaseDetailService;
import cn.jinterest.ware.service.WareSkuService;
import cn.jinterest.ware.vo.MergeVo;
import cn.jinterest.ware.vo.PurchaseDoneVo;
import cn.jinterest.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.Query;

import cn.jinterest.ware.dao.PurchaseDao;
import cn.jinterest.ware.entity.PurchaseEntity;
import cn.jinterest.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {


        QueryWrapper<PurchaseEntity> queryWrapper = new QueryWrapper<PurchaseEntity>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            //purchase_id  assignee_name
            queryWrapper.and(w->{
                w.eq("assignee_id",key).or().eq("ware_id",key).or().like("assignee_name",key);
            });
        }

        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            //assignee_id  sku_id
            queryWrapper.eq("status",status);
        }

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 查询未领取的采购单
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    /**
     * 合并采购需求
     * @param mergeVo
     */
    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        //  判断提交数据中是否有携带 purchaseId: 1, //整单id
        Long purchaseId = mergeVo.getPurchaseId();

        if (purchaseId == null) { // 页面合并采购需求的时候没有选择合并到哪个采购单，则需要新建一个采购单
            PurchaseEntity purchaseEntity = new PurchaseEntity();

            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);

            purchaseId = purchaseEntity.getId();
        }


        List<Long> items = mergeVo.getItems();

        Long finalPurchaseId = purchaseId;

        List<PurchaseDetailEntity> collect = items.stream().filter(item ->{
            //  确认采购单的状态和采购项的状态是0-1，才可以合并
            PurchaseDetailEntity byId = purchaseDetailService.getById(item);
            if (byId.getStatus() == WareConstant.PurchaseDetailStatusEnum.CREATED.getCode() || byId.getStatus() == WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode()){
                return true;
            }
            return false;
        }).map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();

            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        if (collect != null){
            purchaseDetailService.updateBatchById(collect);
        }


        if (purchaseId != null) {
            // 更新采购单的时间
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setId(purchaseId);
            purchaseEntity.setUpdateTime(new Date());
            this.updateById(purchaseEntity);
        }

    }
    /**
     * 领取采购单
     * @param ids 采购单id
     */
    @Override
    public void received(List<Long> ids) {

        //1、确认当前采购单是新建或者已分配状态
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(item -> { // 过滤， 当前的采购单的状态是新建或者已分配
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item->{
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

        //2、改变采购单的状态
        this.updateBatchById(collect);

        //3、改变采购项的状态
        collect.forEach((item)->{
            List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> detailEntities = entities.stream().map(entity -> {
                PurchaseDetailEntity entity1 = new PurchaseDetailEntity();
                entity1.setId(entity.getId());
                entity1.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return entity1;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntities);
        });

    }

    /**
     * 完成采购单
     * @param doneVo
     */
    @Override
    public void done(PurchaseDoneVo doneVo) {
        // 当前需要修改的采购单id
        Long id = doneVo.getId();

        //2、改变采购项的状态
        Boolean flag = true;

        List<PurchaseItemDoneVo> items = doneVo.getItems();

        List<PurchaseDetailEntity> updates = new ArrayList<>();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            // TODO 完善采购失败的情况  1.压根没买到 2.买到了但是没完成实际需要的数量
            // 如果有采购项失败
            if(item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()){
                flag = false;
                detailEntity.setStatus(item.getStatus());
            }else{ // 采购成功
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());

                //3、将成功采购项的进行入库
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId()); // 根据采购项id查出采购项的详细信息
                wareSkuService.addStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());

            }
            detailEntity.setId(item.getItemId());
            updates.add(detailEntity);
        }
        // 批量更新采购项
        purchaseDetailService.updateBatchById(updates);

        //1、改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }

}
