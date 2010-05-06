/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.datatable;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Dmitry Pikhulya
 */
public class EmbeddedDatatablesBean {

    private List<TableData> tables;

    public EmbeddedDatatablesBean() {
        tables = new ArrayList<TableData>();
        for (int i = 0; i < 5; i++)
            tables.add(new TableData());
    }

    public List<TableData> getTables() {
        return tables;
    }

    public static class TableData {
        private List<TestBean> records;
        private TestBean selectedRecord;

        public TableData() {
            records = new ArrayList<TestBean>();
            Random random = new Random();
            int recordCount = random.nextInt(10) + 1;
            for (int i = 0; i < recordCount; i++)
                records.add(TestBean.createRandom());
        }

        public List<TestBean> getRecords() {
            return records;
        }

        public TestBean getSelectedRecord() {
            return selectedRecord;
        }

        public void setSelectedRecord(TestBean selectedRecord) {
            this.selectedRecord = selectedRecord;
        }
    }
}
