package com.codejam.utils;

import java.util.Map;

import javax.annotation.ManagedBean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.codejam.model.Comment;
import com.google.gson.Gson;

@ManagedBean
@PropertySource("classpath:application.properties")
public class DocumentRepo {

	@Value("${docuRepoURL}")
	private String URI;
	@Value("${yaaSClientsIdentifier}")
	private String appId;
	@Value("${docuRepoType}")
	private String type;

	public DocumentRepo() {
	}

	private HttpHeaders headerWithToken(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.add("Authorization", token);
		return headers;
	}

	private String getUrl(String tenant, String appId, String type) {
		return URI + "/" + tenant + "/" + appId + "/data/" + type;
	}

	private String toJson(Object o) {
		return new Gson().toJson(o);
	}

	public Comment[] getAll(String token, String tenant, String productId) {
		try {
			HttpEntity<String> request = new HttpEntity<>(headerWithToken(token));
			String url = getUrl(tenant, appId, productId);
			Comment[] objects = new RestTemplate().exchange(url, HttpMethod.GET, request, Comment[].class).getBody();
			return objects;
		} catch (RestClientException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public String post(Object o, String tenant, String token, String productId) {
		try {

			String url = getUrl(tenant, appId, productId);

			HttpEntity<String> request = new HttpEntity<>(toJson(o), headerWithToken(token));
			Map<String, String> map = new RestTemplate().postForObject(url, request, Map.class);
			if (map != null && map.containsKey("link")) {
				return map.get("link");
			}
		} catch (RestClientException e) {
			e.printStackTrace();
			if (e.getMessage().contains("Conflict")) {
				return "Conflict";
			}
		}
		throw new RuntimeException();
	}
	
	public HttpStatus delete(String id, String tenant, String token, String productId) {
        try {

			String url = getUrl(tenant, appId, productId) + "/" + id;

            HttpEntity<String> request = new HttpEntity<>(headerWithToken(token));
            Object o = new RestTemplate().exchange(url, HttpMethod.DELETE, request, Object.class).getBody();
            return HttpStatus.OK;
        } catch (RestClientException e) {
            e.printStackTrace();
            return HttpStatus.NOT_MODIFIED;
        }
    }
}
