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

import org.openfaces.demo.beans.util.FacesUtils;

import javax.faces.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class DrinkOrder {

    public HashMap<Drink.DrinkReceipt, Drink> drinks = new HashMap<Drink.DrinkReceipt, Drink>();

    private Map<Drink.DrinkReceipt, Boolean> ordered = new HashMap<Drink.DrinkReceipt, Boolean>() {
        @Override
        public Boolean get(Object key) {
            return drinks.get(key) != null;
        }
    };

    public HashMap<Drink.DrinkReceipt, Drink> getDrinks() {
        return drinks;
    }

    public Map<Drink.DrinkReceipt, Boolean> getOrdered() {
        return ordered;
    }

    public void orderDrink(ActionEvent actionEvent) {
        Drink.DrinkReceipt drinkReceipt =FacesUtils.getEventParameter(actionEvent, "drinkReceipt");
        if (drinks.get(drinkReceipt) == null) {
            drinks.put(drinkReceipt, new Drink(drinkReceipt));
        } else {
            drinks.remove(drinkReceipt);
        }
    }

    public Map<String, Drink.IngredientGroup> getIngredientGroup(){
        return new HashMap<String, Drink.IngredientGroup>(){
            @Override
            public Drink.IngredientGroup get(Object key) {
                return Drink.IngredientGroup.valueOf(String.valueOf(key));
            }
        };
    }

      public Map<String, Drink.Ingredient> getIngredient(){
        return new HashMap<String, Drink.Ingredient>(){
            @Override
            public Drink.Ingredient get(Object key) {
                return Drink.Ingredient.valueOf(String.valueOf(key));
            }
        };
    }

     public Map<String, Drink.DrinkReceipt> getDrinkReceipt(){
        return new HashMap<String, Drink.DrinkReceipt>(){
            @Override
            public Drink.DrinkReceipt get(Object key) {
                return Drink.DrinkReceipt.valueOf(String.valueOf(key));
            }
        };
    }

    public void orderIngredient(ActionEvent actionEvent) {
        Drink.DrinkReceipt drinkReceipt =FacesUtils.getEventParameter(actionEvent, "drinkReceipt");
        Drink.Ingredient ingredient =FacesUtils.getEventParameter(actionEvent, "ingredient");
        Drink drink = drinks.get(drinkReceipt);
        if (drink != null) {
            drink.order(ingredient);
        }
    }

    public void orderGroup(ActionEvent actionEvent) {
        Drink.DrinkReceipt drinkReceipt =FacesUtils.getEventParameter(actionEvent, "drinkReceipt");
        Drink.IngredientGroup group  = FacesUtils.getEventParameter(actionEvent, "ingredientGroup");
        Drink drink = drinks.get(drinkReceipt);
        if (drink != null) {
            drink.orderGroup(group);
        }
    }
}

