package cn.jinterest.member.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.Query;

import cn.jinterest.member.dao.MemberReceiveAddressDao;
import cn.jinterest.member.entity.MemberReceiveAddressEntity;
import cn.jinterest.member.service.MemberReceiveAddressService;


@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberReceiveAddressEntity> page = this.page(
                new Query<MemberReceiveAddressEntity>().getPage(params),
                new QueryWrapper<MemberReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据会员id查询收货地址
     * @param memberId
     * @return
     */
    @Override
    public List<MemberReceiveAddressEntity> getAddresses(Long memberId) {
        return this.list(new QueryWrapper<MemberReceiveAddressEntity>().eq("member_id", memberId));
    }
}
