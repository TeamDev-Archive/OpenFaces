/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.support.QKS110;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Tatyana Matveyeva
 */
public class ResultSetBean {


    public ResultSetBean() throws SQLException {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot load hsqldb driver", e);
        }
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");

        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE Personas " +
                "(" +
                "LastName varchar," +
                "FirstName varchar," +
                "Age int" +
                ");");
        stmt.execute("insert into Personas values ('Name1', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name2', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name3', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name4', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name5', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name6', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name7', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name8', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name9', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name10', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name11', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name12', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name13', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name14', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name15', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name16', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name17', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name18', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name19', 'FirstName', 30);");
        stmt.execute("insert into Personas values ('Name20', 'FirstName', 30);");

        stmt.close();
    }

    public ResultSet getPeopleList() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs;
        rs = stmt.executeQuery(
                "SELECT * FROM Personas");
        return rs;
    }
}
