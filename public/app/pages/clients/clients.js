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
                                element.locked = true;
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
                                element.locked = false;
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
                navService.navigateTo("addClient");
            };


            return self;
        }


        return {viewModel: clientsViewModel, template: listTemplate};
    });

