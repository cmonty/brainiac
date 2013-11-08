var Kudotron = (function () {
  var template;

  return {
    initialize: function () {
      template = $('#kudotron-template');
      Updater.addListener("kudotron", this.render);
    },

    render: function (e, payload) {
      var content = $.mustache(template.html(), payload),
             name = payload.name
        $("div#" + name).append(content);
    }
  };
})();

$(function () {
  Kudotron.initialize();
});
