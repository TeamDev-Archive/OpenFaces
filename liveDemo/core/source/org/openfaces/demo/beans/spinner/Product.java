/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.spinner;

/**
 * @author Alexei Tymchenko
 */
public class Product {
    private String name;
    private float price;
    private int quantity;
    private int inStock;

    public Product(String name, float price, int quantity, int inStock) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.inStock = inStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity > inStock) {
            quantity = inStock;
        }
        if(quantity < 0) {
            quantity = 0;
        }
        this.quantity = quantity;
    }

    public float getProductTotalCost() {
        return quantity * price;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getLeftInStock() {
        return inStock - quantity;
    }
}
