var Updater = (function() {
  var socket;

  return {
    connect: function() {
      socket.send("subscribe");
    },

    updatePlugin: function(name, content) {
      if ($("div#" + name).length == 0) {
        var plugin = $('<div/>', {'id': name, 'class': 'plugin'}).html(content);
        $("#plugins").append(plugin);
      } else {
        $("div#" + name).html(content);
      }
    },

    update: function(event) {
      var data = JSON.parse(event.data);
      var template = $("#" + data.type + "-template").html();
      Updater.updatePlugin(data.name, $.mustache(template, data));
    },

    subscribe: function() {
      socket = new WebSocket("ws://" + location.host + "/async");
      socket.onmessage = this.update;
      socket.onopen = this.connect;
    }
  };
})();

$(function() {
  Updater.subscribe();
});
