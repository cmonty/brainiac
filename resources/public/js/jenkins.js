var Jenkins = (function () {
  var template;

  return {
    initialize: function () {
      template = $('#jenkins-template');
      Updater.addListener("jenkins-builds", this.render);
    },

    render: function (e, data) {
      var content = $.mustache(template.html(), data),
             name = data.name,
       html_class = data.html_class;

      if ($("div#" + name).length == 0) {
        var plugin = $('<div/>', {'id': name, 'class': html_class}).html(content);
        $("#plugins").append(plugin);
      } else {
        $("div#" + name).attr('class', html_class).html(content);
      }
    }
  };
})();

$(function () {
  Jenkins.initialize();
});
