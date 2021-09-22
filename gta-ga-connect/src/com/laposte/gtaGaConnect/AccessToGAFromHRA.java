package com.laposte.gtaGaConnect;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hraccess.nonportlet.HRNonPortletUtils;
import com.hraccess.nonportlet.PortalPageContext;

public class AccessToGAFromHRA  implements Filter {


    public void init(FilterConfig filterconfig)
        throws ServletException
    {
    	


    }
    
    public void destroy()
    {
    }
    
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

    	
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		PortalPageContext portalContext = HRNonPortletUtils.getPortalPageContext(httpRequest);
		if (portalContext == null ) return;

		Cookie[] cookies = httpRequest.getCookies();
		Optional<Cookie> cookieOption = Stream.of(cookies).filter(c -> c.getName().equals("com.hraccess.portal.connection.id")).findAny();
		if (!cookieOption.isPresent()) return;
		
		PrintWriter writer = httpResponse.getWriter();
		writer.write(this.getHtmlResponse(portalContext, cookieOption.get()));
	}

		
		private String getHtmlResponse(PortalPageContext portalContext, Cookie cookie) {
			
		
			String response = "<html>";
			response = response + "<body onload=\"document.createElement('form').submit.call(document.getElementById('submitForm'))\">";
			response = response + "<form id='submitForm' action='https://j2gu2-front-j2g-sdev.paas-02.build.net.intra.laposte.fr/' method='POST' name='submitForm'>";

			response = response + "<input 	id='vsid' "
					+ 	"name='com.hraccess.portal.connection.id' "
					+ 	"type='hidden' "
					+	"value='" + cookie.getValue() +"'/>";
			Map<String, Object> map = portalContext.getAttributes();
			
			
			 for (Map.Entry<String, Object> mapentry : map.entrySet()) {
				 String key = mapentry.getKey();
				 Object value = mapentry.getValue();
				System.out.println(key);	 
				response = response + "<input 	id='" + key + "' "
										+ 	"name='" + key + "' "
										+ 	"type='hidden' "
										+	"value='" + value +"'/>";
				
			};
			
			response = response + "</form>";		

			response = response + "</body>";
			response = response + "</html>";
			
			return response;
		}



}
