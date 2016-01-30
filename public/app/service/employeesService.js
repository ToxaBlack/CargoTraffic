define(["app/utils/utils"],
    function (utils) {
        "use strict";
        function EmployeesService() {

            var get = function (count, done, error, always) {
                utils.send(
                    "api/employees",
                    "GET",
                    {"id": "1", "employees": count, "ascOrder": "true"},
                    done,
                    error,
                    always
                );
            };

            var toNextPage = function (nextPageFirstCompanyId, count, done, error) {
                utils.send(
                    "api/employees",
                    "GET",
                    {"id": nextPageFirstCompanyId, "employees": count, "ascOrder": "true"},
                    done,
                    error
                );
            };

            var toPreviousPage = function(count, date, done, error){
                utils.send(
                    "api/employees",
                    "GET",
                    {"id": date, "employees": count, "ascOrder": "false"},
                    done,
                    error
                );
            };

            var add = function (employee, done, error) {
                utils.send(
                    "api/employees",
                    "POST",
                    JSON.stringify(employee),
                    done,
                    error
                );
            };

            return {
                get: get,
                toNextPage: toNextPage,
                toPreviousPage: toPreviousPage,
                add: add
            }
        }

        return new EmployeesService();
    });

