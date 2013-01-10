package com.igzcode.java.gae.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ServletResponseWrapper extends HttpServletResponseWrapper {

	private int httpStatus;
	
	public ServletResponseWrapper(HttpServletResponse p_response) {
		super(p_response);
	}

	@Override
    public void sendError(int sc) throws IOException {
        httpStatus = sc;
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        httpStatus = sc;
        super.sendError(sc, msg);
    }

    @Override
    public void setStatus(int sc) {
        httpStatus = sc;
        super.setStatus(sc);
    }

    public int getStatus() {
        return httpStatus;
    }
}
