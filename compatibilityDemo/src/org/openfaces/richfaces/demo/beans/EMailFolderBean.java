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
package org.openfaces.richfaces.demo.beans;

public final class EMailFolderBean {
	
	public static final String ROOT = "root";
	public static final String SUBNODE = "subnode";
	
	private final String name;
	private final boolean subfolder;
	private final EMailFolderBean[] children;
	private final EMailsTreeTableBean content;
	private final boolean incoming;
	
	EMailFolderBean(String name, boolean subfolder, boolean incoming) {
		this(name, subfolder, null, incoming);
	}
	
	EMailFolderBean(String name, boolean subfolder, EMailFolderBean[] children, boolean incoming) {
		this.name = name;
		this.subfolder = subfolder;
		this.children = children;
		this.content = new EMailsTreeTableBean(name);
		this.incoming = incoming;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isSubfolder() {
		return subfolder;
	}
	
	public EMailFolderBean[] getChildren() {
		return children;
	}
	
	public EMailsTreeTableBean getContent() {
		return content;
	}
	
	public boolean isIncoming() {
		return incoming;
	}
	
	public String toString() {
		return name;
	}
}
