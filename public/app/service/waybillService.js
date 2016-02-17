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

            var getWaypints = function (id, done, error, always) {
                utils.send(
                    "api/waypoints",
                    "GET",
                    {"id": id},
                    done,
                    error
                );
            };

            var getWaybill = function(id, done, error, always){
                utils.send(
                    "api/waybill/"+id,
                    "GET",
                    {},
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
                getProducts: getProducts,
                save: save,
                getWaypints: getWaypints,
                getWaybill: getWaybill
            }
        }

        return new WaybillService();
    });