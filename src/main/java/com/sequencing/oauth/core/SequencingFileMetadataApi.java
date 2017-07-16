package com.sequencing.oauth.core;

import com.sequencing.oauth.exception.NonAuthorizedException;

/**
 * Interface that defines methods for accessing file metadata stored 
 * for an user account at sequencing.com 
 */
public interface SequencingFileMetadataApi {
	/**
	 * Returns sample files from sequencing.com
	 * @return String json of file content
	 */
	public String getSampleFiles() throws NonAuthorizedException;

	/**
	 * Returns own files from sequencing.com
	 * @return String json of file content
	 */
	public String getOwnFiles() throws NonAuthorizedException;
	
	/**
	 * Returns all files from sequencing.com
	 * @return String json of file content
	 */
	public String getFiles() throws NonAuthorizedException;
}
