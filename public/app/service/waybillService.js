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

            var get = function(id, done, error, always){
                utils.send(
                    "api/waybill/"+id,
                    "GET",
                    {},
                    done,
                    error
                );
            };

            return {
                save: save
            }
        }

        return new WaybillService();
    });