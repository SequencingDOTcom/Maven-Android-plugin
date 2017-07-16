package com.sequencing.oauth.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequencing.oauth.config.AuthenticationParameters;
import com.sequencing.oauth.exception.BasicAuthenticationFailedException;
import com.sequencing.oauth.helper.HttpHelper;
import com.sequencing.oauth.helper.JsonHelper;

/**
 * Default implementation of SequencingOAuth2Client interface
 */
public class DefaultSequencingOAuth2Client implements SequencingOAuth2Client, Serializable
{
	private static final long serialVersionUID = -1085956585099205527L;
	private AuthenticationParameters parameters;
	private volatile Token token;
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(DefaultSequencingOAuth2Client.class);
	
	/**
	 * Attribute for value of redirect url 
	 */
	private static final String ATTR_REDIRECT_URL = "redirect_uri";
	
	/**
	 * Attribute for value of response type
	 */
	private static final String ATTR_RESPONSE_TYPE = "response_type";
	
	/**
	 * Attribute for value state
	 */
	private static final String ATTR_STATE = "state";
	
	/**
	 * Attribute for value client id
	 */
	private static final String ATTR_CLIENT_ID = "client_id";
	
	/**
	 * Attribute for value scope
	 */
	private static final String ATTR_SCOPE = "scope";
	
	/**
	 * Attribute for value code
	 */
	private static final String ATTR_CODE = "code";
	
	/**
	 * Attribute for value refresh token
	 */
	private static final String ATTR_REFRESH_TOKEN = "refresh_token";
	
	/**
	 * Attribute for access token
	 */
	private static final String ATTR_ACCESS_TOKEN = "access_token";
	
	/**
	 * Attribute for value grant type
	 */
	private static final String ATTR_GRANT_TYPE = "grant_type";
	
	/**
	 * Attribute for value expires in
	 */
	private static final String ATTR_EXPRIES_IN = "expires_in";
	
	/**
	 * Attribute for value mobile mode
	 */
	private static final String ATTR_MOBILE_MODE = "mobile";
	
	public DefaultSequencingOAuth2Client(AuthenticationParameters parameters){
		this.parameters = parameters;
	}
	
	@Override
	public Map<String, String> getHttpParametersForRedirect() {
		Map<String, String> attribures = new HashMap<String, String>(5);
		attribures.put(ATTR_REDIRECT_URL, parameters.getRedirectUri());
		attribures.put(ATTR_RESPONSE_TYPE, parameters.getResponseType());
		attribures.put(ATTR_STATE, parameters.getState());
		attribures.put(ATTR_CLIENT_ID, parameters.getClientId());
		attribures.put(ATTR_SCOPE, parameters.getScope());
		attribures.put(ATTR_MOBILE_MODE, parameters.getMobileMode());
		return attribures;
	}
	
	@Override
	public String getLoginRedirectUrl() {
		return String.format("%s?%s", parameters.getOAuthAuthorizationUri(), getAttributesForRedirectAsString());
	}

	@Override
	public Token authorize(String responseCode, String responseState) throws IllegalStateException, BasicAuthenticationFailedException
	{
		if (responseState.equals(parameters.getState()) == false)
			throw new IllegalStateException("Invalid state parameter");
			
		// You are to save these 2 tokens somewhere in a permanent storage, such as
        // database. When access token expires, you will be able to use refresh
        // token to fetch a new access token without need of re-authorization by
        // user.
			
		Map<String, String> params = new HashMap<String, String>();
		params.put(ATTR_GRANT_TYPE,   parameters.getGrantType());
		params.put(ATTR_CODE,		  responseCode);
		params.put(ATTR_REDIRECT_URL, parameters.getRedirectUri());

		String uri = parameters.getOAuthTokenUri();
		String result = HttpHelper.doBasicSecurePost(uri, parameters, params);

		if (result == null) {
			throw new BasicAuthenticationFailedException("Failure authentication");
		}
			
		String accessToken = JsonHelper.getField(result, ATTR_ACCESS_TOKEN);
		String refreshToken = JsonHelper.getField(result, ATTR_REFRESH_TOKEN);
		long timelife = Long.parseLong(JsonHelper.getField(result, ATTR_EXPRIES_IN));
			
		token = new Token(accessToken, refreshToken, timelife, new Date());
		
		return token;
	}
	
	@Override
	public boolean isAuthorized()
	{
		return token != null && token.getLifeTime() != 0;
	}
	
	@Override
	public AuthenticationParameters getAuthenticationParameters() {
		return parameters;
	}

	@Override
	public Token getToken() {
		if(System.currentTimeMillis() >= (token.getLastRefreshDate().getTime() + (token.getLifeTime() * 1000) - 30000) && isAuthorized()){
			try {
				refreshToken();
			} catch (BasicAuthenticationFailedException e) {
				log.debug("Error occured during refresh token", e.getMessage());
			}
		}
		
		return token;
	}

	protected void refreshToken() throws BasicAuthenticationFailedException
	{
		log.debug("Going to refresh OAuth token");
		
		Map<String, String> params = new HashMap<String, String>(2);
		params.put(ATTR_GRANT_TYPE, parameters.getGrantTypeRefreshToken());
		params.put(ATTR_REFRESH_TOKEN, token.getRefreshToken());

		String uri = parameters.getOAuthTokenUri();
		String result = HttpHelper.doBasicSecurePost(uri, parameters, params);

		if (result == null) {
			throw new BasicAuthenticationFailedException("Authentication against backend failed. "
					+ "Server replied with: " + result);
		}
		
		String accessToken = JsonHelper.getField(result, ATTR_ACCESS_TOKEN);
		long timelife = Long.parseLong(JsonHelper.getField(result, ATTR_EXPRIES_IN));
		
		token = new Token(accessToken, token.getRefreshToken(), timelife, new Date());
		log.info("Token has been refreshed. New token value " + token.getAccessToken());
	}
	
	private List<String> getAttributesForRedirectAsList() {
		Map<String, String> attributes = getHttpParametersForRedirect();
		List<String> result = new ArrayList<String>(attributes.size());
		
		for (Entry<String, String> e : attributes.entrySet())
			result.add(String.format("%s=%s", e.getKey(), e.getValue()));
		
		return result;
	}
	
	private String getAttributesForRedirectAsString(){
		List<String> listOfAttributes = getAttributesForRedirectAsList();
		StringBuilder builder = new StringBuilder(listOfAttributes.get(0));
		for(int i = 1; i < listOfAttributes.size(); i++)
		{
			builder.append("&");
			builder.append(listOfAttributes.get(i));
		}
		return builder.toString();
	}
}
