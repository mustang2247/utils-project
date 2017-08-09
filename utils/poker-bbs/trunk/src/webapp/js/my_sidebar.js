

$('.toogle').on('click', function() {
	$('.body').toggleClass('slide');
	$('.sidebar').toggleClass('show');
	$('.toogle i').toggleClass('fa-chevron-left fa-chevron-right')
});
$('#accordion').on('click', '.single-item', function() {
	localStorage.removeItem('collapseID');
	localStorage.removeItem('activeLink')
});
$('#accordion').on('click', '.list-group-item', function() {
	localStorage.setItem('collapseID', $(this).parents().eq(1).attr('id'));
	localStorage.setItem('activeLink', this)
});
if (localStorage.collapseID) {
	$('.panel-collapse').removeClass('in');
	$('#' + localStorage.collapseID).addClass('in')
} else {
	$('.panel-collapse').removeClass('in')
}
if (localStorage.activeLink) {
	$('a[href="' + localStorage.activeLink.split('/').pop() + '"]').addClass('solso-active')
}