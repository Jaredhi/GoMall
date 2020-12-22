package cn.jinterest.ware.dao;

import cn.jinterest.ware.entity.WareInfoEntity;
import cn.jinterest.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 仓库信息
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:16:36
 */
@Mapper
public interface WareInfoDao extends BaseMapper<WareInfoEntity> {

}
