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
package org.openfaces.testapp.tomahawk;

import java.io.Serializable;

/**
 * <p>
 * Bean class holding a tree item.
 * </p>
 *
 * @author <a href="mailto:dlestrat@apache.org">David Le Strat</a>
 */
public class TreeItem implements Serializable {
    /**
     * serial id for serialisation versioning
     */
    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String isoCode;

    private String description;


    /**
     * @param id          The id.
     * @param name        The name.
     * @param isoCode     The isoCode.
     * @param description The description.
     */
    public TreeItem(int id, String name, String isoCode, String description) {
        this.id = id;
        this.name = name;
        this.isoCode = isoCode;
        this.description = description;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Returns the isoCode.
     */
    public String getIsoCode() {
        return isoCode;
    }

    /**
     * @param isoCode The isoCode to set.
     */
    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
