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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

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
            permissionsAssignment = new HashMap<PermissionGroup, Map<Permission, State>>() {
                @Override
                public Map<Permission, State> get(final Object key) {
                    PermissionGroup permissionGroup = (PermissionGroup) key;
                    Map<Permission, State> value = super.get(permissionGroup);
                    if (value == null) {
                        value = new HashMap<Permission, State>() {

                            @Override
                            public State get(Object key) {
                                final Permission permission = (Permission) key;
                                State result = super.get(permission);
                                if (result == null) {
                                    result = new State() {
                                        @Override
                                        public void setState(Boolean state) {
                                            super.setState(state);
                                            System.out.println("PermissionSchema.setState");
                                            if (state != null) {
                                                Collection<Permission> dependendPermissions = permission.getDependend();
                                                for (Permission dependendPermission : dependendPermissions) {
                                                    get(dependendPermission).setDependent(state);
                                                }
                                            }
                                        }

                                        @Override
                                        public void setDependent(boolean dependent) {
                                            super.setDependent(dependent);
                                                Collection<Permission> dependendPermissions = permission.getDependend();
                                                for (Permission dependendPermission : dependendPermissions) {
                                                    get(dependendPermission).setDependent(dependent);
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
            };
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

    public void setAssigned(Permission permission, PermissionGroup permissionGroup, Boolean state){
        getPermissionsAssignment().get(permissionGroup).get(permission).setState(state);
    }

    public class State {
        private Boolean state = false;
        private boolean dependent = false;

        public Boolean getState() {
            return state;
        }

        public void setState(Boolean state) {
            this.state = state;
        }

        public boolean isDependent() {
            return dependent;
        }

        public void setDependent(boolean dependent) {
            if (this.dependent != dependent) {
                this.dependent = dependent;
                if (!dependent) {
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

    }

    @Test
    public void test(){
        PermissionSchema schema = new PermissionSchema();
        schema.setAssigned(Permission.EDIT_ANY_POST, PermissionGroup.USER, true);
        assertTrue(schema.isAssigned(Permission.VIEW_POSTS, PermissionGroup.USER));
        assertTrue(schema.isAssigned(Permission.CREATE_POSTS, PermissionGroup.USER));
        assertTrue(schema.isAssigned(Permission.EDIT_ANY_POST, PermissionGroup.USER));
        assertFalse(schema.isAssigned(Permission.DELETE_ANY_POST, PermissionGroup.USER));

        schema.setAssigned(Permission.EDIT_ANY_POST, PermissionGroup.USER, null);
        assertTrue(schema.isAssigned(Permission.VIEW_POSTS, PermissionGroup.USER));
        assertTrue(schema.isAssigned(Permission.CREATE_POSTS, PermissionGroup.USER));
        assertTrue(schema.isAssigned(Permission.EDIT_ANY_POST, PermissionGroup.USER));
        assertFalse(schema.isAssigned(Permission.DELETE_ANY_POST, PermissionGroup.USER));

        schema.setAssigned(Permission.CREATE_POSTS, PermissionGroup.USER, null);
        assertTrue(schema.isAssigned(Permission.VIEW_POSTS, PermissionGroup.USER));
        assertTrue(schema.isAssigned(Permission.CREATE_POSTS, PermissionGroup.USER));
        assertTrue(schema.isAssigned(Permission.EDIT_ANY_POST, PermissionGroup.USER));
        assertFalse(schema.isAssigned(Permission.DELETE_ANY_POST, PermissionGroup.USER));

        schema.setAssigned(Permission.EDIT_ANY_POST, PermissionGroup.USER, false);
        assertFalse(schema.isAssigned(Permission.VIEW_POSTS, PermissionGroup.USER));
        assertFalse(schema.isAssigned(Permission.CREATE_POSTS, PermissionGroup.USER));
        assertFalse(schema.isAssigned(Permission.EDIT_ANY_POST, PermissionGroup.USER));
        assertFalse(schema.isAssigned(Permission.DELETE_ANY_POST, PermissionGroup.USER));

    }
}
