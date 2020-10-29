package cn.jinterest.member.dao;

import cn.jinterest.member.entity.UmsMemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-29 22:47:43
 */
@Mapper
public interface UmsMemberDao extends BaseMapper<UmsMemberEntity> {
	
}
