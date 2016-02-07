define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function PackingListService() {

            var save = function (to,from,products,date,done, error, always) {
                utils.send(
                    "api/packingList",
                    "POST",
                    JSON.stringify({"departureWarehouse": from, "destinationWarehouse": to, "products": products, "issueDate": date }),
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

