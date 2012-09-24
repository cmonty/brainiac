var Graphite = (function () {

  return {
    initialize: function () {
      Updater.addListener("graphite", this.render);
    },

    render: function (e, payload) {
      $('#graphite').html('')
      var paper = Raphael(document.getElementById("graphite"), 600, 500)
      paper.linechart(0, 0, 600, 500, payload.data["valuesx"], payload.data["valuesy"], {smooth:true, shade: true})
    }
  };
})();

$(function () {
  Graphite.initialize();
});
