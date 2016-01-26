define(['app/utils/utils', "knockout", "jquery", "text!./warehouses.html"], function (utils, ko, $, listTemplate) {
    "use strict";

    function warehousesViewModel() {
        var self = this;
        self.warehouses = ko.observableArray();
        self.checkedWarehouses = ko.observableArray();
        self.hasNextPage = ko.observable(false);
        self.hasPreviousPage = ko.observable(false);
        self.allChecked = false;
        self.warehouseName = ko.observable();
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


        self.toggleAssociation = function (item) {
            if (item.Selected() === true) console.log("dissociate item " + item.id());
            else console.log("associate item " + item.id());
            item.Selected(!(item.Selected()));
            return true;
        };

        self.addWarehouse = function() {
            utils.send("api/addWarehouse", "POST",
                JSON.stringify({warehouseName: self.warehouseName()}),
                function () {
                    var dialog = $('#myModal');
                    dialog.modal("hide");
                },
                function () {
                    var dialog = $('#myModal');
                    dialog.modal("hide");
                    utils.goTo("error");
                });
        };

        self.editWarehouse = function() {
            alert("edit");
        };

        self.deleteWarehouse = function() {
            alert(self.checkedWarehouse());
        }

        self.isOpen = ko.observable(false);

        self.open =function() {
            this.isOpen(true);
        };

        self.close = function() {
            this.isOpen(false);
        };

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
                    self.warehouses(data);
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
                    self.warehouses(data);
                },
                function () {
                    utils.goTo("error");
                });

        };

        return self;
    }

    return {viewModel: warehousesViewModel, template: listTemplate};
});

