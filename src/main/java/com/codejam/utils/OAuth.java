package com.codejam.utils;

import java.util.Map;
import java.util.Optional;

import javax.annotation.ManagedBean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@ManagedBean
@PropertySource("classpath:application.properties")
public class OAuth {

	@Value("${oauthURL}")
	private String URI;

	private String tenant;

	@Value("${yaaSClientsIdentifier}")
	private String appId;
	@Value("${docuRepoType}")
	private String type;
	@Value("${yaaSClientsIClient_ID}")
	private String clientId;
	@Value("${yaaSClientsClient_Secret}")
	private String clientSecret;
	@Value("${docuRepoScopes}")
	private String scopes;

	private String grantType = "client_credentials";

	public OAuth() {
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	private String getScopes(String tenant) {
		return "hybris.tenant=" + tenant + " " + scopes;
	}

	private Optional<Map<String, String>> getToken(String tenant) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("content-type", "application/x-www-form-urlencoded");
			RestTemplate restTemplate = new RestTemplate();
			String body = "grant_type=" + grantType + "&client_id=" + clientId + "&client_secret=" + clientSecret
					+ "&scope=" + getScopes(tenant);

			HttpEntity<Object> request = new HttpEntity<>(body, headers);
			Map<String, String> tokenMap = restTemplate.postForObject(URI, request, Map.class);
			return Optional.of(tokenMap);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public String getAuthToken(String tenant) {
		Map<String, String> tokenMap = getToken(tenant).orElse(null);
		if (tokenMap == null) {
			throw new RuntimeException();
		}
		return "Bearer " + tokenMap.get("access_token");
	}

}
