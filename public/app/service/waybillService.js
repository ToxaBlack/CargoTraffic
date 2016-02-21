define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function WaybillService() {

            var save = function (manager,departureDate,arrivalDate,vehicleDrivers,packingListId,waypoints, done, error, always) {
                utils.send(
                    "api/waybill",
                    "POST",

                    JSON.stringify({
                        'manager':manager,
                        'departureDate':departureDate,
                        'arrivalDate':arrivalDate,
                        'vehicleDrivers':vehicleDrivers,
                        'packingList':{'id' : packingListId},
                        'waypoints':waypoints}),
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

            var putWaypints = function (checkedId, controlPoints, done, error) {
                utils.send(
                    "api/waypoints",
                    "PUT",
                    JSON.stringify({'checked': checkedId, 'controlPoints': controlPoints}),
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
                getWaybill: getWaybill,
                putWaypoints: putWaypints
            }
        }

        return new WaybillService();
    });