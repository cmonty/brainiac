function flipPanel($elem) {
  $elem.toggleClass('flip');
  setTimeout(function() {
    flipPanel($elem);
  }, 12000);
}

function scheduleFlip($elem, initialDelay) {
  $elem.delay(initialDelay).queue(function() {
    flipPanel($elem);
  });
}

$(document).ready(function() {
  $('.panel').each(function(index, elem) {
    $elem = $(elem);
    if($elem.children('div').length > 1) {
      scheduleFlip($(elem), index * 2000);
    }
  });
});
