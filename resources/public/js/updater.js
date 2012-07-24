var Updater = (function() {
  var socket, heartbeat,
  registeredPlugins = new Array();

  return {
    connect: function() {
      socket.send("devs");
    },

    socket: function() {
      new WebSocket("ws://" + location.host + "/async");
    },

    renderTemplate: function(name, data) {
      var template = $("#" + name + "-template");
      var content = $.mustache(template.html(), data)
      if ($("div#" + name).length == 0) {
        var plugin = $('<div/>', {'id': name}).html(content);
        if(data.html_class) {
          plugin.attr('class', data.html_class);
        }
        $("#plugins").append(plugin);
        Updater.attachWidget(template, plugin);
      } else {
        var plugin = $("div#" + name)
        plugin.html(content);
        if(data.html_class) {
          plugin.attr('class', data.html_class);
        }
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
      var children = plugin.children("[data-widget]");
      var widget = children.data('widget');
      if (widget) {
        Widgets[widget](plugin);
      }
    },

    addListener: function(pluginName, callback) {
      registeredPlugins.push(pluginName);
      $('#' + pluginName).bind(pluginName + ".updated", callback);
    },

    update: function(event) {
      var data = JSON.parse(event.data),
          name = data.name;

      if ($.inArray(name, registeredPlugins) > -1) {
        $("#" + name).trigger(name + ".updated", data);
      } else {
        Updater.renderTemplate(data.name, data);
      }
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
