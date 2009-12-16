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

package org.openfaces.demo.beans.selectbooleancheckbox;

import org.openfaces.component.input.SelectBooleanCheckbox;
import org.openfaces.demo.beans.util.FacesUtils;

import javax.faces.event.ActionEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class PermissionSchema {

    public PermissionSchema() {
        setAssigned(Permission.EDIT_ANY_POST, PermissionGroup.USER, true);
    }

    private Map<PermissionGroup, Map<Permission, State>> permissionsAssignment;


    public Map<PermissionGroup, Map<Permission, State>> getPermissionsAssignment() {
        if (permissionsAssignment == null) {
            permissionsAssignment = new PermissionAssigmnent();
        }
        return permissionsAssignment;
    }

    public Collection<Permission> getPermissions() {
        return EnumSet.allOf(Permission.class);
    }

    public Collection<PermissionGroup> getPermissionGroups() {
        return EnumSet.allOf(PermissionGroup.class);
    }

    public boolean isAssigned(Permission permission, PermissionGroup permissionGroup) {
        Boolean state = getPermissionsAssignment().get(permissionGroup).get(permission).getState();
        return state == null || state;
    }

    public void setAssigned(Permission permission, PermissionGroup permissionGroup, Boolean state) {
        getPermissionsAssignment().get(permissionGroup).get(permission).setState(state);
    }


    private class PermissionAssigmnent extends HashMap<PermissionGroup, Map<Permission, State>> {
        @Override
        public Map<Permission, State> get(final Object key) {
            if (key == null) {
                return null;
            }
            final PermissionGroup permissionGroup;
            if (key instanceof PermissionGroup) {
                permissionGroup = (PermissionGroup) key;
            } else {
                permissionGroup = PermissionGroup.valueOf(key.toString());
            }
            Map<Permission, State> value = super.get(permissionGroup);
            if (value == null) {
                value = new HashMap<Permission, State>() {

                    @Override
                    public State get(Object key) {
                        if (key == null) {
                            return null;
                        }
                        final Permission permission;
                        if (key instanceof Permission) {
                            permission = (Permission) key;
                        } else {
                            permission = Permission.valueOf(key.toString());
                        }
                        State result = super.get(permission);
                        if (result == null) {
                            result = new State() {
                                @Override
                                public void setState(Boolean state) {
                                    super.setState(state);
                                    if (state != null) {
                                        setDependentPermissions(state);
                                    }
                                }

                                @Override
                                public void setDependent(boolean dependent) {
                                    super.setDependent(dependent);
                                    setDependentPermissions(dependent);

                                }

                                private void setDependentPermissions(boolean value) {
                                    Collection<Permission> dependendPermissions = permission.getDependent();
                                    for (Permission dependendPermission : dependendPermissions) {
                                        get(dependendPermission).setDependent(value);
                                    }
                                    Collection<PermissionGroup> dependentPermissionGroups = permissionGroup.getDependent();
                                    for (PermissionGroup dependendPermissionGroup : dependentPermissionGroups) {
                                        PermissionAssigmnent.this.get(dependendPermissionGroup).get(permission).setDependent(value);
                                    }
                                }
                            };
                            super.put(permission, result);
                        }
                        return result;
                    }
                };
                super.put(permissionGroup, value);
            }
            return value;
        }
    }

    public class State {

        private Boolean state = false;
        private Boolean outputState = false;
        private int dependent = 0;

        public Boolean getState() {
            return state;
        }

        public void setState(Boolean state) {
            this.state = state;
        }

        public Boolean getOutputState() {
            return state;
        }

        public void setOutputState(Boolean outputState) {
            this.outputState = outputState;
        }

        private void submitState() {
            setState(outputState);
        }

        public boolean isDependent() {
            return dependent != 0;
        }

        public void setDependent(boolean dependent) {

            if (dependent) {
                this.dependent++;
            } else {
                this.dependent--;
            }
            if (!isDependent()) {
                if (state == null) {
                    state = false;
                }
            } else {
                if (state != null && !state) {
                    state = null;
                }
            }

        }

    }

    public void updateAssignment(ActionEvent event) {

        String groupString = String.valueOf(FacesUtils.getEventParameter(event, "group"));
        PermissionGroup permissionGroup = PermissionGroup.valueOf(groupString);
        String permissionString = String.valueOf(FacesUtils.getEventParameter(event, "permission"));
        Permission permission = Permission.valueOf(permissionString);

        getPermissionsAssignment().get(permissionGroup).get(permission).submitState();
    }


    public Iterable<String> getDefaultStateList() {
        return SelectBooleanCheckbox.DAFAULT_STATE_LIST;
    }

    public Iterable<String> getDependentStateList() {
        return Arrays.asList(SelectBooleanCheckbox.SELECTED_STATE, SelectBooleanCheckbox.UNDEFINED_STATE);
    }

}
