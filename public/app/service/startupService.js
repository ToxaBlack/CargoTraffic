define(["app/utils/utils"], function(utils) {
    "use strict";
    function StartupService(){

        var roles = function(done, error, always) {
            utils.send(
                "api/roles",
                "GET",
                {},
                done,
                error,
                always
            );
        };


        return {
            roles: roles
        }
    }

    return new StartupService();
});