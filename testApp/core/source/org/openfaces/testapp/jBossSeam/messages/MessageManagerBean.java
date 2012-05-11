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

//$Id: MessageManagerBean.java,v 1.7 2006/11/19 21:01:33 gavin Exp $
package org.openfaces.testapp.jBossSeam.messages;

import static org.jboss.seam.ScopeType.SESSION;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.openfaces.util.Faces;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static javax.persistence.PersistenceContextType.EXTENDED;
import java.io.Serializable;
import java.util.List;

@Stateful
@Scope(SESSION)
@Name("messageManager")
public class MessageManagerBean implements Serializable, MessageManager {

    @DataModel
    private List<Message> messageList;

    @DataModelSelection
    @Out(required = false)
    private Message seammessage;

    @PersistenceContext(type = EXTENDED)
    private EntityManager em;

    @Factory("messageList")
    public void findMessages() {
        messageList = em.createQuery("select msg from Message msg order by msg.datetime desc").getResultList();
    }

    public void select() {
        Message currentMessage = Faces.var("msg", Message.class);
        if (currentMessage != null) currentMessage.setRead(true);
    }

    public void delete() {
        Message currentMessage = Faces.var("msg", Message.class);
        if (currentMessage != null) {
            messageList.remove(currentMessage);
            em.refresh(currentMessage);
            currentMessage = null;
        }
    }

    @Remove
    @Destroy
    public void destroy() {
    }

}
