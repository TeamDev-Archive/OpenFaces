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
package org.openfaces.validation.core;

import java.io.Serializable;
import java.util.*;
import java.text.MessageFormat;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.hibernate.validator.InvalidValue;

/**
 * <p><strong>FacesMessages</strong></p> is a utility class that is used
 * to creating {@link FacesMessage} instances and adding them to {@link FacesContext}
 *
 * @author Gavin King
 * @author Eugene Goncharov
 */
public class FacesMessages implements Serializable {
    private static final long serialVersionUID = -5395975397632138270L;
    private static FacesMessages ourInstance;
    private transient List<Runnable> tasks;

    private List<Message> facesMessages = new ArrayList<Message>();
    private Map<String, List<Message>> keyedFacesMessages = new HashMap<String, List<Message>>();


    protected FacesMessages() {

    }

    /**
     * Workaround for non-serializability of
     * JSF FacesMessage.Severity class.
     *
     * @author Gavin King
     */
    class Message implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = -6644797739333302312L;
        private String summary;
        private String detail;
        private int severityOrdinal;

        Message(FacesMessage fm) {
            summary = fm.getSummary();
            detail = fm.getDetail();
            severityOrdinal = fm.getSeverity().getOrdinal();
        }

        FacesMessage toFacesMessage() {
            Severity severity = null;
            for (Object o : FacesMessage.VALUES) {
                severity = (Severity) o;
                if (severity.getOrdinal() == severityOrdinal) {
                    break;
                }
            }
            return new FacesMessage(severity, summary, detail);
        }
    }

    public void beforeRenderResponse() {
        for (Message message : facesMessages) {
            FacesContext.getCurrentInstance().addMessage(null, message.toFacesMessage());
        }
        for (Map.Entry<String, List<Message>> entry : keyedFacesMessages.entrySet()) {
            for (Message msg : entry.getValue()) {
                FacesContext.getCurrentInstance().addMessage(entry.getKey(), msg.toFacesMessage());
            }
        }
        clear();
    }

    /**
     * Get all faces messages that have already been added
     * to the context.
     *
     * @return a list of messages
     */
    public List<FacesMessage> getCurrentMessages() {
        List<FacesMessage> result = new ArrayList<FacesMessage>();
        Iterator<FacesMessage> iter = FacesContext.getCurrentInstance().getMessages();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /**
     * Get all faces global messages that have already been added
     * to the context.
     *
     * @return a list of global messages
     */
    public List<FacesMessage> getCurrentGlobalMessages() {
        List<FacesMessage> result = new ArrayList<FacesMessage>();
        Iterator<FacesMessage> iter = FacesContext.getCurrentInstance().getMessages(null);
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /**
     * Get all faces messages that have already been added
     * to the control.
     *
     * @return a list of messages
     */
    public List<FacesMessage> getCurrentMessagesForControl(String id) {
        String clientId = getClientId(id);
        List<FacesMessage> result = new ArrayList<FacesMessage>();
        Iterator<FacesMessage> iter = FacesContext.getCurrentInstance().getMessages(clientId);
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    public static void afterPhase() {
    }

    public void clear() {
        facesMessages.clear();
        keyedFacesMessages.clear();
    }

    /**
     * Add a FacesMessage that will be used
     * the next time a page is rendered.
     */
    public void add(FacesMessage facesMessage) {
        if (facesMessage != null) {
            facesMessages.add(new Message(facesMessage));
        }
    }

    /**
     * Add a FacesMessage instance to a particular component id
     *
     * @param id a JSF component id
     */
    public void addToControl(String id, FacesMessage facesMessage) {
        if (facesMessage != null) {
            String clientId = getClientId(id);
            List<Message> list = keyedFacesMessages.get(clientId);
            if (list == null) {
                list = new ArrayList<Message>();
                keyedFacesMessages.put(clientId, list);
            }
            list.add(new Message(facesMessage));
        }
    }

    /**
     * Add a templated FacesMessage that will be used
     * the next time a page is rendered.
     */
    public void add(String messageTemplate, Object... params) {
        addToTasks(FacesMessage.SEVERITY_INFO, null, messageTemplate, params);
    }

    /**
     * Add a templated FacesMessage that will be used
     * the next time a page is rendered.
     */
    public void add(Severity severity, String messageTemplate, Object... params) {
        addToTasks(severity, null, messageTemplate, params);
    }

    /**
     * Add a templated FacesMessage to a particular JSF control
     *
     * @param id a JSF component id
     */
    public void addToControl(String id, String messageTemplate, Object... params) {
        addToControl(id, FacesMessage.SEVERITY_INFO, messageTemplate, params);
    }

    /**
     * Add a templated FacesMessage to a particular JSF control
     *
     * @param id a JSF component id
     */
    public void addToControl(String id, Severity severity, String messageTemplate, Object... params) {
        addToTasks(id, severity, null, messageTemplate, params);
    }

    /**
     * Add a templated FacesMessage by looking for the message
     * template in the resource bundle.
     */
    public void addFromResourceBundle(String key, Object... params) {
        addFromResourceBundle(FacesMessage.SEVERITY_INFO, key, params);
    }

    /**
     * Add a templated FacesMessage by looking for the message
     * template in the resource bundle.
     */
    public void addFromResourceBundle(Severity severity, String key, Object... params) {
        addFromResourceBundleOrDefault(severity, key, key, params);
    }

    /**
     * Add a templated FacesMessage to a particular component id by looking
     * for the message template in the resource bundle. If it is missing, use
     * the given message template.
     */
    public void addFromResourceBundleOrDefault(String key, String defaultMessageTemplate, Object... params) {
        addFromResourceBundleOrDefault(FacesMessage.SEVERITY_INFO, key, defaultMessageTemplate, params);
    }

    /**
     * Add a templated FacesMessage to a particular component id by looking
     * for the message template in the resource bundle. If it is missing, use
     * the given message template.
     */
    public void addFromResourceBundleOrDefault(Severity severity, String key, String defaultMessageTemplate, Object... params) {
        addToTasks(severity, key, defaultMessageTemplate, params);
    }

    /**
     * Add a templated FacesMessage to a particular component id by looking
     * for the message template in the resource bundle.
     */
    public void addToControlFromResourceBundle(String id, String key, Object... params) {
        addToControlFromResourceBundle(id, FacesMessage.SEVERITY_INFO, key, params);
    }

    /**
     * Add a templated FacesMessage to a particular component id by looking
     * for the message template in the resource bundle.
     */
    public void addToControlFromResourceBundle(String id, Severity severity, String key, Object... params) {
        addToControlFromResourceBundleOrDefault(id, severity, key, key, params);
    }

    /**
     * Add a templated FacesMessage to a particular component id by looking
     * for the message template in the resource bundle. If it is missing, use
     * the given message template.
     */
    public void addToControlFromResourceBundleOrDefault(String id, String key, String defaultMessageTemplate, Object... params) {
        addToControlFromResourceBundleOrDefault(id, FacesMessage.SEVERITY_INFO, key, defaultMessageTemplate, params);
    }

    /**
     * Add a templated FacesMessage to a particular component id by looking
     * for the message template in the resource bundle. If it is missing, use
     * the given message template.
     */
    public void addToControlFromResourceBundleOrDefault(String id, Severity severity, String key, String defaultMessageTemplate, Object... params) {
        addToTasks(id, severity, key, defaultMessageTemplate, params);
    }

    private static String getBundleMessage(String key, String defaultMessageTemplate) {
        String messageTemplate = defaultMessageTemplate;
        if (key != null) {
            ResourceBundle resourceBundle = null;
            if (resourceBundle != null) {
                try {
                    String bundleMessage = resourceBundle.getString(key);
                    if (bundleMessage != null) messageTemplate = bundleMessage;
                }
                catch (MissingResourceException mre) {
                } //swallow
            }
        }
        return messageTemplate;
    }

    public void add(InvalidValue[] ivs) {
        for (InvalidValue iv : ivs) {
            add(iv);
        }
    }

    public void addToControls(InvalidValue[] ivs) {
        for (InvalidValue iv : ivs) {
            addToControl(iv);
        }
    }

    public void add(InvalidValue iv) {
        add(FacesMessage.SEVERITY_WARN, iv.getMessage());
    }

    public void addToControl(InvalidValue iv) {
        addToControl(iv.getPropertyName(), iv);
    }

    public void addToControl(String id, InvalidValue iv) {
        addToControl(id, FacesMessage.SEVERITY_WARN, iv.getMessage());
    }

    public static FacesMessage createFacesMessage(Severity severity, String messageTemplate, Object... params) {
        return new FacesMessage(severity, interpolate(messageTemplate, params), null);
    }

    /**
     * Replace all EL expressions in the form #{...} with their evaluated
     * values.
     *
     * @param string a template
     * @return the interpolated string
     */
    public static String interpolate(String string, Object... params) {
        if (params == null) {
            params = new Object[0];
        }

        if (params.length > 10) {
            throw new IllegalArgumentException("more than 10 parameters");
        }

        if (string.indexOf('#') >= 0 || string.indexOf('{') >= 0) {
            string = interpolateExpressions(string, params);
        }

        return string;
    }

    private static String interpolateExpressions(String string, Object... params) {
        StringTokenizer tokens = new StringTokenizer(string, "#{}", true);
        StringBuilder builder = new StringBuilder(string.length());
        while (tokens.hasMoreTokens()) {
            String tok = tokens.nextToken();
            if ("#".equals(tok) && tokens.hasMoreTokens()) {
                String nextTok = tokens.nextToken();
                if ("{".equals(nextTok)) {
                    String expression = "#{" + tokens.nextToken() + "}";
                    try {
                        Object value = Expressions.instance().createValueExpression(expression).getValue();
                        if (value != null) builder.append(value);
                    }
                    catch (Exception e) {
                    }
                    tokens.nextToken(); //the }
                } else {
                    int index;
                    try {
                        index = Integer.parseInt(nextTok.substring(0, 1));
                        if (index >= params.length) {
                            //log.warn("parameter index out of bounds: " + index + " in: " + string);
                            builder.append("#").append(nextTok);
                        } else {
                            builder.append(params[index]).append(nextTok.substring(1));
                        }
                    }
                    catch (NumberFormatException nfe) {
                        builder.append("#").append(nextTok);
                    }
                }
            } else if ("{".equals(tok)) {
                StringBuilder expr = new StringBuilder();

                expr.append(tok);
                int level = 1;

                while (tokens.hasMoreTokens()) {
                    String nextTok = tokens.nextToken();
                    expr.append(nextTok);

                    if (nextTok.equals("{")) {
                        ++level;
                    } else if (nextTok.equals("}")) {
                        if (--level == 0) {
                            try {
                                String value = new MessageFormat(expr.toString(), Locale.getDefault()).format(params);
                                builder.append(value);
                            }
                            catch (Exception e) {
                                // if it is a bad message, use the expression itself
                                builder.append(expr);
                            }
                            expr = null;
                            break;
                        }
                    }
                }

                if (expr != null) {
                    builder.append(expr);
                }
            } else {
                builder.append(tok);
            }
        }
        return builder.toString();
    }


    public static FacesMessage createFacesMessage(Severity severity, String key, String defaultMessageTemplate, Object... params) {
        String message = getBundleMessage(key, defaultMessageTemplate);
        if (message != null && message.length() > 0) {
            return createFacesMessage(severity, message, params);
        } else {
            return null;
        }
    }

    private String getClientId(String id) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return getClientId(facesContext.getViewRoot(), id, facesContext);
    }

    private static String getClientId(UIComponent component, String id, FacesContext facesContext) {
        String componentId = component.getId();
        if (componentId != null && componentId.equals(id)) {
            return component.getClientId(facesContext);
        } else {
            Iterator<UIComponent> iter = component.getFacetsAndChildren();
            while (iter.hasNext()) {
                UIComponent child = iter.next();
                String clientId = getClientId(child, id, facesContext);
                if (clientId != null) return clientId;
            }
            return null;
        }
    }

    private List<Runnable> getTasks() {
        if (tasks == null) {
            tasks = new ArrayList<Runnable>();
        }
        return tasks;
    }

    private void addToTasks(final Severity severity, final String key, final String messageTemplate, final Object... params) {
        getTasks().add(new Runnable() {
            public void run() {
                add(createFacesMessage(severity, key, messageTemplate, params));
            }
        });
    }

    private void addToTasks(final String id, final Severity severity, final String key, final String messageTemplate, final Object... params) {
        getTasks().add(new Runnable() {
            public void run() {
                addToControl(id, createFacesMessage(severity, key, messageTemplate, params));
            }
        });
    }

    public static FacesMessages instance() {
        if (ourInstance == null) {
            ourInstance = new FacesMessages();
        }
        return ourInstance;
    }

}

