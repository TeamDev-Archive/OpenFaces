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

package org.openfaces.testapp.support.QKS646;

/**
 * @author Tatyana Matveyeva
 */

import org.openfaces.util.Faces;

import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class QKS646 implements Serializable {
    private String selectedNodeId;
    private Integer level;

    public List cars = new ArrayList();

    public List details = new ArrayList();

    public QKS646() {
        Passenger p1 = new Passenger("p1");
        Passenger p2 = new Passenger("p2");

        Pet pet1 = new Pet("pet1");
        Pet pet2 = new Pet("pet2");

        p1.pets.add(pet1);
        p1.pets.add(pet2);

        p2.pets.add(pet1);
        p2.pets.add(pet2);


        Car car1 = new Car("honda");
        car1.passengers.add(p1);
        car1.passengers.add(p2);

        Car car2 = new Car("nissan");
        car2.passengers.add(p1);
        car2.passengers.add(p2);

        cars.add(car1);
        cars.add(car2);
    }

    public List getChildren() {
        Object node = Faces.var("node");


        if (node == null)
            return cars;
        else if (node instanceof Car)
            return ((Car) node).passengers;
        else if (node instanceof Passenger)
            return ((Passenger) node).pets;
        else
            return null;
    }


    public void viewDetails(ActionEvent ae) {
        if (level == 0)
            this.details = cars;
        else if (level == 1) {
            this.details = ((Car) cars.get(0)).passengers;

        } else
            this.details = new ArrayList();


    }

    public String getSelectedNodeId() {
        return selectedNodeId;
    }

    public void setSelectedNodeId(String selectedNodeId) {
        this.selectedNodeId = selectedNodeId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    public List getDetails() {
        return details;
    }

    public void setDetails(List details) {
        this.details = details;
    }


    public class Car implements Serializable {
        public List passengers = new ArrayList();
        public String name;

        public Car(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result
                    + ((passengers == null) ? 0 : passengers.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Car other = (Car) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (passengers == null) {
                if (other.passengers != null)
                    return false;
            } else if (!passengers.equals(other.passengers))
                return false;
            return true;
        }

        public List getPassengers() {
            return passengers;
        }

        public void setPassengers(List passengers) {
            this.passengers = passengers;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }

    public class Passenger implements Serializable {
        public List pets = new ArrayList();
        public String name;

        public Passenger(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((pets == null) ? 0 : pets.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Passenger other = (Passenger) obj;
            if (name == null) {
                {
                    if (other.name != null) {
                        return false;
                    }
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            if (pets == null) {
                {
                    if (other.pets != null) {
                        return false;
                    }
                }
            } else if (!pets.equals(other.pets)) {
                return false;
            }
            return true;
        }

        public List getPets() {
            return pets;
        }

        public void setPets(List pets) {
            this.pets = pets;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }

    public class Pet implements Serializable {
        public String name;

        public Pet(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Pet other = (Pet) obj;
            if (name == null) {
                {
                    if (other.name != null) {
                        return false;
                    }
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            return true;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}