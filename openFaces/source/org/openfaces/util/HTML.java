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
package org.openfaces.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class HTML {

    private HTML() {
        // disable instantiation
    }

    //HTML entities
    public static final String NBSP_ENTITY = "&#160;";
    public static final String LAQUO_ENTITY = "&#171;";
    public static final String RAQUO_ENTITY = "&#187;";

    // TODO [sanders] (Apr 1, 2009, 6:33 AM): Transform to enum with code
    public static final Map<String, Integer> HTML_SPECIFIC_ENTITIES_TO_CODES = new HashMap<String, Integer>();

    static {
        String[] entities = new String[]{
                "nbsp",
                "iexcl",
                "cent",
                "pound",
                "curren",
                "yen",
                "brvbar",
                "sect",
                "uml",
                "copy",
                "ordf",
                "laquo",
                "not",
                "shy",
                "reg",
                "macr",
                "deg",
                "plusmn",
                "sup2",
                "sup3",
                "acute",
                "micro",
                "para",
                "middot",
                "cedil",
                "sup1",
                "ordm",
                "raquo",
                "frac14",
                "frac12",
                "frac34",
                "iquest",
                "Agrave",
                "Aacute",
                "Acirc",
                "Atilde",
                "Auml",
                "Aring",
                "AElig",
                "Ccedil",
                "Egrave",
                "Eacute",
                "Ecirc",
                "Euml",
                "Igrave",
                "Iacute",
                "Icirc",
                "Iuml",
                "ETH",
                "Ntilde",
                "Ograve",
                "Oacute",
                "Ocirc",
                "Otilde",
                "Ouml",
                "times",
                "Oslash",
                "Ugrave",
                "Uacute",
                "Ucirc",
                "Uuml",
                "Yacute",
                "THORN",
                "szlig",
                "agrave",
                "aacute",
                "acirc",
                "atilde",
                "auml",
                "aring",
                "aelig",
                "ccedil",
                "egrave",
                "eacute",
                "ecirc",
                "euml",
                "igrave",
                "iacute",
                "icirc",
                "iuml",
                "eth",
                "ntilde",
                "ograve",
                "oacute",
                "ocirc",
                "otilde",
                "ouml",
                "divide",
                "oslash",
                "ugrave",
                "uacute",
                "ucirc",
                "uuml",
                "yacute",
                "thorn",
                "yuml"};
        for (int i = 0; i < entities.length; i++) {
            String entity = entities[i];
            int numericEntityCode = 160 + i;
            HTML_SPECIFIC_ENTITIES_TO_CODES.put(entity, numericEntityCode);
        }
        HTML_SPECIFIC_ENTITIES_TO_CODES.put("euro", 8364);
        HTML_SPECIFIC_ENTITIES_TO_CODES.put("quot", 34);
        HTML_SPECIFIC_ENTITIES_TO_CODES.put("amp", 38);
        HTML_SPECIFIC_ENTITIES_TO_CODES.put("lt", 60);
        HTML_SPECIFIC_ENTITIES_TO_CODES.put("gt", 62);
    }


    public static final String NBSP_CHAR = "\u00a0";

}


