define(['app/service/navService', 'app/service/clientService', "knockout", 'app/service/barService', "jquery", "text!./Clients.html"],
    function (navService, clientService, ko, bar, $, listTemplate) {
        "use strict";

        function clientsViewModel() {
            bar.go(50);
            var self = this;
            self.clients = ko.observableArray([]);
            self.checkedClients = ko.observableArray([]);
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);
            self.allChecked = false;
            self.CLIENTS_PER_PAGE = 3;

            clientService.list(1, 4, true,
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
                clientService.list(nextPageFirstClientId, 4, true,
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
                clientService.list(self.clients()[0].id, 4, false,
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

            self.checkedClient = function () {
                console.log("checkedClient called");
                if ($.inArray(this, self.checkedClients()) === -1)
                    self.checkedClients.push(this);
                else
                    self.checkedClients.remove(this);
            };

            self.checkAll = function () {

            };

            self.addClient = function () {
                navService.navigateTo("addClient");
            };


            return self;
        }


        return {viewModel: clientsViewModel, template: listTemplate};
    });

