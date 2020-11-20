package cn.jinterest.product.webcontroller;


import cn.jinterest.product.entity.CategoryEntity;
import cn.jinterest.product.service.CategoryService;
import cn.jinterest.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

//    @Autowired
//    RedissonClient redisson;

    /* 首页面跳转 */
    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {

        List<CategoryEntity> categoryEntityList = categoryService.getLeve1Categorys();

        model.addAttribute("categorys", categoryEntityList);

        return "index";
    }

//    之前请求的是index/json/catalog.json静态文件
    @GetMapping("/index/catalog.json")
    @ResponseBody
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }

//    @ResponseBody
//    @GetMapping("/hello")
//    public String hello() {
//        RLock mylock = redisson.getLock("mylock");
//
//        mylock.lock(); // 加锁 阻塞式等待
//        try {
//            System.out.println(Thread.currentThread().getId() + "---> 加锁成功");
//            Thread.sleep(10000);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            mylock.unlock(); // 解锁
//            System.out.println(Thread.currentThread().getId() + " ---> 释放锁");
//        }
//
//        return "hello";
//    }
    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}

