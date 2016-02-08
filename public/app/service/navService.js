define(["app/utils/utils"], function(utils) {
    "use strict";
    function NavService(){

        var navigateTo = function(page) {
            utils.goTo(page);
        };

        var mainPage = function() {
            utils.goTo("account");
        };

        var catchError = function(data) {
            switch (data.status) {
                case 403:
                    utils.goTo("login");
                    break;
                default:
                    utils.goTo("error");
            }
        };

        return {
            navigateTo: navigateTo,
            mainPage: mainPage,
            catchError: catchError
        }
    }

    return new NavService();
});
