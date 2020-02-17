if (Cookies.get('night-mode') === undefined) {

  Cookies.set('night-mode', 'true')

}
if (Cookies.get('night-mode') === 'true') {

  $('body').addClass('night-mode');
  
} else {
  
  $('body').removeClass('night-mode');

}

function toggleNightMode() {

  const $body = $('body');
  $body.toggleClass('night-mode');

  Cookies.set('night-mode', $body.hasClass('night-mode').toString());

}
