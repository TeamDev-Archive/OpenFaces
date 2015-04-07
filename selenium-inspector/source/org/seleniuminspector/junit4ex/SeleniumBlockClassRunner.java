/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.seleniuminspector.junit4ex;

import org.testng.annotations.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * @author Andrii Gorbatov
 */
public class SeleniumBlockClassRunner extends BlockJUnit4ClassRunner {

    public SeleniumBlockClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        Statement statement = super.methodBlock(method);
        statement = withStartBrowserForMethod(method, statement);
        statement = withStopBrowserForMethod(method, statement);

        return statement;
    }

    @Override
    protected Statement classBlock(RunNotifier notifier) {
        Statement statement = super.classBlock(notifier);
        statement = withStartBrowserForClass(statement);
        statement = withStopBrowserForClass(statement);

        return statement;
    }

    public Statement withStartBrowserForMethod(FrameworkMethod method, Statement next) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null) {
            Class<?> testClass = getUnderlyingTestClass();
            BrowserRestartPolicy browserRestartPolicy = testClass.getAnnotation(BrowserRestartPolicy.class);
            if (browserRestartPolicy != null && browserRestartPolicy.runType() == BrowserRestartPolicy.RunType.METHOD) {
                return new StartBrowserRun(next);
            } else {
                return next;
            }
        } else {
            return next;
        }
    }

    public Statement withStopBrowserForMethod(FrameworkMethod method, Statement next) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null) {
            Class<?> testClass = getUnderlyingTestClass();
            BrowserRestartPolicy browserRestartPolicy = testClass.getAnnotation(BrowserRestartPolicy.class);
            if (browserRestartPolicy != null && browserRestartPolicy.runType() == BrowserRestartPolicy.RunType.METHOD) {
                return new StopBrowserRun(next);
            } else {
                return next;
            }
        } else {
            return next;
        }
    }

    public Statement withStartBrowserForClass(Statement next) {
        Class<?> testClass = getUnderlyingTestClass();
        BrowserRestartPolicy browserRestartPolicy = testClass.getAnnotation(BrowserRestartPolicy.class);
        if (browserRestartPolicy != null && browserRestartPolicy.runType() == BrowserRestartPolicy.RunType.CLASS) {
            return new StartBrowserRun(next);
        } else {
            return next;
        }
    }

    public Statement withStopBrowserForClass(Statement next) {
        Class<?> testClass = getUnderlyingTestClass();
        BrowserRestartPolicy browserRestartPolicy = testClass.getAnnotation(BrowserRestartPolicy.class);
        if (browserRestartPolicy != null && browserRestartPolicy.runType() == BrowserRestartPolicy.RunType.CLASS) {
            return new StopBrowserRun(next);
        } else {
            return next;
        }
    }

    private Class<?> getUnderlyingTestClass() {
        return this.getTestClass().getJavaClass();
    }

}
