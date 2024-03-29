package cn.jinterest.search.webcontroller;


import cn.jinterest.search.service.MallSearchService;
import cn.jinterest.search.vo.SearchParam;
import cn.jinterest.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request) {

        String queryString = request.getQueryString();
        param.set_queryString(queryString);

        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result", result);
        return "list";
    }


}
