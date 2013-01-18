/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.spinner;

import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexei Tymchenko
 */
public class SpinnerBean {
    private List<Product> products = new ArrayList<Product>();
    private float totalCost;
    private boolean pricesEditable;

    public SpinnerBean() {
        products.add(new Product("Ink Pen Black", 7.99f, 1, 80));
        products.add(new Product("Leather Blotter", 15.49f, 1, 25));
        products.add(new Product("Ink Cartridge Blue ", 9.00f, 1, 10));
        products.add(new Product("Ink Cartridge Black ", 8.00f, 0, 12));
        products.add(new Product("Glass Inkwell Red", 80.00f, 1, 10));
        products.add(new Product("Glass Inkwell Blue", 80.00f, 0, 7));
        products.add(new Product("Marker Blue", 7.99f, 2, 10));
        products.add(new Product("Pen Set", 29.99f, 2, 27));
        calculateTotalCost(null);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void calculateTotalCost(ActionEvent actionEvent) {
        totalCost = 0;
        for (Product product : products) {
            totalCost += product.getPrice() * product.getQuantity();
        }
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public boolean isPricesEditable() {
        return pricesEditable;
    }

    public void setPricesEditable(boolean pricesEditable) {
        this.pricesEditable = pricesEditable;
    }
}
