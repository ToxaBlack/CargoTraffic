define(['app/utils/messageUtil','app/service/warehouseService', 'app/service/navService', 'app/service/barService',
    "knockout", "jquery", "text!./warehouses.html"],
    function (message,warehouseService, navService, bar, ko, $, listTemplate) {
        "use strict";

        function warehousesViewModel() {
            bar.go(50);
            var self = this;
            self.idEdit = -1;
            self.warehouses = ko.observableArray();
            self.checkedWarehouses = ko.observableArray();
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);

            //Dialog's form
            self.warehouseName = ko.observable();
            self.country = ko.observable();
            self.city = ko.observable();
            self.street = ko.observable();
            self.house = ko.observable();


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


            self.saveWarehouse = function () {
               if(self.idEdit != -1) {
                   warehouseService.edit(self.idEdit, self.warehouseName(), self.country(),self.city(),self.street(), self.house(),
                       function (data) {
                            var auxiliaryArray = self.warehouses().slice();
                            auxiliaryArray.splice(self.editRow,1);
                            auxiliaryArray.splice(self.editRow,0,data);
                            self.warehouses(auxiliaryArray);
                            self.checkedWarehouses([]);
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
                           self.closeDialog();
                       });
               } else {
                   warehouseService.add(self.warehouseName(), self.country(),self.city(),self.street(), self.house(),
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
                           self.closeDialog();
                       });
               }
            };

            self.editWarehouse = function () {
                var countChosen = self.checkedWarehouses().length;
                if(! countChosen || countChosen > 1) {
                    message.createWarningMessage("Please, choose just only one warehouse.");
                    return false;
                }
                for (var i = 0; i < self.warehouses().length; i++) {
                    if (self.warehouses()[i].id == self.checkedWarehouses()[0]) {
                        self.editRow = i;
                        var editWarehouse= self.warehouses()[i];
                        break;
                    }
                }

                //var editWarehouse = $.grep(self.warehouses(), function(element) {
                //    return element.id == self.checkedWarehouses()[0];
               // });

                //Feeling dialog's form
                self.idEdit = editWarehouse.id;
                self.warehouseName(editWarehouse.name);
                self.country(editWarehouse.address.country);
                self.city(editWarehouse.address.city);
                self.street(editWarehouse.address.street);
                self.house(editWarehouse.address.house);
                $('#myModal').modal("show");
            };

            self.closeDialog = function () {
                //Clearing dialog's form
                self.idEdit = -1;
                self.warehouseName("");
                self.country("");
                self.city("");
                self.street("");
                self.house("");

                $('#myModal').modal("hide");
            }


            self.deleteWarehouse = function () {
                if(! self.checkedWarehouses().length) {
                    message.createWarningMessage("Please, choose at least one warehouse.");
                    return false;
                }
                alert(self.checkedWarehouses());
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

