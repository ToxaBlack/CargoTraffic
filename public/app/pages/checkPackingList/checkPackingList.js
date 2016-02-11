define(['app/service/packingListService', 'app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./checkPackingList.html"],

    function (packingListService, navService, bar, ko, $, packingListTemplate) {
        "use strict";

        function packingListViewModel(requestParams) {

            bar.go(50);
            var self = this;
            self.departureAddress = ko.observable({});
            self.destinationAddress = ko.observable({});
            self.issueDate = new Date();
            self.goods = ko.observableArray([]);
            self.checkedGoods = ko.observableArray([]);
            self.allChecked = ko.computed(function() {
                return self.checkedGoods().length === self.goods().length;
            }, this);


            packingListService.getPackingList(requestParams.id,
                function (data) {
                    self.goods(data.products);
                    self.departureAddress(data.departureWarehouse.address);
                    self.destinationAddress(data.destinationWarehouse.address);
                },
                function (data) {
                    navService.catchError(data);
                },
                function () {
                    bar.go(100);
                }
            );


            $('#selectAllCheckbox').on('click', function () {
                if (!self.allChecked()) {
                    $.each(self.goods(), function (index, element) {
                        if ($.inArray(element.id.toString(), self.checkedGoods()) === -1) {
                            self.checkedGoods.push(element.id.toString());
                        }
                    });
                } else {
                    $.each(self.goods(), function (index, element) {
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
                navService.navigateTo('waybill/' + requestParams.id);
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
                navService.navigateTo('account');
            };

            bar.go(100);
            return self;
        }

        return {viewModel: packingListViewModel, template: packingListTemplate};
    });