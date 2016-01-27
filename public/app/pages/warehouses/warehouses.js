define(['app/utils/messageUtil','app/service/warehouseService', 'app/service/navService', 'app/service/barService',
    "knockout", "jquery", "text!./warehouses.html"],
    function (message,warehouseService, navService, bar, ko, $, listTemplate) {
        "use strict";

        function warehousesViewModel() {
            bar.go(50);
            var self = this;
            self.warehouses = ko.observableArray();
            self.checkedWarehouses = ko.observableArray();
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);
            self.warehouseName = ko.observable();
            self.allChecked = ko.computed(function() {
                var success = $.grep(self.warehouses(), function(element,index) {
                        return $.inArray(element.id.toString(), self.checkedWarehouses()) !== -1;
                    }).length === self.warehouses().length;
                return success;
            }, this);
            self.WAREHOUSES_PER_PAGE = 20;

            warehouseService.list(1, self.WAREHOUSES_PER_PAGE + 1, true,
                function (data) {
                    if (data.length === self.WAREHOUSES_PER_PAGE + 1) {
                        self.hasNextPage(true);
                        data.pop();
                    } else {
                        self.hasNextPage(false);
                    }
                    self.hasPreviousPage(false);
                    self.warehouses(data);
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


            self.addWarehouse = function () {
                warehouseService.add(self.warehouseName(),
                    function (data) {
                        self.warehouses.push(data);
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
                        var dialog = $('#myModal');
                        dialog.modal("hide");
                        self.warehouseName("");
                    });
            };

            self.editWarehouse = function () {
                var countChoosen = self.checkedWarehouses().length;
                if(! countChoosen || countChoosen > 1) {
                    var mess = "Please, choose just one warehouse.";
                    message.createWarningMessage(mess);
                    return false;
                }
                var dialog = $('#myModal');
                dialog.modal("show");
            };

            self.deleteWarehouse = function () {
                alert("//TODO");
            };

            self.nextPage = function () {
                if (!self.hasNextPage()) return;
                var nextPageFirstWarehouseId = self.warehouses()[self.warehouses().length - 1].id + 1;
                warehouseService.list(nextPageFirstWarehouseId, self.WAREHOUSES_PER_PAGE + 1, true,
                    function (data) {
                        if (data.length === self.WAREHOUSES_PER_PAGE + 1) {
                            self.hasNextPage(true);
                            data.pop();
                        } else {
                            self.hasNextPage(false);
                        }
                        self.hasPreviousPage(true);
                        self.warehouses(data);
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
                warehouseService.list(self.warehouses()[0].id, self.WAREHOUSES_PER_PAGE + 1, false,
                    function (data) {

                        if (data.length === self.WAREHOUSES_PER_PAGE + 1) {
                            self.hasPreviousPage(true);
                            data.shift();
                        } else {
                            self.hasPreviousPage(false);
                        }
                        self.hasNextPage(true);
                        self.warehouses(data);
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
                    $.each(self.warehouses(), function (index, element) {
                        if ($.inArray(element.id.toString(), self.checkedWarehouses()) === -1) {
                            self.checkedWarehouses.push(element.id.toString());
                        }
                    });
                } else {
                    $.each(self.warehouses(), function (index, element) {
                        if ($.inArray(element.id.toString(), self.checkedWarehouses()) !== -1) {
                            self.checkedWarehouses.remove(element.id.toString());
                        }
                    });
                }
            });

            return self;
        }

        return {viewModel: warehousesViewModel, template: listTemplate};
    });

