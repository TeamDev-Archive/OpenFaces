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

package org.openfaces.portlet;


import com.sun.faces.portlet.BridgeConstants;
import org.openfaces.ajax.AjaxViewHandler;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.webapp.FacesServlet;
import javax.portlet.*;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <p><strong>OpenFacesJSFPortlet</strong> is a portlet that manages the request
 * processing lifecycle for web applications that are utilizing JavaServer
 * Faces to construct the user interface in a portlet-based environment.</p>
 */

public class OpenFacesJSFPortlet extends GenericPortlet {

    // The Logger instance for this class
    private static Logger logger = Logger.getLogger(OpenFacesJSFPortlet.class.getPackage().getName(), "JSFPLogMessages");

    /**
     * <p>The key used to store/retrive the PortletConfig object.</p>
     */
    public static final String PORTLET_CONFIG = "javax.portlet.PortletConfig";

    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>Context initialization parameter name for the lifecycle identifier
     * of the {@link Lifecycle} instance to be utilized.</p>
     */
    private static final String LIFECYCLE_ID_ATTR =
            FacesServlet.LIFECYCLE_ID_ATTR;

    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link Application} instance for this web application.</p>
     */
    private Application application = null;


    /**
     * <p>Factory for {@link FacesContext} instances.</p>
     */
    private FacesContextFactory facesContextFactory = null;


    /**
     * <p>The {@link Lifecycle} instance to use for request processing.</p>
     */
    private Lifecycle lifecycle = null;


    /**
     * <p>The <code>PortletConfig</code> instance for this portlet.</p>
     */
    private PortletConfig portletConfig = null;

    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Release all resources acquired at startup time.</p>
     */
    public void destroy() {
        logger.finest("PS_CSFP0006");
        application = null;
        facesContextFactory = null;
        lifecycle = null;
        portletConfig = null;
        logger.finest("PS_CSFP0007");
    }


    /**
     * <p>Acquire the factory instance we will require.</p>
     *
     * @throws PortletException if, for any reason, the startp of
     *                          this Faces application failed.  This includes errors in the
     *                          config file that is parsed before or during the processing of
     *                          this <code>init()</code> method.
     */
    public void init(PortletConfig portletConfig) throws PortletException {

        logger.finest("PS_CSFP0008");

        // Save our PortletConfig instance
        this.portletConfig = portletConfig;

        logger.finest("PS_CSFP0009");
    }

    public FacesContextFactory getFacesContextFactory() throws PortletException {
        if (facesContextFactory != null) {
            return facesContextFactory;
        }
        // Acquire our FacesContextFactory instance
        try {
            facesContextFactory = (FacesContextFactory)
                    FactoryFinder.getFactory
                            (FactoryFinder.FACES_CONTEXT_FACTORY);
            logger.log(Level.FINEST, "PS_CSFP0010", facesContextFactory);
        } catch (FacesException e) {
            Throwable rootCause = e.getCause();
            if (rootCause == null) {
                throw e;
            } else {
                throw new PortletException(e.getMessage(), rootCause);
            }
        }
        return facesContextFactory;
    }

    public Lifecycle getLifecycle() throws PortletException {
        if (lifecycle != null) {
            return lifecycle;
        }
        try {
            LifecycleFactory lifecycleFactory = (LifecycleFactory)
                    FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            logger.log(Level.FINEST, "PS_CSFP0011", lifecycleFactory);
            String lifecycleId =
                    portletConfig.getPortletContext().getInitParameter
                            (LIFECYCLE_ID_ATTR);
            logger.log(Level.FINE, "PS_CSFP0012", lifecycleId);
            if (lifecycleId == null) {
                lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
            }
            lifecycle = lifecycleFactory.getLifecycle(lifecycleId);
            logger.log(Level.FINEST, "PS_CSFP0013", lifecycle);
        } catch (FacesException e) {
            Throwable rootCause = e.getCause();
            if (rootCause == null) {
                throw e;
            } else {
                throw new PortletException(e.getMessage(), rootCause);
            }
        }
        return lifecycle;
    }

    /**
     * <p>Perform the request processing lifecycle for the specified request,
     * up to (but not including) the <em>Render Response</em> phase.</p>
     *
     * @param request  The portlet request we are processing
     * @param response The portlet response we are processing
     * @throws IOException      if an input/output error occurs
     * @throws PortletException if a portlet processing error occurs
     */
    public void processAction(ActionRequest request, ActionResponse response)
            throws IOException, PortletException {
        logger.finest("PS_CSFP0014");

        storeInitParameter(request);

        // Acquire the FacesContext instance for this request
        FacesContext context =
                getFacesContextFactory().getFacesContext
                        (portletConfig.getPortletContext(),
                                request, response, lifecycle);

        // Store the PortletConfig in the Application Map
        storePortletConfig(context);

        logger.finest("PS_CSFP0015");

        // Execute the pre-render request processing lifecycle for this request
        if (AjaxViewHandler.isNewSession(context.getExternalContext().getSession(false))) {
            Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
            String viewId = (requestParameterMap.containsKey("com.sun.faces.portlet.VIEW_ID"))
                    ? (String) requestParameterMap.get("com.sun.faces.portlet.VIEW_ID")
                    : null;

            storeInitParameter(request, viewId);

        }

        try {
            getLifecycle().execute(context);
            logger.finest("PS_CSFP0016");
        } catch (FacesException e) {
            Throwable t = ((FacesException) e).getCause();
            if (t == null) {
                throw new PortletException(e.getMessage(), e);
            } else {
                if (t instanceof PortletException) {
                    throw ((PortletException) t);
                } else if (t instanceof IOException) {
                    throw ((IOException) t);
                } else {
                    throw new PortletException(t.getMessage(), t);
                }
            }

        } finally {
            PortletSession session = (PortletSession) context.getExternalContext().getSession(true);
            PortletMode mode = request.getPortletMode();
            Boolean errorFlag = (Boolean) session.getAttribute(BridgeConstants.ERROR_FLAG);
            // If any error occured use the same mode
            if (errorFlag != null && errorFlag.equals(Boolean.TRUE)) {
                response.setPortletMode(mode);
            } else {
                // Check if the page is same as the INIT_VIEW page, if yes set the mode to VIEW
                // If the page is not the INIT_VIEW page, it means EDIT/HELP has navigation
                // continue by setting the same mode as the current mode.
                String currentViewId = context.getViewRoot().getViewId();
                String viewId = (String) portletConfig.
                        getInitParameter(BridgeConstants.INIT_VIEW_PARAMETER);
                try {
                    if (viewId.equals(currentViewId))
                        response.setPortletMode(PortletMode.VIEW);
                    else
                        response.setPortletMode(mode);
                } catch (IllegalStateException ise) {
                    // sendRedirect might have been called
                    logger.log(Level.INFO, "PS_CSFP0017", ise.getMessage());
                }
            }
            // Release the FacesContext instance for this request
            context.release();
        }
        logger.finest("PS_CSFP0018");
    }


    /**
     * <p>Perform the <em>Render Response</em> phase of the request processing
     * lifecycle for the specified request.</p>
     *
     * @param request  The portlet request we are processing
     * @param response The portlet response we are processing
     * @throws IOException      if an input/output error occurs
     * @throws PortletException if a portlet processing error occurs
     */
    public void render(RenderRequest request, RenderResponse response)
            throws IOException, PortletException {
        logger.finest("PS_CSFP0019");

        // in a portlet environment, the context type of reponse must
        // be set explicitly.
        response.setContentType(request.getResponseContentType());
        // set portlet title if its set.
        java.util.ResourceBundle bundle =
                portletConfig.getResourceBundle(request.getLocale());
        if (bundle != null) {
            String title = null;
            try {
                title = bundle.getString("javax.portlet.title");
                response.setTitle(title);
            } catch (Exception e) {
                // Ignore MissingResourceException
            }
        }

        storeInitParameter(request);

        // Acquire the FacesContext instance for this request
        FacesContext context =
                getFacesContextFactory().getFacesContext
                        (portletConfig.getPortletContext(),
                                request, response, getLifecycle());

        // Store the PortletConfig in the Application Map
        storePortletConfig(context);

        logger.finest("PS_CSFP0020");

        // Execute the render response phase for this request
        try {
            getLifecycle().render(context);
            logger.finest("PS_CSFP0021");
        } catch (FacesException e) {
            Throwable t = ((FacesException) e).getCause();
            if (t == null) {
                throw new PortletException(e.getMessage(), e);
            } else {
                if (t instanceof PortletException) {
                    throw ((PortletException) t);
                } else if (t instanceof IOException) {
                    throw ((IOException) t);
                } else {
                    throw new PortletException(t.getMessage(), t);
                }
            }
        } finally {

            // Release the FacesContext instance for this request
            context.release();

        }
        logger.finest("PS_CSFP0022");
    }

    // --------------------------------------------------------- Private Methods

    /**
     * Stores the init parameter identifier and the value for that in the request map.
     * The init parameter identifier can be either com.sun.faces.portlet.INIT_VIEW or
     * com.sun.faces.portlet.INIT_EDIT or com.sun.faces.portlet.INIT_HELP. The appropriate
     * identifier based on the Portlet mode is stored. This value is used in LifeCycleImpl
     * to keep the state information for each mode separate.
     * The value for the identifier is used during the RenderResponse Phase to
     * display the initial view.
     *
     * @param request the portlet request, can be ActionRequest or RenderRequest
     * @throws PortletException
     */
    private void storeInitParameter(PortletRequest request) throws PortletException {
        PortletMode mode = request.getPortletMode();

        // Get the init parameter identifier based on the mode.
        String initParameterIdentifier = null;
        if (mode.equals(PortletMode.VIEW)) {
            initParameterIdentifier = BridgeConstants.INIT_VIEW_PARAMETER;
        } else if (mode.equals(PortletMode.EDIT)) {
            initParameterIdentifier = BridgeConstants.INIT_EDIT_PARAMETER;
        } else if (mode.equals(PortletMode.HELP)) {
            initParameterIdentifier = BridgeConstants.INIT_HELP_PARAMETER;
        }
        // Check whether the requested mode is allowed
        if (!request.isPortletModeAllowed(mode) || initParameterIdentifier == null) {
            throw new PortletException(mode + " is not allowed");
        }

        String initId = (String) portletConfig.getInitParameter(initParameterIdentifier);
        if (initId != null) {
            // store the identifier in the request map. This identifier can be either a
            // INIT_VIEW or INIT_EDIT or INIT_HELP based on the portlet mode. This information
            // is used in LifeCycleImpl to keep  the state information for each mode separate.
            request.setAttribute(BridgeConstants.INIT_PARAMETER, initParameterIdentifier);
            // store the value for the identifier in the requestMap so that it could be used
            // during the RenderResponse Phase to display the initial view.
            request.setAttribute(initParameterIdentifier, initId);
        } else {
            throw new PortletException(initParameterIdentifier + " must be specified");
        }
    }


    private void storeInitParameter(PortletRequest request, String viewId) throws PortletException {
        PortletMode mode = request.getPortletMode();

        // Get the init parameter identifier based on the mode.
        String initParameterIdentifier = null;
        if (mode.equals(PortletMode.VIEW)) {
            initParameterIdentifier = BridgeConstants.INIT_VIEW_PARAMETER;
        } else if (mode.equals(PortletMode.EDIT)) {
            initParameterIdentifier = BridgeConstants.INIT_EDIT_PARAMETER;
        } else if (mode.equals(PortletMode.HELP)) {
            initParameterIdentifier = BridgeConstants.INIT_HELP_PARAMETER;
        }
        // Check whether the requested mode is allowed
        if (!request.isPortletModeAllowed(mode) || initParameterIdentifier == null) {
            throw new PortletException(mode + " is not allowed");
        }

        String initId = (String) portletConfig.getInitParameter(initParameterIdentifier);
        if (initId != null) {
            // store the identifier in the request map. This identifier can be either a
            // INIT_VIEW or INIT_EDIT or INIT_HELP based on the portlet mode. This information
            // is used in LifeCycleImpl to keep  the state information for each mode separate.
            request.setAttribute(BridgeConstants.INIT_PARAMETER, initParameterIdentifier);
            // store the value for the identifier in the requestMap so that it could be used
            // during the RenderResponse Phase to display the initial view.
            request.setAttribute(initParameterIdentifier, viewId);
        } else {
            throw new PortletException(initParameterIdentifier + " must be specified");
        }
    }


    /**
     * Store the PortletConfig in the Application Map
     */
    private void storePortletConfig(FacesContext context) {
        Map applicationMap = context.getExternalContext().getApplicationMap();
        if (!applicationMap.containsKey(PORTLET_CONFIG))
            applicationMap.put(PORTLET_CONFIG, portletConfig);
    }
}
