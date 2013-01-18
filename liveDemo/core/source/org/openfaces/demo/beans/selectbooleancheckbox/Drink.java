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

package org.openfaces.demo.beans.selectbooleancheckbox;

import static org.openfaces.demo.beans.selectbooleancheckbox.Drink.Ingredient.*;
import static org.openfaces.demo.beans.selectbooleancheckbox.Drink.IngredientGroup.ADDINS;
import static org.openfaces.demo.beans.selectbooleancheckbox.Drink.IngredientGroup.BASE;
import static org.openfaces.demo.beans.selectbooleancheckbox.Drink.IngredientGroup.SYRUPS;
import static org.openfaces.demo.beans.selectbooleancheckbox.Drink.IngredientGroup.TOPPING;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Natalia Zolochevska
 */
public class Drink extends HashMap<Object, Boolean> {

    private DrinkReceipt receipt;

    public Drink(DrinkReceipt receipt) {
        this.receipt = receipt;
    }
 
    public void orderGroup(IngredientGroup group) {
        Boolean currentValue = get(group);
        if (currentValue == null) {
            currentValue = false;
        }
        Collection<Ingredient> ingredients = receipt.getIngredientsByGroup().get(group);
        for (Ingredient ingredient : ingredients) {
            super.put(ingredient, !currentValue);
        }
    }

    public void order(Ingredient ingredient) {
        remove(ingredient.getGroup());
        Boolean currentValue = get(ingredient);
        if (currentValue == null) {
            currentValue = false;
        }
        super.put(ingredient, !currentValue);
    }


    @Override
    public Boolean put(Object key, Boolean value) {
        return value;
    }

    @Override
    public Boolean get(Object key) {
        if (key instanceof String) {
            try{
                key = Ingredient.valueOf((String)key);
            }catch (Exception e1){
                try{
                key = IngredientGroup.valueOf((String)key);
            }
            catch (Exception e2){
                return super.get(key);
            }
            }
        }
        if (key instanceof Ingredient) {
            if (!containsKey(key)) {
                super.put(key, false);
            }
            return super.get(key);
        }
        //if (key instanceof IngredientGroup)
        Boolean value = super.get(key);
        if (value != null) {
            return value;
        }
        Collection<Ingredient> ingredients = receipt.getIngredientsByGroup().get(key);
        boolean hasCheckedIngredients = false;
        boolean hasUncheckedIngredients = false;
        for (Ingredient ingredient : ingredients) {
            if (get(ingredient)) {
                hasCheckedIngredients = true;
            } else {
                hasUncheckedIngredients = true;
            }
            if (hasCheckedIngredients && hasUncheckedIngredients) {
                return null;
            }
        }
        return hasCheckedIngredients;
    }

    public enum DrinkReceipt {
        ESPRESSO("Espresso", SYRUP, MILK, SUGAR, HONEY, ICE),
        HOT_CHOCOLATE("Hot Chocolate", CREAM, GINGER, MOCHA, NUTMEG, SEASALT, VANILLA_POWDER, BANANA, FRAPPS),
        ICED_TEA("Iced Tea", LEMON, VANILLA, SUGAR, HONEY, WATER),
        CLEAN_WATER("Clean Water", LEMON_SYRUP, VANILLA_SYRUP, GINGER_SYRUP, SUGAR, ICE);

        private String label;
        private Collection<Ingredient> ingredients;
        private Map<IngredientGroup, Collection<Ingredient>> ingredientsByGroup;

        private DrinkReceipt(String label, Ingredient... ingredients) {
            this.label = label;
            this.ingredients = Arrays.asList(ingredients);
        }

        public String getLabel() {
            return label;
        }

        public Collection<Ingredient> getIngredients() {
            return ingredients;
        }

        public Map<IngredientGroup, Collection<Ingredient>> getIngredientsByGroup() {
            if (ingredientsByGroup == null) {
                ingredientsByGroup = new HashMap<IngredientGroup, Collection<Ingredient>>(){
                    @Override
                    public Collection<Ingredient> get(Object key) {
                        if (key instanceof String){
                            key = IngredientGroup.valueOf((String) key);
                        }
                        return super.get(key);
                    }
                };
                for (Ingredient ingredient : getIngredients()) {
                    IngredientGroup group = ingredient.getGroup();
                    Collection<Ingredient> ingredients = ingredientsByGroup.get(group);
                    if (ingredients == null) {
                        ingredients = new ArrayList<Ingredient>();
                        ingredientsByGroup.put(group, ingredients);
                    }
                    ingredients.add(ingredient);

                }
            }

            return ingredientsByGroup;
        }
    }


    public enum IngredientGroup {
        BASE("Base"),
        TOPPING("Topping"),
        ADDINS("Add-ins"),
        SYRUPS("Syrup");

        private String label;

        private IngredientGroup(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum Ingredient {
        CREAM("Cream", BASE),
        SYRUP("Syrup", BASE),
        MILK("Milk", BASE),
        GINGER("Ginger", TOPPING),
        MOCHA("Mocha", TOPPING),
        NUTMEG("Nutmeg", TOPPING),
        SEASALT("SeaSalt", TOPPING),
        VANILLA_POWDER("Vanilla Powder", TOPPING),
        LEMON_SYRUP("Lemon", SYRUPS),
        VANILLA_SYRUP("Vanilla", SYRUPS),
        GINGER_SYRUP("Ginger", SYRUPS),
        LEMON("Lemon", BASE),
        VANILLA("Vanilla", BASE),
        BANANA("Banana", ADDINS),
        FRAPPS("Frapps", ADDINS),
        SUGAR("Sugar", ADDINS),
        WATER("Water", ADDINS),
        HONEY("Honey", ADDINS),
        ICE("Ice", ADDINS);

        private String label;
        private IngredientGroup group;


        private Ingredient(String label, IngredientGroup group) {
            this.label = label;
            this.group = group;
        }

        public String getLabel() {
            return label;
        }

        public IngredientGroup getGroup() {
            return group;
        }

        /*public static Collection<Ingredient> valuesByGroup(IngredientGroup group) {

            Collection<Ingredient> result = new ArrayList<Ingredient>();
            for (Ingredient ingredient : values()) {
                if (group.equals(ingredient.getGroup())) {
                    result.add(ingredient);
                }
            }
            return result;
        }*/


    }


}
