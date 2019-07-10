package org.dmytro;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

public class PostalCodeTag extends TagSupport {

	private String zipCode;

	public void setValue(final Object object) {
		if (object instanceof String)
			zipCode = StringUtils.trimToEmpty((String) object);
		else
			zipCode = "";
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			for (int i = 0, j = 0, len = zipCode.length(); i < len; ++i) {
				char c = zipCode.charAt(i);
				if (Character.isDigit(c)) {
					pageContext.getOut().print(zipCode.charAt(i));
					if (++j == 3)
						pageContext.getOut().print(' ');
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
}
