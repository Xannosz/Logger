function setActiveStyleSheet(title) {
	var i, a, main;
	for (i = 0; (a = document.getElementsByTagName("link")[i]); i++) {
		if (a.getAttribute("rel").indexOf("style") != -1
				&& a.getAttribute("title")) {
			a.disabled = true;
			if (a.getAttribute("title") == title)
				a.disabled = false;
		}
	}
}

function getActiveStyleSheet() {
	var i, a;
	for (i = 0; (a = document.getElementsByTagName("link")[i]); i++) {
		if (a.getAttribute("rel").indexOf("style") != -1
				&& a.getAttribute("title") && !a.disabled)
			return a.getAttribute("title");
	}
	return null;
}

function getPreferredStyleSheet() {
	var i, a;
	for (i = 0; (a = document.getElementsByTagName("link")[i]); i++) {
		if (a.getAttribute("rel").indexOf("style") != -1
				&& a.getAttribute("rel").indexOf("alt") == -1
				&& a.getAttribute("title"))
			return a.getAttribute("title");
	}
	return null;
}

function createCookie(name, value, days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		var expires = "; expires=" + date.toGMTString();
	} else
		expires = "";
	document.cookie = name + "=" + value + expires + "; path=/";
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ')
			c = c.substring(1, c.length);
		if (c.indexOf(nameEQ) == 0)
			return c.substring(nameEQ.length, c.length);
	}
	return null;
}

window.onload = function(e) {
	var cookie = readCookie("style");
	var title = cookie ? cookie : getPreferredStyleSheet();
	setActiveStyleSheet(title);

	var selectors = getDefaultSelectors();
	var cookieTr = readCookie("trace");
	selectors["trace"] = (cookieTr == "true") ? true : false;
	var cookieDb = readCookie("debug");
	selectors["debug"] = (cookieDb == "true") ? true : false;
	var cookieIf = readCookie("info");
	selectors["info"] = (cookieIf == "true") ? true : false;
	var cookieWa = readCookie("warning");
	selectors["warning"] = (cookieWa == "true") ? true : false;
	var cookieEr = readCookie("error");
	selectors["error"] = (cookieEr == "true") ? true : false;
	var cookieLe = readCookie("level");
	selectors["level"] = (cookieLe == "true") ? true : false;
	var cookieDa = readCookie("date");
	selectors["date"] = (cookieDa == "true") ? true : false;
	var cookieTi = readCookie("time");
	selectors["time"] = (cookieTi == "true") ? true : false;
	var cookieMi = readCookie("millisec");
	selectors["millisec"] = (cookieMi == "true") ? true : false;
	var cookieMe = readCookie("message");
	selectors["message"] = (cookieMe == "true") ? true : false;
	var cookieCl = readCookie("clazz");
	selectors["clazz"] = (cookieCl == "true") ? true : false;
	setActiveSelectors(selectors);
}

window.onunload = function(e) {
	var title = getActiveStyleSheet();
	createCookie("style", title, 365);
	createCookie("trace", selectors["trace"] ? "true" : "false", 365);
	createCookie("debug", selectors["debug"] ? "true" : "false", 365);
	createCookie("info", selectors["info"] ? "true" : "false", 365);
	createCookie("warning", selectors["warning"] ? "true" : "false", 365);
	createCookie("error", selectors["error"] ? "true" : "false", 365);
	createCookie("level", selectors["level"] ? "true" : "false", 365);
	createCookie("date", selectors["date"] ? "true" : "false", 365);
	createCookie("time", selectors["time"] ? "true" : "false", 365);
	createCookie("millisec", selectors["millisec"] ? "true" : "false", 365);
	createCookie("message", selectors["message"] ? "true" : "false", 365);
	createCookie("clazz", selectors["clazz"] ? "true" : "false", 365);
}

function setActiveSelector(selector) {
	var i, a;
	if (selectors[selector]) {
		selectors[selector] = false;
		for (i = 0; (a = document.getElementsByClassName(selector)[i]); i++) {
			a.classList.remove("deactivate");
		}
	} else {
		selectors[selector] = true;
		for (i = 0; (a = document.getElementsByClassName(selector)[i]); i++) {
			a.classList.add("deactivate");
		}
	}
}

function setActiveSelectors(selectors) {
	var keys = Object.keys(selectors);
	for (i = 0; (key = keys[i]); i++) {
		setActiveSelector(key);
	}
}

function getDefaultSelectors() {
	return {
		trace : true,
		debug : false,
		info : false,
		warning : false,
		error : false,
		level : false,
		date : true,
		time : false,
		millisec : true,
		message : false,
		clazz : false
	};
}

var cookie = readCookie("style");
var title = cookie ? cookie : getPreferredStyleSheet();
setActiveStyleSheet(title);

var selectors = getDefaultSelectors();
var cookieTr = readCookie("trace");
selectors["trace"] = (cookieTr == "true") ? true : false;
var cookieDb = readCookie("debug");
selectors["debug"] = (cookieDb == "true") ? true : false;
var cookieIf = readCookie("info");
selectors["info"] = (cookieIf == "true") ? true : false;
var cookieWa = readCookie("warning");
selectors["warning"] = (cookieWa == "true") ? true : false;
var cookieEr = readCookie("error");
selectors["error"] = (cookieEr == "true") ? true : false;
var cookieLe = readCookie("level");
selectors["level"] = (cookieLe == "true") ? true : false;
var cookieDa = readCookie("date");
selectors["date"] = (cookieDa == "true") ? true : false;
var cookieTi = readCookie("time");
selectors["time"] = (cookieTi == "true") ? true : false;
var cookieMi = readCookie("millisec");
selectors["millisec"] = (cookieMi == "true") ? true : false;
var cookieMe = readCookie("message");
selectors["message"] = (cookieMe == "true") ? true : false;
var cookieCl = readCookie("clazz");
selectors["clazz"] = (cookieCl == "true") ? true : false;
setActiveSelectors(selectors);