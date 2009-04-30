function setStyle(obj, style, value) {
  getRef(obj).style[style] = value;
}

function getRef(obj) {
  return (typeof obj == "string") ?
         document.getElementById(obj) : obj;
}

function setStyleClass(obj, name) {
  if (name == "mouseOverActiveDays")
    obj.style.background = "#888888";
  else
    obj.style.background = "#bbccdd";
  //  obj.className = name;
}
