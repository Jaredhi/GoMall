package cn.jinterest.search.service;


import cn.jinterest.search.vo.SearchParam;
import cn.jinterest.search.vo.SearchResult;

public interface MallSearchService {

    /**
     * 去es检索需要的信息
     * @param param 检索的所有参数
     * @return 返回检索的结果,页面需要显示的所以数据
     */
    SearchResult search(SearchParam param);
}
