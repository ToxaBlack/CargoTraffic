define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function ChartService() {

            var list = function (minDate, maxDate, done, error, always) {
                utils.send(
                    "api/money",
                    "GET",
                    {"minDate": minDate, "maxDate": maxDate},
                    done,
                    error,
                    always
                );
            };

            return {
                list: list
            }
        }

        return new ChartService();
    });