define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function PackingListService() {

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
                save: save
            }
        }
        return new PackingListService();
    });

