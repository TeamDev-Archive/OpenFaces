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

package org.openfaces.testapp.jBossSeam.binding;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.openfaces.component.calendar.Calendar;
import org.openfaces.component.input.DateChooser;
import org.openfaces.component.input.DropDownField;
import org.openfaces.component.output.DynamicImage;
import org.openfaces.component.output.HintLabel;
import org.openfaces.component.panel.FoldingPanel;
import org.openfaces.component.panel.TabbedPane;
import org.openfaces.component.select.TabSet;
import org.openfaces.component.select.TwoListSelection;
import org.openfaces.component.window.Confirmation;
import org.openfaces.component.window.PopupLayer;

/**
 * @author Tatyana Matveyeva
 */
@Name("seamcomponentsbinding")
@Scope(ScopeType.SESSION)
public class SeamComponentsBinding {

    private Calendar calendar;
    private DateChooser dateChooser;
    private Confirmation confirmation;
    private DropDownField dropDownField;
    private DynamicImage dynamicImage;
    private FoldingPanel foldingPanel;
    private HintLabel hintLabel;
    private PopupLayer popupLayer;
    private TabbedPane tabbedPane;
    private TabSet tabSet;
    private TwoListSelection twoListSelection;


    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public DateChooser getDateChooser() {
        return dateChooser;
    }

    public void setDateChooser(DateChooser dateChooser) {
        this.dateChooser = dateChooser;
    }

    public Confirmation getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Confirmation confirmation) {
        this.confirmation = confirmation;
    }

    public DropDownField getDropDownField() {
        return dropDownField;
    }

    public void setDropDownField(DropDownField dropDownField) {
        this.dropDownField = dropDownField;
    }

    public DynamicImage getDynamicImage() {
        return dynamicImage;
    }

    public void setDynamicImage(DynamicImage dynamicImage) {
        this.dynamicImage = dynamicImage;
    }

    public FoldingPanel getFoldingPanel() {
        return foldingPanel;
    }

    public void setFoldingPanel(FoldingPanel foldingPanel) {
        this.foldingPanel = foldingPanel;
    }

    public HintLabel getHintLabel() {
        return hintLabel;
    }

    public void setHintLabel(HintLabel hintLabel) {
        this.hintLabel = hintLabel;
    }

    public PopupLayer getPopupLayer() {
        return popupLayer;
    }

    public void setPopupLayer(PopupLayer popupLayer) {
        this.popupLayer = popupLayer;
    }

    public TabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(TabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    public TabSet getTabSet() {
        return tabSet;
    }

    public void setTabSet(TabSet tabSet) {
        this.tabSet = tabSet;
    }

    public TwoListSelection getTwoListSelection() {
        return twoListSelection;
    }

    public void setTwoListSelection(TwoListSelection twoListSelection) {
        this.twoListSelection = twoListSelection;
    }
}
