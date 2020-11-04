package cn.jinterest.thirdParty.controller;

import cn.jinterest.common.utils.R;
import cn.jinterest.thirdParty.component.OssComponent;
import cn.jinterest.thirdParty.ossutils.OssPropertiesUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
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
