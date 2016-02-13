define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function WaypointsService() {

            var save = function (waypoints, done, error, always) {
                utils.send(
                    "api/waypoints",
                    "POST",
                    JSON.stringify(waypoints),
                    //JSON.stringify({'waypoints' : waypoints}),
                    done,
                    error
                );
            };

            return {
                save: save
            }
        }

        return new WaypointsService();
    });