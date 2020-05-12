var LiveOcrPlugin = {
    recognizeText: function (regex, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "LiveOcrPlugin", "recognizeText", [regex]);
    },

    loadLanguage: function (language, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "LiveOcrPlugin", "loadLanguage", [language]);
    }
};
module.exports = LiveOcrPlugin;
