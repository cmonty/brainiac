$(document).ready(function() {
  $('.panel').each(function(index, elem) {
    if($(elem).children('div').length > 1) {
      $(elem).delay(index * 2000).queue(function () {
        window.setInterval(function() {
          $(elem).toggleClass('flip');
        }, 12000);
      });
    }
  });
});
