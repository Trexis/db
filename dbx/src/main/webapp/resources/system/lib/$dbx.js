if (!window.$dbx) window.$dbx = {};

$dbx.renderPage = function(){
	document.write($dbx.model.application.name);
};

$dbx.renderPage.prototype 	= {};
$dbx.renderPage.prototype.init = function(){

};

