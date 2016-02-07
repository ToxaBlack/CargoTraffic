define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function PackingListService() {

            var save = function (to,from,products,date,done, error, always) {
                utils.send(
                    "api/packingList",
                    "POST",
                    JSON.stringify({"from": from, "to": to, "products": products, "date": date }),
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

