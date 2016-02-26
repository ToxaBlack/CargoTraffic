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

            var completeDelivery = function (done, error) {
                utils.send(
                    "api/driver/complete",
                    "PUT",
                    JSON.stringify({body: "empty"}),
                    done,
                    error
                );
            };

            return {
                getProducts: getProducts,
                createAct: createAct,
                completeDelivery: completeDelivery
            }
        }

        return new DriverService();
    });