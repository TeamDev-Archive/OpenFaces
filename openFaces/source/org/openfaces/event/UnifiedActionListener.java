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

package org.openfaces.event;

import org.openfaces.taglib.internal.AbstractTag;
import org.openfaces.util.AjaxUtil;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.MethodExpressionActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * UnifiedActionListener is unified {@link MethodExpressionActionListener}, which allows developers to use
 * different types of FacesEvent in event handlers. It holds a set of {@link MethodExpression} instances
 * and try to invoke them one by one till the first successful invocation.
 *
 * @author Vladislav Lubenskiy
 */
public class UnifiedActionListener extends MethodExpressionActionListener {
    private Map<Class, MethodExpression> listeners = new HashMap<Class, MethodExpression>();
    private Behavior defaultBehavior = new Behavior() {
        public void broadcast(BehaviorEvent event) {
            throw new UnsupportedOperationException("This method is not expected to be invoked.");
        }
    };

    public UnifiedActionListener() {
        super();
    }

    public UnifiedActionListener(AbstractTag.ExpressionCreator creator, FacesContext context, String expressionString) {
        super();
        listeners.put(ActionEvent.class,
                creator.createMethodExpression(context, "actionListener", expressionString, void.class, new Class[]{ActionEvent.class}));
        listeners.put(AjaxBehaviorEvent.class,
                creator.createMethodExpression(context, "actionListener", expressionString, void.class, new Class[]{AjaxBehaviorEvent.class}));
        listeners.put(AjaxActionEvent.class,
                creator.createMethodExpression(context, "actionListener", expressionString, void.class, new Class[]{AjaxActionEvent.class}));
    }

    @Override
    public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();

        if (AjaxUtil.isAjaxRequest(FacesContext.getCurrentInstance())) {
            try {
                listeners.get(AjaxActionEvent.class).invoke(elContext, new Object[]{new AjaxActionEvent(
                        actionEvent.getComponent(), defaultBehavior)});
                return;
            } catch (MethodNotFoundException e) {
                // Method with that signature is not found, will search one with another argument type.
            }

            try {
                listeners.get(AjaxBehaviorEvent.class).invoke(elContext, new Object[]{new AjaxBehaviorEvent(
                        actionEvent.getComponent(), defaultBehavior)});
                return;
            } catch (MethodNotFoundException e) {
                throw new MethodNotFoundException("Couldn't find Ajax action handler method. Method expression: " +
                        listeners.get(ActionEvent.class).getExpressionString() + ". " +
                        "Note, the appropriate method should receive one parameter of either javax.faces.event.AjaxBehaviorEvent or " +
                        "org.openfaces.event.AjaxActionEvent type.");
            }
        } else {
            try {
                listeners.get(ActionEvent.class).invoke(elContext, new Object[]{actionEvent});
                return;
            } catch (MethodNotFoundException e) {
                throw new MethodNotFoundException("Couldn't find Ajax action handler method. Method expression: " +
                        listeners.get(ActionEvent.class).getExpressionString() + ". " +
                        "Note, the appropriate method should receive one parameter of javax.faces.event.ActionEvent.");
            }

        }
    }
}
