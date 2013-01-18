/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.filter;

import org.openfaces.demo.services.RuleItem;
import org.openfaces.demo.services.Rules;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LiveDemoFilter implements Filter {
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getMethod().equalsIgnoreCase("get")) {
            String requestURI = request.getRequestURI();
            List<RuleItem> rules = Rules.getRules();
            for (RuleItem ruleItem : rules) {
                if (requestURI.endsWith(ruleItem.getShortPath())) {
                    ((HttpServletResponse) servletResponse).sendRedirect(request.getContextPath() + ruleItem.getUrl());
                    return;
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        Rules.buildSiteRules(filterConfig.getServletContext());
    }

    public void destroy() {
    }
}
