define(['app/service/packingListService', 'app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./packingLists.html"],

    function (packingListService, navService, bar, ko, $, packingListTemplate) {
        "use strict";

        function packingListViewModel() {
            bar.go(50);
            var self = this;
            self.packingLists = ko.observableArray([]);
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);
            self.PACKINGLISTS_PER_PAGE = 5;
            var dateOptions = {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric'
            };

            packingListService.list(1, self.PACKINGLISTS_PER_PAGE + 1, true,
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
                packingListService.list(self.packingLists()[0].id, self.PACKINGLISTS_PER_PAGE + 1, false,
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

            function toReadableDate(data) {
                $.each(data, function (index, element) {
                    element.issueDate = new Date(element.issueDate).toLocaleString("ru", dateOptions);
                });
            }

            return self;
        }

        return {viewModel: packingListViewModel, template: packingListTemplate};
    });