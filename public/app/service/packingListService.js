define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function PackingListService() {

            var list = function (id, numberOfPackingLists, ascOrder, done, error, always) {
                utils.send(
                    "api/packingLists",
                    "GET",
                    {"id": id, "packingLists": numberOfPackingLists, "ascOrder": ascOrder},
                    done,
                    error,
                    always
                );
            };

            var save = function (packingList,done, error, always) {
                utils.send(
                    "api/packingList",
                    "POST",
                    packingList,
                    //JSON.stringify({"departureWarehouse": from, "destinationWarehouse": to, "products": products, "issueDate": date }),
                    done,
                    error,
                    always
                );
            };

            return {
                list: list,
                save: save
            }
        }
        return new PackingListService();
    });

