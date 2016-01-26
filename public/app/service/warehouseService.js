define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function WarehouseService() {


            var list = function (id, numberOfWarehouses, ascOrder, done, error, always) {
                utils.send(
                    "api/warehouses",
                    "GET",
                    {"id": id, "warehouses": numberOfWarehouses, "ascOrder": ascOrder},
                    done,
                    error,
                    always
                );
            };

            var add = function (warehouse, done, error, always) {
                utils.send(
                    "api/warehouse",
                    "POST",
                    JSON.stringify({warehouseName: warehouse}),
                    done,
                    error,
                    always
                );
            };



            return {
                list: list,
                add: add
            }
        }

        return new WarehouseService();
    });