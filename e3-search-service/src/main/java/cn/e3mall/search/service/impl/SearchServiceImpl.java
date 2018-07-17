package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

/**
 * 商品收缩
 * <p>
 * Title: SearchServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.itcast.cn
 * </p>
 * 
 * @version 1.0
 */
@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	private SearchDao searchDao;

	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception {
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(keyword);
		if (page <= 0)
			page = 1;
		solrQuery.setStart((page - 1) * rows);
		solrQuery.setRows(rows);
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\" >");
		solrQuery.setHighlightSimplePost("</em>");

		SearchResult search = searchDao.search(solrQuery);
		long recordCount = search.getRecordCount();
		int totalPage = (int) Math.ceil(recordCount / (double) rows);
		search.setTotalPages(totalPage);
		return search;

	}

}
