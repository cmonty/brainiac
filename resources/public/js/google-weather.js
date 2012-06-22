var GoogleWeather = (function () {
  var template;

  return {
    initialize: function () {
      template = $('#google-weather-template');
      Updater.addListener("google-weather", this.render);
    },

    render: function (e, payload) {
      var content = $.mustache(template.html(), payload),
             name = payload.name,
       html_class = payload.data["current-conditions"].toLowerCase().replace(/\s/, "-");

        $("div#" + name).attr('class', html_class).html(content);
    }
  };
})();

$(function () {
  GoogleWeather.initialize();
});
