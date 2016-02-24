define(['app/service/packingListService', 'app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./packingLists.html"],

    function (packingListService, navService, bar, ko, $, packingListTemplate) {
        "use strict";

        function packingListViewModel() {
            bar.go(50);
            var self = this;
            self.packingLists = ko.observableArray([]);
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);

            self.packingListsPending = ko.observableArray([]);
            self.hasNextPagePending = ko.observable(false);
            self.hasPreviousPagePending = ko.observable(false);

            self.PACKINGLISTS_PER_PAGE = 5;
            var dateOptions = {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric'
            };

            self.getFullAddress=function(warehouse){
                if(warehouse != undefined && warehouse.address != undefined){
                    var address = warehouse.address;
                return address.country + ', '
                + address.city + ' city, ' + address.street + ' st., ' + address.house;
                }};

            self.getFullName=function(person){
                if(person!=undefined)
                return person.surname + ' ' + person.name + ' ' + person.patronymic; };

            packingListService.list(1, self.PACKINGLISTS_PER_PAGE + 1, true, true,
                function (data) {
                    if (data.length === self.PACKINGLISTS_PER_PAGE + 1) {
                        self.hasNextPage(true);
                        data.pop();
                    } else {
                        self.hasNextPage(false);
                    }
                    self.hasPreviousPage(false);
                    toReadableDate(data);
                    self.packingLists(data);
                },
                function (data) {
                    navService.catchError(data);
                },
                function () {
                    bar.go(100);
                }
            );

            var roles = ko.contextFor($("body")[0]).$data.roles();
            if(roles.length !==0 && roles[0].name == "MANAGER" ){
                packingListService.list(
                    1,
                    self.PACKINGLISTS_PER_PAGE + 1,
                    true,
                    false,
                    function (data) {
                        if (data.length === self.PACKINGLISTS_PER_PAGE + 1) {
                            self.hasNextPagePending(true);
                            data.pop();
                        } else {
                            self.hasNextPagePending(false);
                        }
                        self.hasPreviousPagePending(false);
                        toReadableDate(data);
                        self.packingListsPending(data);
                    },
                    function (data) {
                        navService.catchError(data);
                    },
                    function () {
                        bar.go(100);
                    }
                );

                self.nextPagePending = function () {
                    if (!self.hasNextPagePending()) return;
                    var nextPageFirstPackingListId = self.packingListsPending()[self.packingListsPending().length - 1].id + 1;
                    packingListService.list(
                        nextPageFirstPackingListId,
                        self.PACKINGLISTS_PER_PAGE + 1,
                        true,false,
                        function (data) {
                            if (data.length === self.PACKINGLISTS_PER_PAGE + 1) {
                                self.hasNextPagePending(true);
                                data.pop();
                            } else {
                                self.hasNextPagePending(false);
                            }
                            self.hasPreviousPagePending(true);
                            toReadableDate(data);
                            self.packingListsPending(data);
                        },
                        function (data) {
                            navService.catchError(data);
                        }
                    );
                };

                self.previousPagePending = function () {
                    if (!self.hasPreviousPagePending()) return;
                    packingListService.list(
                        self.packingListsPending()[0].id,
                        self.PACKINGLISTS_PER_PAGE + 1,
                        false,false,
                        function (data) {
                            if (data.length === self.PACKINGLISTS_PER_PAGE + 1) {
                                self.hasPreviousPagePending(true);
                                data.shift();
                            } else {
                                self.hasPreviousPagePending(false);
                            }
                            self.hasNextPagePending(true);
                            toReadableDate(data);
                            self.packingListsPending(data);
                        },
                        function (data) {
                            navService.catchError(data);
                        });
                };

            }


            self.nextPage = function () {
                if (!self.hasNextPage()) return;
                var nextPageFirstPackingListId = self.packingLists()[self.packingLists().length - 1].id + 1;
                packingListService.list(
                    nextPageFirstPackingListId,
                    self.PACKINGLISTS_PER_PAGE + 1,
                    true,true,
                    function (data) {
                        if (data.length === self.PACKINGLISTS_PER_PAGE + 1) {
                            self.hasNextPage(true);
                            data.pop();
                        } else {
                            self.hasNextPage(false);
                        }
                        self.hasPreviousPage(true);
                        toReadableDate(data);
                        self.packingLists(data);
                    },
                    function (data) {
                        navService.catchError(data);
                    }
                );
            };


            self.previousPage = function () {
                if (!self.hasPreviousPage()) return;
                packingListService.list(
                    self.packingLists()[0].id,
                    self.PACKINGLISTS_PER_PAGE + 1,
                    false,true,
                    function (data) {
                        if (data.length === self.PACKINGLISTS_PER_PAGE + 1) {
                            self.hasPreviousPage(true);
                            data.shift();
                        } else {
                            self.hasPreviousPage(false);
                        }
                        self.hasNextPage(true);
                        toReadableDate(data);
                        self.packingLists(data);
                    },
                    function (data) {
                        navService.catchError(data);
                    });
            };

            self.toPackingList = function (packingList) {
                var context = ko.contextFor($("body")[0]);
                var roles = context.$data.roles();
                if(roles[0].name === "MANAGER") {
                    navService.navigateTo("checkPackingList/" + packingList.id);
                }
            };

            self.toPackingListPending = function (packingList) {
                var context = ko.contextFor($("body")[0]);
                var roles = context.$data.roles();
                if(roles[0].name === "MANAGER") {
                    navService.navigateTo("waybill/" + packingList.id);
                }
            };

            function toReadableDate(data) {
                $.each(data, function (index, element) {
                    element.issueDate = new Date(element.issueDate).toLocaleString("ru", dateOptions);
                });
            }

            return self;
        }

        return {viewModel: packingListViewModel, template: packingListTemplate};
    });