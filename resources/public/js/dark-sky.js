var DarkSky = (function () {
  var template;

  return {
    initialize: function () {
      template = $('#dark-sky-template');
      Updater.addListener("dark-sky", this.render);
    },

    render: function (e, payload) {
      var content = $.mustache(template.html(), payload),
             name = payload.name,
       html_class = payload.data["current-summary"].toLowerCase().replace(/\s/, "-");

        $("div#" + name).attr('class', html_class).html(content);
    }
  };
})();

$(function () {
  DarkSky.initialize();
});
