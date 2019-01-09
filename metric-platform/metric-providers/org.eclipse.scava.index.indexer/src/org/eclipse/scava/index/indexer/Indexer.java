package org.eclipse.scava.index.indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Indexer {

	private static RestHighLevelClient highLevelClient;
	private static Client adminClient;

	static {
		IndexerSingleton singleton = IndexerSingleton.getInstance();
		highLevelClient = singleton.getHighLevelclient();
		adminClient = singleton.getAdminClient();
	}

	/**
	 * Retrieves all non-default indices from Elasticsearch  
	 * 
	 * @return List<String>
	 */
	private static List<String> getIndices() {
		List<String> indicesList = new ArrayList<String>();
		ImmutableOpenMap<String, IndexMetaData> indices = adminClient.admin().cluster().prepareState().get().getState()
				.getMetaData().getIndices();
		for (ObjectObjectCursor<String, IndexMetaData> x : indices) {
			if (!(x.value == null)) {
				if (!(x.value.getIndex().getName().startsWith("."))) {
					if (!(indicesList.contains(x.value.getIndex().getName()))) {
						indicesList.add(x.value.getIndex().getName());
					}
				}
			}
		}
		return indicesList;
	} 

	/**
	 * Adds settings to index
	 * 
	 * @param index
	 *            - name of the index
	 * @param setting
	 *            - settings represented as a JSON String
	 * @throws IOException
	 */
	private static void addIndexSetting(String index, String setting) throws IOException {
		UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest();
		updateSettingsRequest.indices(index);
		updateSettingsRequest.settings(setting, XContentType.JSON);
		UpdateSettingsResponse updateSettingsResponse = highLevelClient.indices().putSettings(updateSettingsRequest,
				getWriteHeaders());
		if (updateSettingsResponse.isAcknowledged() == true) {
			System.out.println("[ELASTICSEARCH MANAGER] \tSettings have been added to " + index);
		}
	}

	/**
	 * Creates a new index
	 * 
	 * @param indexName
	 *            - name of the new index
	 */
	private static void createIndex(String indexName) {

		CreateIndexRequest request = new CreateIndexRequest(indexName);
		CreateIndexResponse createIndexResponse;
		try {
			createIndexResponse = highLevelClient.indices().create(request, getWriteHeaders());
			if (createIndexResponse.isAcknowledged() == true) {
				System.err.println("\t -Index " + indexName + " has been created");
			}
		} catch (ElasticsearchStatusException e) {
			System.err.println("\t-" + indexName + "\tIndexResponse :" + e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a document map (mapping) to a specific index
	 * 
	 * @param index
	 *            - index name
	 * @param type
	 *            - doucment type
	 * @param mapping
	 *            - a JSON document represented as a string
	 */
	private static void addMappingToIndex(String indexName, String documentType, String mapping) {

		PutMappingRequest putMappingRequest = new PutMappingRequest(indexName.toLowerCase());
		putMappingRequest.source(mapping, XContentType.JSON).type(documentType.toLowerCase());
		PutMappingResponse putMappingResponse;
		try {
			putMappingResponse = highLevelClient.indices().putMapping(putMappingRequest, getWriteHeaders());
			if (putMappingResponse.isAcknowledged() == true) {
				System.out.println(
						"[ELASTICSEARCH MANAGER] \tMapping for " + documentType + " in " + indexName + " was added");
			}
		} catch (IOException e) {
			//Do Nothing
		}
	}

	/**
	 * Returns a basic HTTP write headers
	 * 
	 * 
	 * @return headers
	 */
	private static Header[] getWriteHeaders() {

		Header[] headers = { new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
				new BasicHeader("Role", "Write") };
		return headers;
	}

	/**
	 * Returns a basic HTTP read headers
	 * 
	 * 
	 * @return headers
	 */
	private static Header[] getReadHeaders() {

		Header[] headers = { new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
				new BasicHeader("Role", "Read") };
		return headers;
	}
	
	/**
	 * @param reposiotry - name of the repository. For example github, mantis
	 * @param documentType - document type. For example article, bug, post.
	 * @param knowledgeType - type of knowledge. for example nlp
	 * @return indexName - generated name for the index
	 */
	public static String generateIndexName(String reposiotry, String documentType, String knowledgeType) {
		
		reposiotry = reposiotry.replaceAll(" ", "").trim().toLowerCase();
		documentType = documentType.replaceAll(" ", "").trim().toLowerCase();
		knowledgeType = knowledgeType.replace(" ", "").trim().toLowerCase();
		
		return reposiotry + "." + documentType + "." + knowledgeType;
	}



	/**
	 * Use this method when the dump that you are using already contains unique
	 * identifiers for each document. For example a 'customerId' or 'postID'. This
	 * will prevent duplicate documents being added to the index. Essentially, if
	 * the same ID already exists in the index, Elasticsearch will simply update the
	 * document with the 'new' changes.
	 * 
	 * @param indexName
	 *            - index name
	 * @param documentType
	 *            - document type
	 * @param uid
	 *            - unique identifier
	 * @param object
	 *            - object that represents the structure of a document for indexing
	 * @return
	 * @return IndexResponse
	 * @throws IOException
	 */
	private static void indexDocument(String indexName, String documentType, String uid, Object document)
			throws IOException {

		String source = objectToJson(document);
		IndexRequest indexRequest = new IndexRequest();
		indexRequest.index(indexName.toLowerCase()).type(documentType.toLowerCase()).id(uid).source(source, XContentType.JSON);
		System.err.println("\t -Document: " + documentType + ": "+ uid + " has been " + highLevelClient.index(indexRequest, getWriteHeaders()).getResult());

	}



	/**
	 * Closes the HighLevel Rest Client's connection to the Elasticsearch index
	 * 
	 * @throws IOException
	 */
	private static void closeHighLevelClient() throws IOException {

		highLevelClient.close();
		System.out.println("[ELASTICSEARCH MANAGER] \tHight Level Client - Closed");
	}

	/**
	 * Closes the Admin Client
	 */
	private static void closeAdminClient() {

		adminClient.close();
		System.out.println("[ELASTICSEARCH MANAGER] \tAdmin Client - Closed");
	}

	/**
	 * Closes all clients (HighLevelRest and AdminClients)
	 * 
	 * @throws IOException
	 */

	private static void closeAllClients() throws IOException {
		
		closeHighLevelClient();
		closeAdminClient();
	}

	/**
	 * This method maps a objects attributes and returns a string in a JSON
	 * structure.
	 * 
	 * @param object
	 * @return string
	 * @throws com.fasterxml.jackson.core.JsonProcessingException
	 */

	private static String objectToJson(Object object) throws com.fasterxml.jackson.core.JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(object);
		return jsonString;
	}

	/**
	 * This method is used to create / check the existence of an index, add mappings
	 * to an index and index or update a document related to bug tracking systems.
	 * 
	 * @param indexName
	 * @param mapping
	 * @param documentType
	 * @param uid
	 * @param document
	 */
	
	
	public static void performIndexing(String indexName, String mapping, String documentType, String uid,
			Object document) {
		
		System.err.println("[Performing Indexing] -----------------------------------------------------------");
		if (!(getIndices().contains(indexName))) {
			createIndex(indexName);
			//String mapping = getMapping(indexName);
			if(!(mapping.isEmpty())) {
			addMappingToIndex(indexName, documentType, mapping);
			}
			try {
				indexDocument(indexName, documentType, uid, document);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				indexDocument(indexName, documentType, uid, document);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------

		// TODO THE Methods below from production Used only for testing

	// ------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------

	
	
	
	
//	/**
//	 * Method for searching the index
//	 * 
//	 * @param index
//	 *            - index name
//	 * @param type
//	 *            - document type
//	 * @param queryBuilder
//	 *            - a query built using a query builder
//	 * @return response
//	 * @throws IOException
//	 */
//	public static SearchResponse searchIndex(String index, String[] type, QueryBuilder queryBuilder)
//			throws IOException {
//
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		searchSourceBuilder.query(queryBuilder);
//		// search request
//		SearchRequest searchRequest = new SearchRequest(index);
//		searchRequest.source(searchSourceBuilder).types(type);
//		SearchResponse searchResponse = highLevelClient.search(searchRequest, getReadHeaders());
//		return searchResponse;
//	}
	
	
	
	
//	/**
//	 * Not yet Implemented
//	 * 
//	 * @param index
//	 * @param doc
//	 * @param doc_id
//	 * @return
//	 */
//	@SuppressWarnings("unused")
//	public static GetRequest prepareGetRequest(String index, String doc, String doc_id) {
//		
//		GetRequest request = new GetRequest(index, doc, doc_id);
//		return null;
//	}
	
	
//	public void printHits(SearchResponse sr, QueryBuilder query) {
//
//		for (SearchHit hit : sr.getHits()) {
//			System.out.println("[" + query.getName() + " query] Response = " + hit.getSourceAsString());
//		}
//	}
//
//	/**
//	 * Remove in production used for debugging
//	 * 
//	 * @param sr
//	 * @param query
//	 */
//	public void deleteIndex(String index) {
//
//		DeleteIndexRequest request = new DeleteIndexRequest(index.toLowerCase());
//		try {
//			DeleteIndexResponse deleteIndexResponse = highLevelClient.indices().delete(request, getWriteHeaders());
//			if (deleteIndexResponse.isAcknowledged()) {
//				System.out.println("[ELASTICSEARCH MANAGER] \tIndex: " + index + "has been deleted");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
