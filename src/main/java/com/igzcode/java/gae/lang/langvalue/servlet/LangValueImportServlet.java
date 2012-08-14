package com.igzcode.java.gae.lang.langvalue.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.igzcode.java.gae.serialization.DataTableList;
import com.igzcode.java.gae.util.LocaleUtil;

public class LangValueImportServlet extends HttpServlet {
	
	private static final long serialVersionUID = -4821819593525176166L;
	
	static private BlobstoreService _BlobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	/**
	 * Servlet to import a uploaded CSV file with the format: separator=; and quote_char="
	 * 
	 * File example:
	 * "KEY1";"VALUE1"
	 * "KEY2";"VALUE2"
	 * ...
	 */
	public void doPost ( HttpServletRequest p_request, HttpServletResponse p_response ) throws ServletException, IOException {
		DataTableList dtl = new DataTableList();
		
		Map<String, List<BlobKey>> blobs = _BlobstoreService.getUploads(p_request);
		BlobKey blobKey = blobs.get("lang-file-csv").get(0);
		if ( blobKey != null ) {
			BlobstoreInputStream blobstoreInputStream = new BlobstoreInputStream(blobKey);
			InputStreamReader inputStreamReader = new InputStreamReader(blobstoreInputStream, "UTF-8");
			LocaleUtil.ImportContents( inputStreamReader );
			dtl.AddVar("OK", "import completed");
		}
		else {
			dtl.AddVar("KO", "blob key not found");
		}
		
		p_response.getWriter().println( dtl.ToString() );
    }
}