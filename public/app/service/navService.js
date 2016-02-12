define(["app/utils/utils", 'knockout'], function (utils, ko) {
    "use strict";
    function NavService() {

        var navigateTo = function (page) {
            utils.goTo(page);
        };

        var mainPage = function (roles) {
            if (!roles) {
                var context = ko.contextFor($("body")[0]);
                roles = context.$data.roles();
            }

            if (!roles || !roles.length) return utils.goTo("login");

            switch (roles[0].name) {
                case "SYS_ADMIN":
                    utils.goTo("clients");
                    break;
                case "ADMIN":
                    utils.goTo("employees");
                    break;
                case "DISPATCHER":
                    utils.goTo("packingList");
                    break;
                case "MANAGER":
                    utils.goTo("packingLists");
                    break;
                default:
                    utils.goTo("error");
            }

        };

        var catchError = function (data) {
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
