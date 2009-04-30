/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.support.QKS340;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class DemoServlet extends HttpServlet {

    Logger logger = Logger.getLogger(DemoServlet.class.getName());

    /**
     * Called once at startup
     */
    public void init() throws ServletException {
        logger.info("Init() the Servlet");
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String scope = request.getParameter("SCOPE");
        if (scope == null) {
            getServletConfig().getServletContext().getRequestDispatcher("/demo/support/QKS340/main.jsp").forward(request, response);
            return;
        }
        if (scope.equals("5")) {
            getServletConfig().getServletContext().getRequestDispatcher("/demo/support/QKS340/dataGrid.jsp").forward(request, response);
        } else {
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.write("Scope not recognized: " + scope);
            writer.flush();
            writer.close();
        }
    }
}