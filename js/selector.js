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
		selectors[key] = !selectors[key];
		setActiveSelector(key)
	}
}

function getDefaultSelectors() {
	return {
		trace : true,
		debug : true,
		info : true,
		warning : true,
		error : true,
		level : true,
		date : true,
		time : true,
		millisec : true,
		message : true,
		clazz : true
	};
}

window.onload = function(e) {
	var cookie1 = readCookie("selectors");
	var selectors = cookie1 ? cookie1 : getDefaultSelectors();
	setActiveSelectors(selectors);
}

window.onunload = function(e) {
	createCookie("selectors", selectors, 365);
}

var cookie1 = readCookie("selectors");
var selectors = cookie1 ? cookie1 : getDefaultSelectors();
setActiveSelectors(selectors);