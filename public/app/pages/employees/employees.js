define(['app/utils/utils', "knockout", 'app/service/barService', "jquery", "text!./employees.html"],
    function (utils, ko, bar, $, listTemplate) {
        "use strict";

        function employeesViewModel() {
            bar.go(50);
            var self = this;
            self.employees = ko.observableArray([]);
            self.checkedEmployees = ko.observableArray([]);
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);
            self.allChecked = false;
            self.EMPLOYEES_PER_PAGE = 10;

            utils.send("api/employees", "GET", {"id": "1", "employees": "10", "ascOrder": "true"},
                function (data) {
                    if (data.length === self.EMPLOYEES_PER_PAGE + 1) {
                        self.hasNextPage(true);
                        data.pop();
                    } else {
                        self.hasNextPage(false);
                    }
                    self.hasPreviousPage(false);
                    self.employees(data);
                },
                function (data) {
                    utils.goTo("error");
                },
                function () {
                    bar.go(100);
                });


            self.nextPage = function () {
                if (!self.hasNextPage()) return;
                var nextPageFirstCompanyId = self.employees()[self.employees().length - 1].id + 1;
                utils.send("api/employees", "GET",
                    {"id": nextPageFirstCompanyId, "employees": "10", "ascOrder": "true"},
                    function (data) {
                        if (data.length === self.EMPLOYEES_PER_PAGE + 1) {
                            self.hasNextPage(true);
                            data.pop();
                        } else {
                            self.hasNextPage(false);
                        }
                        self.hasPreviousPage(true);
                        self.employees(data);
                    },
                    function (data) {
                        utils.goTo("error");
                    });

            };

            self.previousPage = function () {
                if (!self.hasPreviousPage()) return;
                utils.send("api/employees", "GET",
                    {"id": self.employees()[0].id, "employees": "10", "ascOrder": "false"},
                    function (data) {

                        if (data.length === self.EMPLOYEES_PER_PAGE + 1) {
                            self.hasPreviousPage(true);
                            data.shift();
                        } else {
                            self.hasPreviousPage(false);
                        }
                        self.hasNextPage(true);
                        self.employees(data);
                    },
                    function () {
                        utils.goTo("error");
                    });

            };

            self.checkedEmployee = function () {
                console.log("checkedEmployees called");
                if ($.inArray(this, self.checkedEmployees()) === -1)
                    self.checkedEmployees.push(this);
                else
                    self.checkedEmployees.remove(this);
            };

            self.checkAll = function () {
                console.log("in checkAll");
            };


            return self;
        }


        return {viewModel: employeesViewModel, template: listTemplate};
    });

