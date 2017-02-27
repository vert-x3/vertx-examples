Highcharts.setOptions({
  global: {
    useUTC: false
  }
});

var Chart = function(metric) {
  $("#container").append('<div id="' + metric + '" style="height: 150px; min-width: 310px"></div>');
  var series = {};
  var widget = new Highcharts.Chart({
    chart: {
      renderTo: metric,
      type: 'spline',
      animation: Highcharts.svg, // don't animate in old IE
      marginRight: 10
    },
    title: {
      text: metric
    },
    xAxis: {
      type: 'datetime',
      tickPixelInterval: 150
    },
    yAxis: {
      title: {
        text: 'Value'
      },
      plotLines: [{
        value: 0,
        width: 1,
        color: '#808080'
      }]
    },
    legend: {
      enabled: false
    },
    exporting: {
      enabled: false
    },
    series: []
  });

  return {

    getSerie : function(id, init) {
      var serie = series[id];
      if (serie == null) {
        var data = init();
        serie = widget.addSeries({
          name: id,
          data: data
        });
        series[id] = serie;
      }
      return serie;
    },

    removeSeries: function(filter) {
      for (j in series) {
        if (series.hasOwnProperty(j)) {
          if (filter(j)) {
            console.log("Deleting serie " + j);
            series[j].remove();
            delete series[j];
          }
        }
      }
    },

    redraw: function() {
      widget.redraw();
    }
  };
};

var Charts = function() {
  var charts = {};
  return {
    getChart: function(metric) {
      var chart = charts[metric];
      if (chart == null) {
        chart = new Chart(metric);
        charts[metric] = chart;
      }
      return chart;
    },
    redraw: function() {
      for (var i in charts) {
        if (charts.hasOwnProperty(i)) {
          charts[i].redraw();
        }
      }
    },
    removeSeries: function(filter) {
      for (var i in charts) {
        if (charts.hasOwnProperty(i)) {
          var chart = charts[i];
          chart.removeSeries(filter);
        }
      }
    }
  };
};
