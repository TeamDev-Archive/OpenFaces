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
package org.openfaces.application;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

/**
 * @author Eugene Goncharov
 */
public class OpenFacesApplicationFactory extends ApplicationFactory {

    private Application application;
    private ApplicationFactory delegateApplicationFactory;


    public OpenFacesApplicationFactory(ApplicationFactory delegateFactory) {
        delegateApplicationFactory = delegateFactory;
    }

    @Override
    public Application getApplication() {
        synchronized (OpenFacesApplicationFactory.class) {
            if (application == null) {
                synchronized (OpenFacesApplicationFactory.class) {
                    Application application = delegateApplicationFactory.getApplication();
                    this.application = new OpenFacesApplication(application);
                }
            }
        }
        return application;
    }

    @Override
    public void setApplication(Application application) {
        this.application = application;
    }


    public ApplicationFactory getDelegateApplicationFactory() {
        return delegateApplicationFactory;
    }

    public void setDelegateApplicationFactory(ApplicationFactory delegateApplicationFactory) {
        this.delegateApplicationFactory = delegateApplicationFactory;
    }
}
