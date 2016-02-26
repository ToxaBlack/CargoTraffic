define(['app/service/packingListService', 'app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./checkPackingList.html"],

    function (packingListService, navService, bar, ko, $, packingListTemplate) {
        "use strict";

        function packingListViewModel(requestParams) {

            bar.go(50);
            var self = this;
            self.departureAddress = ko.observable({});
            self.destinationAddress = ko.observable({});
            self.issueDate = ko.observable();
            self.goodsOnPage = ko.observableArray([]);
            self.allGoods = ko.observableArray([]);
            self.checkedGoods = ko.observableArray([]);
            self.allChecked = ko.computed(function() {
                var isAllChecked = true;
                $.each(self.goodsOnPage(), function (index, element) {
                    if ($.inArray(element.id.toString(), self.checkedGoods()) === -1) {
                        isAllChecked = false;
                    }
                });
                return isAllChecked;
            }, this);
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);
            self.GOODS_PER_PAGE = 5;
            var currentPage = 1;
            var dateOptions = {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric'
            };


            packingListService.getPackingList(requestParams.id,
                function (data) {
                    if (!data.id)
                        navService.navigateTo("packingLists");
                    else {
                        self.allGoods(data.products);
                        self.goodsOnPage(self.allGoods().slice(0, self.GOODS_PER_PAGE * currentPage));
                        self.departureAddress(data.departureWarehouse.address);
                        self.destinationAddress(data.destinationWarehouse.address);
                        self.issueDate(new Date(data.issueDate).toLocaleString("ru", dateOptions));
                        if (self.allGoods().length > self.GOODS_PER_PAGE * currentPage) {
                            self.hasNextPage(true);
                        } else {
                            self.hasNextPage(false);
                        }
                        self.hasPreviousPage(false);
                    }
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
                if (self.allGoods().length > self.GOODS_PER_PAGE * (currentPage + 1)) {
                    self.hasNextPage(true);
                } else {
                    self.hasNextPage(false);
                }
                self.hasPreviousPage(true);
                self.goodsOnPage(self.allGoods().slice(self.GOODS_PER_PAGE * currentPage, self.GOODS_PER_PAGE * (currentPage + 1)));
                currentPage++;
            };

            self.previousPage = function () {
                if (!self.hasPreviousPage()) return;
                if ((currentPage - 1) === 1) {
                    self.hasPreviousPage(false);
                } else {
                    self.hasPreviousPage(true);
                }
                self.hasNextPage(true);
                currentPage--;
                self.goodsOnPage(self.allGoods().slice(self.GOODS_PER_PAGE * (currentPage - 1), self.GOODS_PER_PAGE * currentPage));
            };


            $('#selectAllCheckbox').on('click', function () {
                if (!self.allChecked()) {
                    $.each(self.goodsOnPage(), function (index, element) {
                        if ($.inArray(element.id.toString(), self.checkedGoods()) === -1) {
                            self.checkedGoods.push(element.id.toString());
                        }
                    });
                } else {
                    $.each(self.goodsOnPage(), function (index, element) {
                        if ($.inArray(element.id.toString(), self.checkedGoods()) !== -1) {
                            self.checkedGoods.remove(element.id.toString());
                        }
                    });
                }
            });

            self.acceptPackingList = function() {
                packingListService.changeStatus(requestParams.id, "CHECKED",
                    function () {
                    },
                    function (data) {
                        navService.catchError(data);
                    },
                    function () {
                        bar.go(100);
                    }
                );
                navService.navigateTo('waybillForPackingList/' + requestParams.id);
            };

            self.rejectPackingList = function() {
                packingListService.changeStatus(requestParams.id, "REJECTED",
                    function () {
                    },
                    function (data) {
                        navService.catchError(data);
                    },
                    function () {
                        bar.go(100);
                    }
                );
                navService.navigateTo('packingLists');
            };

            bar.go(100);
            return self;
        }

        return {viewModel: packingListViewModel, template: packingListTemplate};
    });