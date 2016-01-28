define(['app/utils/messageUtil','app/service/warehouseService', 'app/service/navService', 'app/service/barService',
    "knockout", "jquery", "text!./warehouses.html"],
    function (message,warehouseService, navService, bar, ko, $, listTemplate) {
        "use strict";

        function warehousesViewModel() {
            bar.go(50);
            var self = this;
            self.isEdit = false;
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
               if(self.isEdit) {

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
                var editWarehouse = $.grep(self.warehouses(), function(element) {
                    return element.id == self.checkedWarehouses()[0];
                });

                //Feeling dialog's form
                self.warehouseName(editWarehouse[0].name);
                self.country(editWarehouse[0].address.country);
                self.city(editWarehouse[0].address.city);
                self.street(editWarehouse[0].address.street);
                self.house(editWarehouse[0].address.house);

                $('#myModal').modal("show");
                self.isEdit = true;
            };

            self.closeDialog = function () {
                //Clearing dialog's form
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

