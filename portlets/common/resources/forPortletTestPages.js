function printInfo(textToOutput, divID, add) {
  var empty = getControl(divID);
  addListItemInnerHTML(empty, textToOutput, add);
  var children = empty.childNodes;
  for (var i = 0; i < children.length; i++) {
    children[i].className = 'programmed';
  }
  empty.style.color = "green";
}

function addListItemInnerHTML(el, text, add) {
  if (add) {
    el.innerHTML += "<div class='programmed'></div>";
    el.innerHTML += "<div class='programmed'>" + text + "</div>";
  } else {
    el.innerHTML = "<div class='programmed'>" + text + "</div>";

    el.style.borderLeftcolor = '';
  }
}

function getControl(id) {
  return document.getElementById(id);
}

