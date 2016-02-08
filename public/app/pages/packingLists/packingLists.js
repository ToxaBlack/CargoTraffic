define(['app/service/packingListService', 'app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./packingLists.html"],

    function (packingListService, navService, bar, ko, $, packingListTemplate) {
        "use strict";

        function PackingList(id, createDate) {
            var self = this;
            self.id = id;
            self.createDate = createDate;
        }

        function packingListViewModel() {
            bar.go(50);
            var self = this;
            self.packingLists = ko.observableArray([]);
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);
            self.PACKINGLISTS_PER_PAGE = 10;


            packingListService.list(1, self.PACKINGLISTS_PER_PAGE + 1, true,
                function (data) {
                    if (data.length === self.PACKINGLISTS_PER_PAGE + 1) {
                        self.hasNextPage(true);
                        data.pop();
                    } else {
                        self.hasNextPage(false);
                    }
                    self.hasPreviousPage(false);
                    self.packingLists(data);
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
                var nextPageFirstPackingListId = self.packingLists()[self.packingLists().length - 1].id + 1;
                packingListService.list(
                    nextPageFirstPackingListId,
                    self.PACKINGLISTS_PER_PAGE + 1,
                    true,
                    function (data) {
                        if (data.length === self.PACKINGLISTS_PER_PAGE + 1) {
                            self.hasNextPage(true);
                            data.pop();
                        } else {
                            self.hasNextPage(false);
                        }
                        self.hasPreviousPage(true);
                        self.packingLists(data);
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
                packingListService.list(self.packingLists()[0].id, self.PACKINGLISTS_PER_PAGE + 1, false,
                    function (data) {
                        if (data.length === self.PACKINGLISTS_PER_PAGE + 1) {
                            self.hasPreviousPage(true);
                            data.shift();
                        } else {
                            self.hasPreviousPage(false);
                        }
                        self.hasNextPage(true);
                        self.packingLists(data);
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

            self.toPackingList = function () {
                navService.navigateTo("checkPackingList");
            };

            return self;
        }

        return {viewModel: packingListViewModel, template: packingListTemplate};
    });