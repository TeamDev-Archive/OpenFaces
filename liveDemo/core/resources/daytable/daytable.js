function updateLayout() {
  var dayTable = document.getElementById('form:dayTable');
  if (dayTable != null) {
    if (dayTable.updateLayout) {
      dayTable.updateLayout();
    } else {
      setTimeout(updateLayout, 1500);
    }
  }
}