define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function AccountService() {

            var get = function (done, error, always) {
                utils.send(
                    "api/account",
                    "GET",
                    {},
                    done,
                    error,
                    always
                );
            };
            var updateAccount = function (account, done, error, always) {
                utils.send(
                    "api/account",
                    "PUT",
                    JSON.stringify(account),
                    done,
                    error,
                    always
                );
            };

            var updatePassword = function (oldPassword, newPassword, done, error, always) {
                utils.send(
                    "api/password",
                    "PUT",
                    JSON.stringify({oldPassword: oldPassword, newPassword: newPassword}),
                    done,
                    error,
                    always
                );
            };

            return {
                get: get,
                updateAccount: updateAccount,
                updatePassword: updatePassword
            }
        }

        return new AccountService();
    });
