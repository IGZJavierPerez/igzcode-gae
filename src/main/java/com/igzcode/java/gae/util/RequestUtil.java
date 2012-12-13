package com.igzcode.java.gae.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility to extract information from a HTTP request.
 */
public class RequestUtil {
    
    /**
     * The base of a request URL.
     * 
     * @param p_request The current request
     * @return The base of a request URL.
     */
	static public String getBaseUri ( HttpServletRequest p_request ) {
        return  p_request.getScheme()      + "://"
                 + p_request.getServerName()   + ":"   + p_request.getServerPort()
                 + p_request.getContextPath()  + "/"
                 ;
	}

}
