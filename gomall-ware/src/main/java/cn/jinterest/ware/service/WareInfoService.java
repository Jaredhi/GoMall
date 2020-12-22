package cn.jinterest.ware.service;

import cn.jinterest.common.vo.SkuInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.ware.entity.WareInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 仓库信息
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:16:36
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuInfoVo> getSkuInfo();
}

