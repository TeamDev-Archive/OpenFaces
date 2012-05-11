/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.social;

import org.openfaces.component.social.DisqusComments;
import org.openfaces.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class DisqusCommentsRenderer extends RendererBase {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);

        DisqusComments dc = (DisqusComments) component;

        String disqusShortname = "localhost-20101209";
        String disqusIdentifier = null;//"thread01309234";//null;
        String disqusUrl = null;//"http://localhost:8080/LiveDemoFacelets/datatable/DataTable_general.jsf";//null;
        boolean disqusDeveloper = true;

        ResponseWriter writer = context.getResponseWriter();

        writer.write("<div id=\"disqus_thread\"></div>\n" +
                "<script type=\"text/javascript\">\n" +
                "    /* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */\n" +
                "    var disqus_shortname = '" + disqusShortname + "'; // required: replace example with your forum shortname\n" +
                "\n" +
                "    // The following are highly recommended additional parameters. Remove the slashes in front to use.\n" +
                "     var disqus_identifier = 'unique_dynamic_id_1234lkjdeflkjsd';\n" +
                "     var disqus_developer = " + (disqusDeveloper ? 1 : 0) + ";\n" +

                "    // var disqus_url = 'http://example.com/permalink-to-page.html';\n" +
                "\n" +
                "    /* * * DON'T EDIT BELOW THIS LINE * * */\n" +
                "    (function() {\n" +
                "        var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;\n" +
                "        dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';\n" +
                "        (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);\n" +
                "    })();\n" +
                "</script>\n" +
                "<noscript>Please enable JavaScript to view the <a href=\"http://disqus.com/?ref_noscript\">comments powered by Disqus.</a></noscript>\n" +
                "<a href=\"http://disqus.com\" class=\"dsq-brlink\">blog comments powered by <span class=\"logo-disqus\">Disqus</span></a>");

//        writer.startElement("div", dc);
//        writer.writeAttribute("id", "disqus_thread", null);//component.getClientId(context), null);
//        writer.endElement("div");
//        writer.startElement("script", dc);
//        writer.writeAttribute("type", "text/javascript", null);
//        writer.write("var disqus_shortname='" + disqusShortname + "';");
//        if (disqusIdentifier != null)
//            writer.write("var disqus_identifier='" + disqusIdentifier + "';");
//        if (disqusUrl != null)
//            writer.write("var disqus_url='" + disqusUrl + "';");
//        if (disqusDeveloper)
//            writer.write("var disqus_developer=" + (disqusDeveloper ? 1 : 0) + ";");
//        writer.write("(function() {\n" +
//                "        var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;\n" +
//                "        dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';\n" +
//                "        (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);\n" +
//                "    })();");
//        writer.endElement("script");
//        writer.write("<noscript>Please enable JavaScript to view the <a href=\"http://disqus.com/?ref_noscript\">comments powered by Disqus.</a></noscript>\n" +
//                "<a href=\"http://disqus.com\" class=\"dsq-brlink\">blog comments powered by <span class=\"logo-disqus\">Disqus</span></a>");
    }
}
