define(["app/utils/utils"], function(utils) {
    "use strict";
    function AccountService(){

        var get = function(done, error, always) {
            utils.send(
                "api/account",
                "GET",
                {},
                done,
                error,
                always
            );
        };
        var update = function(account, done, error, always) {
            utils.send(
                "api/account",
                "PUT",
                JSON.stringify(account),
                done,
                error,
                always
            );
        };

        return {
            get: get,
            update: update
        }
    }

    return new AccountService();
});
