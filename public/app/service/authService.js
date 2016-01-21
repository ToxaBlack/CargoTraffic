define(["app/utils/utils"], function (utils) {
    "use strict";
    function NavService() {


        var login = function (user, password, done, error, always) {
            utils.send(
                "api/login",
                "POST",
                JSON.stringify({user: user, password: password}),
                done,
                error,
                always
            );
        };

        var logout = function (done, error, always) {
            utils.send(
                "api/logout",
                "POST",
                JSON.stringify({}),
                done,
                error,
                always
            );
        };

        return {
            login: login,
            logout: logout
        }
    }

    return new NavService();
});