var exec = require('cordova/exec');

var SunmiPrinter = {
    printText: function(text, successCallback, errorCallback) {
        exec(successCallback, errorCallback, "SunmiPrinter", "printText", [text]);
    }
};

module.exports = SunmiPrinter;
