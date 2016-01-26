define(['app/service/warehouseService', 'app/service/navService', 'app/service/barService', "knockout", "jquery", "text!./warehouses.html"],
    function (warehouseService, navService, bar, ko, $, listTemplate) {
        "use strict";

        function warehousesViewModel() {
            bar.go(50);
            var self = this;
            self.warehouses = ko.observableArray();
            self.checkedWarehouses = ko.observableArray();
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);
            self.allChecked = false;
            self.warehouseName = ko.observable();
            self.WAREHOUSES_PER_PAGE = 3;

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


            self.toggleAssociation = function (item) {
                if (item.Selected() === true) console.log("dissociate item " + item.id());
                else console.log("associate item " + item.id());
                item.Selected(!(item.Selected()));
                return true;
            };

            self.addWarehouse = function () {
                warehouseService.add(self.warehouseName(),
                    function () {
                        var dialog = $('#myModal');
                        dialog.modal("hide");
                    },
                    function (data) {
                        var dialog = $('#myModal');
                        dialog.modal("hide");
                        switch (data.status) {
                            case 403:
                                navService.navigateTo("login");
                                break;
                            default:
                                navService.navigateTo("error");
                        }
                    });
            };

            self.editWarehouse = function () {
                var dialog = $('#myModal');
                dialog.modal("show");
                alert("//TODO");
            };

            self.deleteWarehouse = function () {
                alert("//TODO");
            };

            self.isOpen = ko.observable(false);

            self.open = function () {
                this.isOpen(true);
            };

            self.close = function () {
                this.isOpen(false);
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

            return self;
        }

        return {viewModel: warehousesViewModel, template: listTemplate};
    });

