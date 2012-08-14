package com.igzcode.java.gae.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility to extract information from a HTTP request.
 */
public class RequestUtil {
    
    static private String _BaseUri;
	
    /**
     * The base of a request URL.
     * 
     * @param p_request The current request
     * @return The base of a request URL.
     */
	static public String GetBaseUri ( HttpServletRequest p_request ) {
	    if ( _BaseUri == null ) {
	        _BaseUri =  p_request.getScheme()      + "://"
	                 + p_request.getServerName()   + ":"   + p_request.getServerPort()
	                 + p_request.getContextPath()  + "/"
	                 ;
	    }
	    return _BaseUri;
	}

}
