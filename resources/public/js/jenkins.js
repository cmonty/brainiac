var Jenkins = (function () {
  var template;

  return {
    initialize: function () {
      template = $('#jenkins-builds-template');
      Updater.addListener("jenkins-builds", this.render);
    },

    render: function (e, data) {
      var content = $.mustache(template.html(), data),
             name = data.name,
       fail_count = data.fail_count,
       html_class = fail_count > 0 ? "jenkins-failure" : "jenkins-success";

      plugin = $("div#" + name);
      plugin.html(content).addClass(html_class);

      if (fail_count > 0) {
        plugin.find("div.build_count").html(fail_count);
        plugin.find("div.build_text").html("failing build" + (data.fail_count == 1 ? "" : "s"));
      } else {
        plugin.find("div.build_text").html("all builds passing").addClass("passing");
      }
    }
  };
})();

$(function () {
  Jenkins.initialize();
});
