define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function VehiclesService() {

            var list = function (id, numberOfVehicles, ascOrder, done, error, always) {
                utils.send(
                    "api/vehicles",
                    "GET",
                    {"id": id, "vehicles": numberOfVehicles, "ascOrder": ascOrder},
                    done,
                    error,
                    always
                );
            };

            var add = function (vehicle, done, error, always) {
                utils.send(
                    "api/vehicle",
                    "POST",
                    JSON.stringify(vehicle),
                    done,
                    error,
                    always
                );
            };

            var remove = function (ids, done, error, always) {
                utils.send(
                    "api/vehicles",
                    "DELETE",
                    JSON.stringify(ids),
                    done,
                    error,
                    always
                );
            };

            var update = function (vehicle, done, error) {
                utils.send(
                    "api/vehicle",
                    "PUT",
                    JSON.stringify(vehicle),
                    done,
                    error
                );
            };


            return {
                list: list,
                add: add,
                remove: remove,
                update: update
            }
        }

        return new VehiclesService();
    });

