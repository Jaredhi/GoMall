package cn.jinterest.thirdParty.controller;

import cn.jinterest.common.utils.R;
import cn.jinterest.thirdParty.component.OssComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * @Auther: AJun
 * @version:1.0
 * @Date: 2020/11/04  19:16
 * @Description:
 */
@RestController
public class OssController {

    @Autowired
    private OssComponent ossComponent;

    @GetMapping("/oss/policy")
    public R policy() {
        Map<String, String> policy = ossComponent.policy();

        return R.ok().put("data",policy);
    }

}
