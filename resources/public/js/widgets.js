var Widgets = (function() {
  return {
    ticker: function(div) {
      div.children('.ticker').vTicker({
        showItems: 1
      });
    }
  };
})();
