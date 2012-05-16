var Updater = (function() {
  var socket, heartbeat;

  return {
    connect: function() {
      socket.send("devs");
    },

    socket: function() {
      new WebSocket("ws://" + location.host + "/async");
    },

    updatePlugin: function(name, data) {
      var template = $("#" + data.type + "-template");
      var content = $.mustache(template.html(), data)
      if ($("div#" + name).length == 0) {
        var plugin = $('<div/>', {'id': name}).html(content);
        $("#plugins").append(plugin);
        Updater.attachWidget(template, plugin);
      } else {
        var plugin = $("div#" + name)
        plugin.html(content);
        Updater.attachWidget(template, plugin);
      }
    },

    classFor: function(template) {
      var classes = "plugin";
      if ($(template.data('class'))) {
        classes = classes + " " + template.data('class');
      }
      return classes;
    },

    attachWidget: function(template, plugin) {
      var widget = template.data('widget');
      if (widget) {
        Widgets[widget](plugin);
      }
    },

    update: function(event) {
      var data = JSON.parse(event.data);
      Updater.updatePlugin(data.name, data);
    },

    startTimer: function() {
      var self = this;

      clearInterval(heartbeat);

      heartbeat = setInterval(function() {
        console.log("Closing stale websocket");
        self.subscribe();
      }, 30000);
    },

    subscribe: function() {
      var self = this;
      socket = new WebSocket("ws://" + location.host + "/async");
      socket.onmessage = function (e) {
        self.update(e);
        self.startTimer();
      }

      socket.onopen = function () {
        console.log("Socket opened");
        self.startTimer();
        self.connect();
      }

      socket.onerror = function () {
        console.log("Error connecting to websocket");
      }
    }
  };
})();

$(function() {
  Updater.subscribe();
});
