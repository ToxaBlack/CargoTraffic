define(['app/service/employeesService','app/service/navService', "knockout", 'app/service/barService', "jquery", "text!./employees.html"],
    function (employeesService, navService, ko, bar, $, listTemplate) {
        "use strict";

        function employeesViewModel() {
            bar.go(50);
            var self = this;
            self.employees = ko.observableArray([]);
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);
            self.EMPLOYEES_PER_PAGE = 10;
            self.edit =  ko.observableArray([]);
            self.rolesList = ko.observableArray(['admin','dispatcher','manager','driver','director']);
            self.selectedRole = ko.observableArray();

            self.checkedEmployees = ko.observableArray([]);
            self.allChecked = ko.computed(function () {
                var success = $.grep(self.employees(), function (element, index) {
                        return $.inArray(element.id.toString(), self.checkedEmployees()) !== -1;
                    }).length === self.employees().length;
                return success;
            }, this);

            employeesService.get(
                self.EMPLOYEES_PER_PAGE + 1,
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
                    switch (data.status) {
                        case 403:
                            navService.navigateTo("login");
                            break;
                        default:
                            navService.navigateTo("error");
                    }
                },
                function () {
                    bar.go(100);
                }
            );

            self.nextPage = function () {
                if (!self.hasNextPage()) return;
                var nextPageFirstCompanyId = self.employees()[self.employees().length - 1].id + 1;
                employeesService.toNextPage(
                    nextPageFirstCompanyId,
                    self.EMPLOYEES_PER_PAGE + 1,
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
                        switch (data.status) {
                            case 403:
                                navService.navigateTo("login");
                                break;
                            default:
                                navService.navigateTo("error");
                        }
                    }
                );

            };

            self.previousPage = function () {
                if (!self.hasPreviousPage()) return;
                employeesService.toPreviousPage(
                    self.EMPLOYEES_PER_PAGE + 1,
                    self.employees()[0].id,
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

            $('#selectAllCheckbox').on('click', function () {
                if (!self.allChecked()) {
                    $.each(self.employees(), function (index, element) {
                        if ($.inArray(element.id.toString(), self.checkedEmployees()) === -1) {
                            self.checkedEmployees.push(element.id.toString());
                        }
                    });
                } else {
                    $.each(self.employees(), function (index, element) {
                        if ($.inArray(element.id.toString(), self.checkedEmployees()) !== -1) {
                            self.checkedEmployees.remove(element.id.toString());
                        }
                    });
                }
            });

            $('#addButton').on('click', function () {
                navService.navigateTo("addEmployee");
            });

            $('#removeButton').on('click', function () {
                employeesService.remove(
                    self.checkedEmployees(),
                    function () {
                        var tempArray = self.employees().slice();
                        $.each(tempArray, function (index, element) {
                            if ($.inArray(element.id.toString(), self.checkedEmployees()) !== -1) {
                                tempArray.splice(index, 1);
                                tempArray.splice(index, 0, element);
                            }
                        });
                        self.employees([]);
                        self.employees(tempArray);
                        self.checkedEmployees([]);
                        window.location.reload();
                    },
                    function (data) {
                        switch (data.status) {
                            case 403:
                                navService.navigateTo("login");
                                break;
                            default:
                                navService.navigateTo("error");
                        }
                    }
                );
            });

            self.onLink = function (id) {
                employeesService.getUser(
                    id.id,
                    function (data) {
                        self.edit(data);
                        self.edit().id = id.id;
                        self.edit().password = id.password;
                        self.selectedRole.push(id.userRoleList[0].name.toLowerCase());
                        $('#editModal').modal();
                    },
                    function (data) {
                        switch (data.status) {
                            case 403:
                                navService.navigateTo("login");
                                break;
                            default:
                                navService.navigateTo("error");
                        }
                    }
                );
            };

            self.updateEmployee = function () {
                employeesService.update(
                    self.edit(),
                    function (data) {
                        self.edit([]);
                        self.selectedRole([]);
                        window.location.reload();
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

            return self;
        }


        return {viewModel: employeesViewModel, template: listTemplate};
    });
