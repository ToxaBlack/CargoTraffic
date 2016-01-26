define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function EmployeesService() {

            var get = function (done, error, always) {
                utils.send(
                    "api/employees",
                    "GET",
                    {"id": "1", "employees": "10", "ascOrder": "true"},
                    done,
                    error,
                    always
                );
            };

            var toNextPage = function (nextPageFirstCompanyId, done, error) {
                utils.send(
                    "api/employees",
                    "GET",
                    {"id": nextPageFirstCompanyId, "employees": "10", "ascOrder": "true"},
                    done,
                    error
                );
            };

            var toPreviousPage = function(date, done, error){
                utils.send(
                    "api/employees",
                    "GET",
                    {"id": date, "employees": "10", "ascOrder": "false"},
                    done,
                    error
                );
            };

            return {
                get: get,
                toNextPage: toNextPage,
                toPreviousPage: toPreviousPage
            }
        }

        return new EmployeesService();
    });

