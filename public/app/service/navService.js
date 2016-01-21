define(["app/utils/utils"], function(utils) {
    "use strict";
    function AuthService(){

        var navigateTo = function(page) {
            utils.goTo(page);
        };

        var mainPage = function() {
            utils.goTo("account");
        };

        return {
            navigateTo: navigateTo,
            mainPage: mainPage
        }
    }

    return new AuthService();
});
