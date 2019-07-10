package org.dmytro;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.stereotype.Component;

import se.bostadsportal.domain.dao.AdDAO;
import se.bostadsportal.domain.manager.SearchManager;
import se.monkeys.commons.ejb.MBeanFactory;

public class CountTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private CountType countType;

	@Component
	public enum CountType {
		COUNT_ADS {
			@Override
			public Long count() {
				return searchManager.countAds();
			}
		},
		COUNT_ALL_ADS {
			@Override
			public Long count() {
				return searchManager.countAllAds();
			}
		},
		COUNT_FREE_ADS {
			@Override
			public Long count() {
				return searchManager.countAllAds() - adDAO.countCurfewedAds();
			}
		};

		static final SearchManager searchManager = MBeanFactory.get(SearchManager.class);

		static final AdDAO adDAO = MBeanFactory.get(AdDAO.class);

		public abstract Long count();
	}

	public void setCountType(final String countTypeString) {
		countType = CountType.valueOf(countTypeString);
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			final Long result = countType != null ? countType.count() : Long.valueOf(0L);
			pageContext.getOut().print(result.toString());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
}
