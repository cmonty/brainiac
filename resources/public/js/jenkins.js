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
       fail_count = data.fail_count;

       plugin = $("div#" + name);
       if (fail_count > 0) {
         plugin.html(content).addClass("failing");
         plugin.find("div.build_count").html(fail_count);
         plugin.find("div.build_text").html("failing build" + (fail_count == 1 ? "" : "s"));
       } else {
         plugin.html(content).removeClass("failing");
         plugin.find("div.build_text").html("all builds passing").addClass("passing");
       }
    }
  };
})();

$(function () {
  Jenkins.initialize();
});
