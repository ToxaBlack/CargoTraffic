define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function WaybillService() {

            var save = function (waybill, waypoints, done, error, always) {
                utils.send(
                    "api/waybill",
                    "POST",
                    JSON.stringify({'waybill' : waybill ,'waypoints' :waypoints}),
                    done,
                    error
                );
            };

            var getProducts = function (done, error) {
                utils.send(
                    "api/waybill",
                    "GET",
                    JSON.stringify({'trace' : 'trace'}),
                    done,
                    error
                );
            };

            return {
                save: save,
                getProducts: getProducts
            }
        }

        return new WaybillService();
    });