define(['app/service/employeesService', 'app/service/navService', 'app/service/barService', "knockout", "text!./addEmployee.html"],
    function (employeesService, navService, bar, ko, addEmployeeTemplate) {
        "use strict";

        function addEmployeeViewModel() {
            bar.go(50);
            var self = this;
            self.employee = ko.observable({});
            self.confirmPassword = ko.observable();
            self.rolesList = ko.observableArray(['Admin', 'Dispatcher', 'Manager', 'Driver', 'Director']);
            self.addEmployee = function () {
                if (validate()) {
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
                }
            };


            bar.go(100);
            return self;
        }

        function validate() {
            var validator = $('#employeeForm').validate({
                rules: {
                    birthDate: {
                        customDate: true
                    },
                    email: {
                        email: true
                    },
                    confirmPassword: {
                        equalTo: '#password'
                    }
                },
                messages: {
                    surname: {
                        required: "Please enter surname."
                    }
                },
                confirmPassword: {
                    equalTo: "Please enter same password"
                },
                password: {
                    pattern: "Password should contain only letters and digits"
                }
            });

            return validator.form();
        }

        return {viewModel: addEmployeeViewModel, template: addEmployeeTemplate};
    }
);