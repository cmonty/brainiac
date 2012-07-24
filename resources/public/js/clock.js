var Clock = (function () {
  // var DAYS_OF_WEEK = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
  // var MONTHS = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

  var DAYS_OF_WEEK = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
  var MONTHS = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];

  var template;

  var current_time = 0;
  var timezones = [];

  return {
    render: function () {
      var local_date = new Date(current_time);
      var local_offset = local_date.getTimezoneOffset();

      var timestamps = [];
      $.each(timezones, function (index, timezone) {
        var date = new Date(current_time + timezone.offset + local_offset * 60000);

        var minute = new String(date.getMinutes());
        if (minute.length == 1) minute = "0" + minute;

        var hour = new String(date.getHours());
        if (hour.length == 1) hour = "0" + hour;

        timestamps.push({
          hour: hour,
          minute: minute,
          seconds: date.getSeconds(),
          name: timezone.name
        });
      });

      var time_data = {
        day_of_week: DAYS_OF_WEEK[local_date.getDay()],
        month: MONTHS[local_date.getMonth()],
        day: local_date.getDate(),
        times: timestamps
      };

      var content = $.mustache(template.html(), time_data);
      $("div#clock").html(content);
    },

    updateTime: function() {
      current_time += 1000;
      Clock.render();
    },

    initialize: function () {
      template = $('#clock-template');
      Updater.addListener("clock", Clock.update);
      window.setInterval(Clock.updateTime, 1000);
    },

    update: function(e, data) {
      current_time = data.time_utc;
      timezones = data.timezones;
      Clock.render();
    }
  };
})();

$(function () {
  Clock.initialize();
});
