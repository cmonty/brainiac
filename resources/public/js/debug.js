var Debug = (function() {
  return {
    init: function () {
      $('html').keyup(this.debugMode);
    },

    debugMode: function(e) {
      if (e.keyCode !== 68) { return }

      $('body').toggleClass('nocursor');
    }
  };
})();

$(function() {
  Debug.init();
});
