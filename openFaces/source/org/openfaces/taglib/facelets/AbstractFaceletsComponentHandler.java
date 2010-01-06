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
package org.openfaces.taglib.facelets;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.MetaRuleset;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentHandler;
import org.openfaces.taglib.TagUtil;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractFaceletsComponentHandler extends ComponentHandler {
    private PropertyHandlerMetaRule metaRule;

    public AbstractFaceletsComponentHandler(com.sun.facelets.tag.jsf.ComponentConfig componentConfig, AbstractComponentTag tag) {
        super(componentConfig);
        metaRule = new PropertyHandlerMetaRule(tag);
    }

    protected MetaRuleset createMetaRuleset(Class aClass) {
        MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
        metaRuleset.addRule(metaRule);
        return metaRuleset;
    }


    protected void setAttributes(FaceletContext faceletContext, Object object) {
        super.setAttributes(faceletContext, object);
        UIComponent component = (UIComponent) object;
        AbstractComponentTag tag = (AbstractComponentTag) metaRule.getTag();
        FacesContext facesContext = faceletContext.getFacesContext();
        tag.setFacesContext(facesContext);
        tag.setExpressionCreator(new FaceletsExpressionCreator(faceletContext) {
            protected TagAttribute getAttribute(String attributeName) {
                return AbstractFaceletsComponentHandler.this.getAttribute(attributeName);
            }
        });
        try {
            tag.setComponentProperties(facesContext, component);
        } finally {
            tag.setFacesContext(null);
        }
    }

    protected void onComponentCreated(FaceletContext faceletContext, UIComponent component, UIComponent parentComponent) {
        super.onComponentCreated(faceletContext, component, parentComponent);
        FacesContext facesContext = faceletContext.getFacesContext();
        TagUtil.initComponentChildren(facesContext, component);
    }

}
