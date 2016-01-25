define(['app/utils/utils', "knockout", "jquery", "text!./warehouses.html"], function (utils, ko, $, listTemplate) {
    "use strict";

    function warehousesViewModel() {
        var self = this;
        self.warehouses = ko.observableArray([]);
        self.checkedWarehouses = ko.observableArray([]);
        self.hasNextPage = ko.observable(false);
        self.hasPreviousPage = ko.observable(false);
        self.allChecked = false;
        self.WAREHOUSES_PER_PAGE = 3;

        utils.send("api/warehouses", "GET", {"id": "1", "warehouses": self.WAREHOUSES_PER_PAGE + 1, "ascOrder": "true"},
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
                utils.goTo("error");
            });

        self.addWarehouse = function() {
            alert("add!");
        };

        self.editWarehouse = function() {
            alert("edit");
        };

        self.deleteWarehouse = function() {
            alert("delete");
        }

        self.nextPage = function () {
            if (!self.hasNextPage()) return;
            var nextPageFirstWarehouseId = self.warehouses()[self.warehouses().length - 1].id + 1;
            utils.send("api/warehouses", "GET",
                {"id": nextPageFirstWarehouseId, "warehouses": self.WAREHOUSES_PER_PAGE + 1, "ascOrder": "true"},
                function (data) {
                    if (data.length === self.WAREHOUSES_PER_PAGE + 1) {
                        self.hasNextPage(true);
                        data.pop();
                    } else {
                        self.hasNextPage(false);
                    }
                    self.hasPreviousPage(true);
                    self.companies(data);
                },
                function (data) {
                    utils.goTo("error");
                });

        };

        self.previousPage = function () {
            if (!self.hasPreviousPage()) return;
            utils.send("api/warehouses", "GET",
                {"id": self.warehouses()[0].id, "warehouses": self.WAREHOUSES_PER_PAGE + 1, "ascOrder": "false"},
                function (data) {

                    if (data.length === self.WAREHOUSES_PER_PAGE + 1) {
                        self.hasPreviousPage(true);
                        data.shift();
                    } else {
                        self.hasPreviousPage(false);
                    }
                    self.hasNextPage(true);
                    self.companies(data);
                },
                function () {
                    utils.goTo("error");
                });

        };

        self.checkedWarehouse = function () {
            console.log("checkedWarehouse called");
            if ($.inArray(this, self.checkedWarehouses()) === -1)
                self.checkedWarehouses.push(this);
            else
                self.checkedWarehouses.remove(this);
        };

        self.checkAll = function () {
            console.log("in checkAll");
        };

        return self;
    }

    return {viewModel: warehousesViewModel, template: listTemplate};
});

