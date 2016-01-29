define(['app/service/employeesService', 'app/service/navService', 'app/service/barService', "knockout", "text!./addEmployee.html"],
    function (employeesService, navService, bar, ko, addEmployeeTemplate) {
        "use strict";

        function addEmployeeViewModel() {
            bar.go(50);
            var self = this;
            self.employee = ko.observable({});
            self.confirmPassword = ko.observable();
            self.rolesList = ko.observableArray(['Admin','Dispatcher','Manager','Driver','Director']);
            self.addEmployee = function () {
                employeesService.add(
                    self.employee(),
                    function (data) {
                        navService.mainPage();
                    },
                    function (data) {
                        switch (data.status) {
                            case 403:
                                navService.navigateTo("login");
                                break;
                            default:
                                navService.navigateTo("error");
                        }
                    });
            };


            bar.go(100);
            return self;
        }

        return {viewModel: addEmployeeViewModel, template: addEmployeeTemplate};
    });