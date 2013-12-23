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
package org.openfaces.taglib.facelets;

import org.openfaces.component.LazyAttributesProcessorEnd;
import org.openfaces.component.LazyAttributesProcessorStart;
import org.openfaces.component.LazyProcessSupport;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagAttribute;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractFaceletsComponentHandler extends ComponentHandler {
    private PropertyHandlerMetaRule metaRule;

    public AbstractFaceletsComponentHandler(ComponentConfig componentConfig, AbstractComponentTag tag) {
        super(componentConfig);
        metaRule = new PropertyHandlerMetaRule(tag);
    }

    protected MetaRuleset createMetaRuleset(Class aClass) {
        MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
        metaRuleset.addRule(metaRule);
        return metaRuleset;
    }

    private static int lazyProcessorsCount =0;


    public void setAttributes(FaceletContext faceletContext, Object object) {
        super.setAttributes(faceletContext, object);
        UIComponent component = (UIComponent) object;

        if (component instanceof LazyAttributesProcessorStart){
            lazyProcessorsCount++;
        }

        if (component instanceof LazyAttributesProcessorEnd){
            lazyProcessorsCount--;
        }

        if (lazyProcessorsCount != 0 && component instanceof LazyProcessSupport){
            LazyProcessSupport lazyProcessSupport = (LazyProcessSupport) component;
            lazyProcessSupport.setLazyProcessAttributes(new FaceletsExpressionCreator(faceletContext) {
                protected TagAttribute getAttribute(String attributeName) {
                    return AbstractFaceletsComponentHandler.this.getAttribute(attributeName);
                }
            },(AbstractComponentTag) metaRule.getTag());
        }else{
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
                tag.removeFacesContext();
            }
        }
    }

}
