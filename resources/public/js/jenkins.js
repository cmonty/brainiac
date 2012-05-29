var Jenkins = (function () {
  var template;

  return {
    initialize: function () {
      template = $('#jenkins-template');
      Updater.addListener("jenkins-builds", this.render);
    },

    render: function (e, data) {
      var content = $.mustache(template.html(), data),
             name = data.name;

      if ($("div#" + name).length == 0) {
        var plugin = $('<div/>', {'id': name}).html(content);
        $("#plugins").append(plugin);
      } else {
        $("div#" + name).html(content);
      }
    }
  };
})();

$(function () {
  Jenkins.initialize();
});
