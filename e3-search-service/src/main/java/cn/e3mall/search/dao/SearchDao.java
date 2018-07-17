package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
@Repository
public class SearchDao {
	@Autowired
	private SolrServer solrServer;

	public SearchResult search(SolrQuery solrQuery) throws Exception {
		QueryResponse response = solrServer.query(solrQuery);
		SolrDocumentList results = response.getResults();
		SearchResult searchResult = new SearchResult();
		searchResult.setRecordCount(results.getNumFound());
		List<SearchItem> list = new ArrayList<SearchItem>();
		for (SolrDocument solrDocument : results) {
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list2 = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = null;
			if (list2 != null && list2.size() > 0) {
				title = list2.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle(title);
			list.add(searchItem);
		}
		searchResult.setItemList(list);
		return searchResult;
	}
}
