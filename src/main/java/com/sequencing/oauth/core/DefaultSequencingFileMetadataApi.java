package com.sequencing.oauth.core;

import com.sequencing.oauth.exception.NonAuthorizedException;
import com.sequencing.oauth.helper.HttpHelper;

/**
 * Default implementation of SequencingFileMetadataApi interface
 */
public class DefaultSequencingFileMetadataApi implements SequencingFileMetadataApi
{
	private SequencingOAuth2Client client;

	public DefaultSequencingFileMetadataApi(SequencingOAuth2Client client) {
		this.client = client;
	}
	
	@Override
	public String getSampleFiles() throws NonAuthorizedException
	{
        return getFilesByType("sample");
    }
	
	@Override
	public String getOwnFiles() throws NonAuthorizedException
	{
        return getFilesByType("uploaded");
    }
	
	@Override
	public String getFiles() throws NonAuthorizedException 
	{
		return getFilesByType("all");
	}
	
	/**
	 * Returns files depending on file type
	 */
	private String getFilesByType(String fileType) throws NonAuthorizedException
	{
		if (client.isAuthorized() == false) {
            throw new NonAuthorizedException();
        }
		
        String uri = String.format("%s/DataSourceList?%s=true", 
        		client.getAuthenticationParameters().getApiUri(), fileType);
        
        return HttpHelper.doOauthSecureGet(uri, client.getToken());
	}
}
