package cn.jinterest.member.dao;

import cn.jinterest.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:11:49
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {

    MemberLevelEntity getDefaultLevel();
}
