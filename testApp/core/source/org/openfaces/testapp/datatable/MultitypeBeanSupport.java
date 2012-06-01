/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.datatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class MultitypeBeanSupport {
    private List<MultitypeBean> list1;

    public MultitypeBeanSupport() {
        list1 = createList1();
    }

    private List<MultitypeBean> createList1() {
        List<MultitypeBean> list = new ArrayList<MultitypeBean>();
        for (int i = 0; i < 30; i++) {
            list.add(new MultitypeBean());
        }
        return list;
    }

    public List<MultitypeBean> getList1() {
        return list1;
    }

    public List<String> getCustomFieldNames() {
        String[] suffixes = new String[]{"0", "1", "2"};
        String[] prefixes = new String[]{"int", "double", "string", "date"};
        List<String> result = new ArrayList<String>();
        for (String prefix : prefixes) {
            for (String suffix : suffixes) {
                result.add(prefix + suffix);
            }
        }
        return result;
    }
}
