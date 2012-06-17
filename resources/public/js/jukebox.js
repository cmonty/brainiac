var Jukebox = (function () {
  var template;

  return {
    initialize: function () {
      template = $('#jukebox-template');
      Updater.addListener("jukebox", this.render);
    },

    render: function (e, data) {
      var content = $.mustache(template.html(), data),
             name = data.name;
       $("div#" + name).
         css('background-size', '100% auto').
         css('background-position', '50% 50%').
         css('backgroundImage', "url(" + data.artwork + ")").
         html(content);
    }
  };
})();

$(function () {
  Jukebox.initialize();
});
