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

            return {
                save: save,
                getWaypints: getWaypints
            }
        }

        return new WaybillService();
    });