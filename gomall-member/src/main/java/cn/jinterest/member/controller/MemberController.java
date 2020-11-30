package cn.jinterest.member.controller;

import java.util.Arrays;
import java.util.Map;

import cn.jinterest.common.exception.BizCodeEnume;
import cn.jinterest.member.exception.PhoneExistException;
import cn.jinterest.member.exception.UserNameExistException;
import cn.jinterest.member.vo.MemberLoginVo;
import cn.jinterest.member.vo.MemberRegistVo;
import cn.jinterest.member.vo.SocialUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.jinterest.member.entity.MemberEntity;
import cn.jinterest.member.service.MemberService;
import cn.jinterest.common.utils.PageUtils;
import cn.jinterest.common.utils.R;



/**
 * 会员
 *
 * @author JInterest
 * @email hwj2586@163.com
 * @date 2020-10-31 14:11:49
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    /**
     * 会员社交登录
     * @param socialUserVo
     * @return
     */
    @PostMapping("/oauth/login")
    public R oauthLogin(@RequestBody SocialUserVo socialUserVo) throws Exception {
        MemberEntity memberEntity = memberService.login(socialUserVo);
        if (memberEntity != null) {
            return R.ok().data(memberEntity);
        } else {
            return R.error(BizCodeEnume.LOGINACCT_PASSSWORD_INVAILD_EXCEPTION.getCode(), BizCodeEnume.LOGINACCT_PASSSWORD_INVAILD_EXCEPTION.getMsg());
        }
    }


    /**
     * 会员注册
     * @param memberRegistVo
     * @return
     */
    @RequestMapping("/regist")
    public R regist(@RequestBody MemberRegistVo memberRegistVo){

        try {
            memberService.regist(memberRegistVo);
        } catch (UserNameExistException e) {
            R.error(BizCodeEnume.USER_EXIST_EXCEPTION.getCode(), BizCodeEnume.USER_EXIST_EXCEPTION.getMsg());
        } catch (PhoneExistException e) {
            R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnume.PHONE_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    /**
     * 会员登录
     * @param memberLoginVo
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo memberLoginVo) {
        MemberEntity memberEntity = memberService.login(memberLoginVo);
        if (memberEntity != null) {
            return R.ok().data(memberEntity);
        } else {
            return R.error(BizCodeEnume.LOGINACCT_PASSSWORD_INVAILD_EXCEPTION.getCode(), BizCodeEnume.LOGINACCT_PASSSWORD_INVAILD_EXCEPTION.getMsg());
        }
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
