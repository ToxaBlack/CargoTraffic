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

            var getPackingList = function (id, done, error, always) {
                utils.send(
                    "api/packingLists/" + id,
                    "GET",
                    {},
                    done,
                    error,
                    always
                );
            };

            var getCheckedPackingList = function (id, done, error, always) {
                utils.send(
                    "api/packingLists/" + id + "/checked",
                    "GET",
                    {},
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

            var changeStatus = function (id, status, done, error, always) {
                utils.send(
                    "api/packingList/" + id,
                    "PUT",
                    JSON.stringify({'status': status}),
                    done,
                    error,
                    always
                );
            };

            return {
                list: list,
                getPackingList: getPackingList,
                save: save,
                changeStatus: changeStatus,
                getCheckedPackingList: getCheckedPackingList
            }
        }
        return new PackingListService();
    });

