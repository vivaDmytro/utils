package org.dmytro;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class DisableJSessionIdFilter implements Filter {

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
			throws IOException, ServletException {
		if (!(servletRequest instanceof HttpServletRequest)) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

		if (httpServletRequest.isRequestedSessionIdFromURL()) {
			String url = httpServletRequest.getRequestURL()
					.append(httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "").toString();
			httpServletResponse.setHeader("Location", encode(url));
			httpServletResponse.sendError(HttpServletResponse.SC_MOVED_PERMANENTLY);
			return;
		}

		filterChain.doFilter(servletRequest, wrappedResponse(httpServletResponse));
	}

	private String encode(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "ENCODINGERROR"; // what shall we do?
		}
	}

	private HttpServletResponseWrapper wrappedResponse(HttpServletResponse httpServletResponse) {
		HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(httpServletResponse) {
			@Override
			public String encodeRedirectUrl(final String url) {
				return url;
			}

			@Override
			public String encodeRedirectURL(final String url) {
				return url;
			}

			@Override
			public String encodeUrl(final String url) {
				return url;
			}

			@Override
			public String encodeURL(final String url) {
				return url;
			}
		};
		return wrappedResponse;
	}

	@Override
	public void init(final FilterConfig pConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
