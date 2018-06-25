package com.awroby.auto.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
/**
 * CORS Filter for testing purpose only. Adjust headers to suit your needs.
 * @author velimir
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {
	
	@Override
	public void init(FilterConfig fc) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpServletRequest request = (HttpServletRequest) req;
		
		//Uncomment to run Websocket test page locally
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		response.setHeader("Access-Control-Max-Age", "1728000");
		response.setHeader("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,"
				+ "Cache-Control,Content-Type,Authorization");
		
		
		/* 
		 * Uncommenting will disable the websocket connection page
		
		response.setHeader("Content-Type", "text/plain");
		response.setHeader("Content-Length", "0");
		response.setHeader("Connection", "keep-alive");

		 */
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		if (httpRequest.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} else {
			chain.doFilter(req, resp);
		}
	}

	@Override
	public void destroy() {
	}
}
