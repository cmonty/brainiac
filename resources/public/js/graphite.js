var Graphite = (function () {

  return {
    initialize: function () {
      Updater.addListener("graphite", this.render);
    },

    render: function (e, payload) {
      var template = $("#graphite-template");
      var content = $.mustache(template.html(), payload)

      $("#graphite").html(content)
      $('#graphite-graph', content).html('')

      var paper = Raphael(document.getElementById("graphite-graph"), 575, 450),
      chart = paper.linechart(
        20,
        0,
        575,
        450,
        payload.data["valuesx"],
        [
          payload.data["valuesy"],
          [0],
        ],
        {axis: "0 0 0 1", colors: ["orange", "orange", "orange"], axisystep:10, smooth:true, shade:true}
      )

      for( var i = 0, l = chart.axis.length; i < l; i++ ) {
        chart.axis[i].attr("stroke", "orange");

        var axisItems = chart.axis[i].text.items
        for( var ii = 0, ll = axisItems.length; ii < ll; ii++ ) {
          axisItems[ii].attr("fill", "orange");
        }
      }
    }
  };
})();

$(function () {
  Graphite.initialize();
});
