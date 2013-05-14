/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

O$.MenuItemConsctructor = {

  setupAjaxHandlers: function (menuItemElement, menuItem){
    menuItemElement.onclick = function () {
      eval(menuItem.onclick);
      if (menuItem.render  || (menuItem.execute && menuItem.length>0 ) ) {
        if (menuItem.render == null){
          alert("'execute' attribute can't be specified without the 'render' attribute. Component id: " + popupMenu.id);
          return;
        }
        O$.ajax.request("", event, {
          render: menuItem.render,
          onajaxend : menuItem.onajaxend,
          execute : menuItem.execute,
          onerror : menuItem.onerror,
          onsuccess : menuItem.onsuccess,
          onajaxstart : menuItem.onajaxstart,
          immediate : menuItem.immediate,
          _action: menuItem.action
        });
      }
    };
  },

  renderMenuItemField: function (menuItem){
    var menuItemFieldSpan = document.createElement("span");
    menuItemFieldSpan.id = menuItem.id + "::caption";
    menuItemFieldSpan.innerHTML = menuItem.value;
    menuItemFieldSpan.className = O$.MenuItemConsctructor.defaultMenuItemsParams.DEFAULT_CONTENT_CLASS;
    return menuItemFieldSpan;
  },

  renderMenuItemImgSpan: function (menuItem){
    var menuItemImgSpan = document.createElement("span");
    menuItemImgSpan.id = menuItem.id + "::imagespan";
    menuItemImgSpan.className = O$.MenuItemConsctructor.defaultMenuItemsParams.DEFAULT_INDENT_CLASS;

    var subMenuImgFakeSpan = document.createElement("span");
    subMenuImgFakeSpan.id = menuItem.id + "::imagefakespan";
    subMenuImgFakeSpan.className = "o_menu_list_item_img_fakespan";
    menuItemImgSpan.appendChild(subMenuImgFakeSpan);

    var menuItemImg = document.createElement("img");
    menuItemImg.src = menuItem.iconUrl;
    menuItemImg.className = O$.MenuItemConsctructor.defaultMenuItemsParams.DEFAULT_IMG_CLASS;
    menuItemImg.id = menuItem.id + "::image";
    menuItemImgSpan.appendChild(menuItemImg);




    return menuItemImgSpan;
  },

  renderSubMenuImgSpan: function (menuItem){
    var subMenuSpan = document.createElement("span");
    subMenuSpan.id = menuItem.id + "::arrowspan";
    subMenuSpan.className = O$.MenuItemConsctructor.defaultMenuItemsParams.DEFAULT_ARROW_SPAN_CLASS;
    //class
    //IF popupMenuChild != null
    if (menuItem.menuId != null) {
      var subMenuImgFakeSpan = document.createElement("span");
      subMenuImgFakeSpan.id = menuItem.id + "::imagefakespan";
      subMenuImgFakeSpan.className = "o_menu_list_item_img_fakespan";

      var subMenuImg = document.createElement("img");
      subMenuImg.id = menuItem.id + "::arrow";
      //if (submenuImageUrl != null)
      subMenuImg.src = menuItem.iconUrl;
      subMenuImg.className = O$.MenuItemConsctructor.defaultMenuItemsParams.DEFAULT_IMG_CLASS;

      subMenuSpan.appendChild(subMenuImgFakeSpan);
      subMenuSpan.appendChild(subMenuImg);
    }
    return subMenuSpan;
  },

  renderMenuItems: function (popupMenu ,menuItemsList){
    var menuItemsResultList = [];
    menuItemsList.forEach(function(menuItem){
      var listElement;
      if (!menuItem.separator) {
        listElement = O$.MenuItemConsctructor.renderMenuItem(menuItem);
      } else {
        listElement = O$.MenuItemConsctructor.renderMenuSeparator(menuItem);
      }
      popupMenu.appendChild(listElement);
      menuItemsResultList.push(listElement);
    });
    return menuItemsResultList;
  },

  renderMenuItem : function (menuItem){
    var menuItemLiElement = document.createElement("li");
    menuItemLiElement.id = menuItem.id;

    var menuItemMainSpan = document.createElement("span");
    menuItemMainSpan.id = menuItem.id + "::commandLink";

    var menuItemImgSpan = O$.MenuItemConsctructor.renderMenuItemImgSpan(menuItem);

    var menuItemFieldSpan = O$.MenuItemConsctructor.renderMenuItemField(menuItem);

    menuItemMainSpan.appendChild(menuItemImgSpan);
    menuItemMainSpan.appendChild(menuItemFieldSpan);
    menuItemMainSpan.appendChild(O$.MenuItemConsctructor.renderSubMenuImgSpan(menuItem));

    menuItemLiElement._anchor = menuItemFieldSpan;
    O$.MenuItemConsctructor.setupAjaxHandlers(menuItemMainSpan,menuItem);


    menuItemLiElement.appendChild(menuItemMainSpan);

    menuItemLiElement.menuId = menuItem.menuId;

    //=======================================
    // adding additional attributes
    menuItemLiElement._properties = {};
    menuItemLiElement._properties.imgSelectedSrc = menuItem.selectedIconUrl;
    menuItemLiElement._properties.imgSrc = menuItem.iconUrl;
    menuItemLiElement._properties.disabledImgSelectedSrc = menuItem.selectedDisabledIconUrl;
    menuItemLiElement._properties.disabledImgSrc = menuItem.disabledIconUrl;
    //=======================================

    return menuItemLiElement;
  },

  renderMenuSeparator : function (separatorItem){
    var menuSeparatorLiElement = document.createElement("li");
    menuSeparatorLiElement.id = separatorItem.id;
    menuSeparatorLiElement.className = O$.MenuItemConsctructor.defaultMenuItemsParams.DEFAULT_LIST_SEPARATOR_CLASS;

    var menuSeparator = document.createElement("span");
    menuSeparator.id = separatorItem.id + "::separator";
    menuSeparator.className = O$.MenuItemConsctructor.defaultMenuItemsParams.DEFAULT_MENU_SEPARATOR_CLASS;

    menuSeparatorLiElement.appendChild(menuSeparator);
    return menuSeparatorLiElement;
  }

}