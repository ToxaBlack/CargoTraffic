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

            var remove = function (id, done, error, always) {
                utils.send(
                    "api/employee/" + id,
                    "DELETE",
                    {},
                    done,
                    error,
                    always
                );
            };

            var getUser = function (id, done, error) {
                utils.send(
                    "api/employees/details",
                    "GET",
                    {"id": id},
                    done,
                    error
                );
            };

            var update = function (date, done, error) {
                utils.send(
                    "api/employees/update",
                    "PUT",
                    JSON.stringify(date),
                    done,
                    error
                );
            };

            var getDrivers = function (done, error, always) {
                utils.send(
                    "api/drivers",
                    "GET",
                    {},
                    done,
                    error,
                    always
                );
            };

            return {
                get: get,
                getUser: getUser,
                toNextPage: toNextPage,
                toPreviousPage: toPreviousPage,
                add: add,
                remove: remove,
                update: update,
                getDrivers: getDrivers
            }
        }

        return new EmployeesService();
    });

