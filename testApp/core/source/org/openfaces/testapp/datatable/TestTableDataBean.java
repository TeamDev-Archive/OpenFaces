/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
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
import java.util.List;
import java.util.Random;

/**
 * @author Dmitry Pikhulya
 */
public class TestTableDataBean {
    private List<TestBean2> collection1;
    private List<TestBean2> collection2;

    public TestTableDataBean() {
        Random random = new Random(5);
        collection1 = new ArrayList<TestBean2>();
        for (int i = 0; i < 10; i++) {
            collection1.add(new TestBean2(randomString(random), randomString(random), randomString(random),
                    random.nextInt(10), random.nextInt(100), random.nextInt(1000), random.nextBoolean()));
        }

        collection2 = new ArrayList<TestBean2>();
        for (int i = 0; i < 30; i++) {
            collection2.add(new TestBean2(randomString(random), randomString(random), randomString(random),
                    random.nextInt(10), random.nextInt(100), random.nextInt(1000), random.nextBoolean()));
        }
    }

    private String randomString(Random random) {
        return Long.toString(random.nextLong(), 36);
    }

    public List<TestBean2> getCollection1() {
        return collection1;
    }

    public List<TestBean2> getCollection2() {
        return collection2;
    }

}
