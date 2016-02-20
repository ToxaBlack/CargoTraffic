define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function DriverService() {

            var getProducts = function (done, error) {
                utils.send(
                    "api/driver/products",
                    "GET",
                    {},
                    done,
                    error
                );
            };

            var createAct = function (lostProducts,done, error) {
                utils.send(
                    "api/driver/act",
                    "POST",
                    lostProducts,
                    done,
                    error
                );
            };

            return {
                getProducts: getProducts,
                createAct: createAct
            }
        }

        return new DriverService();
    });