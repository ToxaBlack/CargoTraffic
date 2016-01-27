define(['app/service/navService', 'app/service/clientService', "knockout", 'app/service/barService', "jquery", "text!./clients.html"],
    function (navService, clientService, ko, bar, $, listTemplate) {
        "use strict";

        function clientsViewModel() {
            bar.go(50);
            var self = this;
            self.clients = ko.observableArray([]);
            self.checkedClients = ko.observableArray([]);
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);
            self.allChecked = ko.computed(function() {
                var success = $.grep(self.clients(), function(element,index) {
                    return $.inArray(element.id.toString(), self.checkedClients()) !== -1;
                }).length === self.clients().length;
                return success;
            }, this);
            self.CLIENTS_PER_PAGE = 10;


            clientService.list(1, self.CLIENTS_PER_PAGE + 1, true,
                function (data) {
                    if (data.length === self.CLIENTS_PER_PAGE + 1) {
                        self.hasNextPage(true);
                        data.pop();
                    } else {
                        self.hasNextPage(false);
                    }
                    self.hasPreviousPage(false);
                    self.clients(data);
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
                });


            self.nextPage = function () {
                if (!self.hasNextPage()) return;
                var nextPageFirstClientId = self.clients()[self.clients().length - 1].id + 1;
                clientService.list(nextPageFirstClientId, self.CLIENTS_PER_PAGE + 1, true,
                    function (data) {
                        if (data.length === self.CLIENTS_PER_PAGE + 1) {
                            self.hasNextPage(true);
                            data.pop();
                        } else {
                            self.hasNextPage(false);
                        }
                        self.hasPreviousPage(true);
                        self.clients(data);
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

            self.previousPage = function () {
                if (!self.hasPreviousPage()) return;
                clientService.list(self.clients()[0].id, self.CLIENTS_PER_PAGE + 1, false,
                    function (data) {
                        if (data.length === self.CLIENTS_PER_PAGE + 1) {
                            self.hasPreviousPage(true);
                            data.shift();
                        } else {
                            self.hasPreviousPage(false);
                        }
                        self.hasNextPage(true);
                        self.clients(data);
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

            self.getClientStatus = function(isLocked) {
                return isLocked ? "Locked" : "Unlocked";
            };


            $('#selectAllCheckbox').on('click', function () {
                if (!self.allChecked()) {
                    $.each(self.clients(), function (index, element) {
                        if ($.inArray(element.id.toString(), self.checkedClients()) === -1) {
                            self.checkedClients.push(element.id.toString());
                        }
                    });
                } else {
                    $.each(self.clients(), function (index, element) {
                        if ($.inArray(element.id.toString(), self.checkedClients()) !== -1) {
                            self.checkedClients.remove(element.id.toString());
                        }
                    });
                }
            });

            $('#lockButton').on('click', function() {
                clientService.lock(self.checkedClients(),
                    function() {
                        var auxiliaryArray = self.clients().slice();
                        $.each(auxiliaryArray, function (index, element) {
                            if ($.inArray(element.id.toString(), self.checkedClients()) !== -1) {
                                element.deleted = true;
                                auxiliaryArray.splice(index, 1);
                                auxiliaryArray.splice(index, 0, element);
                            }
                        });
                        self.clients([]);
                        self.clients(auxiliaryArray);
                        self.checkedClients([]);
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
            });

            $('#unlockButton').on('click', function() {
                clientService.unlock(self.checkedClients(),
                    function() {
                        var auxiliaryArray = self.clients().slice();
                        $.each(auxiliaryArray, function (index, element) {
                            if ($.inArray(element.id.toString(), self.checkedClients()) !== -1) {
                                element.deleted = false;
                                auxiliaryArray.splice(index, 1);
                                auxiliaryArray.splice(index, 0, element);
                            }
                        });
                        self.clients([]);
                        self.clients(auxiliaryArray);
                        self.checkedClients([]);
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
            });


            self.addClient = function () {
                $('#addClientModal').modal();
            };


            /*************** Modal-dialog ***************/


            self.company = {};
            self.admin = {};
            self.company.name = ko.observable();
            self.admin.surname = ko.observable();
            self.admin.email = ko.observable();

            self.companyNameRegexp = "^[a-zA-Z0-9_-]{1,250}$";
            self.surnameRegexp = "^[a-zA-Z]{1,250}$";
            self.emailRegexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]{1,210}@[a-zA-Z0-9-]{1,30}\.[a-zA-Z0-9-]{2,3}$";
            self.isCompanyNameCorrect = ko.computed(function() {
                if (self.company.name())
                    return (!!self.company.name().match(self.companyNameRegexp));
                return true;
            });
            self.isAdminSurnameCorrect = ko.computed(function() {
                if (self.admin.surname())
                    return (!!self.admin.surname().match(self.surnameRegexp));
                return true;
            });
            self.isAdminEmailCorrect = ko.computed(function() {
                if (self.admin.email())
                    return (!!self.admin.email().match(self.emailRegexp));
                return true;
            });
            self.isValidated = ko.computed(function() {
                return self.isCompanyNameCorrect() &&
                    self.isAdminSurnameCorrect() &&
                    self.isAdminEmailCorrect() &&
                    !!self.company.name() &&
                    !!self.admin.surname() &&
                    !!self.admin.email();
            });


            self.add = function () {
                if (!self.isValidated()) return;
                clientService.add(self.company, self.admin,
                    function (data) {
                        $('#addClientModal').modal('toggle');
                        // TODO push data to self.clients if self.CLIENTS_PER_PAGE < 10
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


        return {viewModel: clientsViewModel, template: listTemplate};
    });

