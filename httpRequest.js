var HttpRequest = function() {
};

HttpRequest.prototype.execute = function(url, method, params, options, win, fail) {
    return Cordova.exec(win, fail, 'HttpRequest', 'execute', [url, method, params,options]);
};

if (!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.HttpRequest) {
    window.plugins.HttpRequest = new HttpRequest();
}