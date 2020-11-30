package cn.jinterest.member.service;

import cn.jinterest.member.exception.PhoneExistException;
import cn.jinterest.member.exception.UserNameExistException;
import cn.jinterest.member.vo.MemberLoginVo;
import cn.jinterest.member.vo.MemberRegistVo;
import cn.jinterest.member.vo.SocialUserVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:11:49
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 会员注册
     * @param memberRegistVo
     */
    void regist(MemberRegistVo memberRegistVo);

    // 校验用户名是否已存在
    void checkUsernameUnique(String userName) throws UserNameExistException;
    // 校验手机是否已存在
    void checkPhonelUnique(String phone) throws PhoneExistException;

    /**
     * 会员登录
     * @param memberLoginVo
     * @return
     */
    MemberEntity login(MemberLoginVo memberLoginVo);

    /**
     * 会员社交登录或注册
     * @param socialUserVo
     * @return
     */
    MemberEntity login(SocialUserVo socialUserVo) throws Exception;
}

