define(['app/utils/messageUtil','app/service/warehouseService', 'app/service/navService',
        "knockout", "jquery", "text!./warehouses.html"],
    function (message,warehouseService, navService, ko, $, listTemplate) {
        "use strict";

        function warehousesViewModel() {
            var self = this;
            self.idEdit = -1;
            self.chosen = ko.observable(-1);
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

            self.warehousesPerPage = ko.observable(5);
            self.warehousesPerPage.subscribe( function() {
                self.showWarehouses();
            });

            self.recordCount = ko.computed(function (){
                return self.warehouses().length
            },this);

            self.allChecked = ko.computed(function() {
                var success = $.grep(self.warehouses(), function(element) {
                        return $.inArray(element.id.toString(), self.checkedWarehouses()) !== -1;
                    }).length === self.recordCount();
                return success;
            }, this);

            self.showWarehouses = function () {
                warehouseService.list(1, self.warehousesPerPage() + 1, true,
                    function (data) {
                        if (data.length === self.warehousesPerPage() + 1) {
                            self.hasNextPage(true);
                            data.pop();
                        } else {
                            self.hasNextPage(false);
                        }
                        self.hasPreviousPage(false);
                        self.warehouses(data);
                    },
                    function (data) {
                        navService.catchError(data);
                    });
            };

            self.showWarehouses();

            self.saveWarehouse = function () {
                if( $("#warehouseForm").valid()){
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
                                navService.catchError(data);
                            },
                            function () {
                                self.closeDialog();
                            });
                    } else {
                        $("#warehouseForm").validate(
                            warehouseService.add(self.warehouseName(), self.country(),self.city(),self.street(), self.house(),
                                function (data) {
                                    if (self.recordCount() != self.warehousesPerPage()) {
                                        self.warehouses.push(data);
                                    } else self.hasNextPage(true);
                                },
                                function (data) {
                                    navService.catchError(data);
                                },
                                function () {
                                    self.closeDialog();
                                })
                        );
                    }
                }

            };

            self.getChosenWarehouse = function () {
                var countChosen = self.checkedWarehouses().length;
                if(! countChosen || countChosen > 1) {
                    message.createWarningMessage("messageBox","Please, choose only one warehouse.");
                    return false;
                }
                for (var i = 0; i < self.warehouses().length; i++) {
                    if (self.warehouses()[i].id == self.checkedWarehouses()[0]) {
                        self.editRow = i;
                        var editWarehouse= self.warehouses()[i];
                        break;
                    }
                }
                return editWarehouse;
            };

            $("#warehouseForm").validate();
            self.editWarehouse = function () {
                var editWarehouse = self.getChosenWarehouse();
                self.idEdit = editWarehouse.id;
                self.warehouseName(editWarehouse.name);
                self.country(editWarehouse.address.country);
                self.city(editWarehouse.address.city);
                self.street(editWarehouse.address.street);
                self.house(editWarehouse.address.house);
                $('#warehouseModal').modal("show");
            };

            self.closeDialog = function () {
                self.idEdit = -1;
                $("#warehouseForm").validate().resetForm();
                $('#warehouseModal').modal("hide");
                self.warehouseName("");
                self.country("");
                self.city("");
                self.street("");
                self.house("");
            };


            self.deleteWarehouse = function () {
                if(! self.checkedWarehouses().length) {
                    message.createWarningMessage("messageBox","Please, choose at least one warehouse.");
                    return false;
                }
                var deletingWarehouse =  $.grep(self.warehouses(), function(element) {
                    return $.inArray(element.id.toString(), self.checkedWarehouses()) !== -1;
                });
                warehouseService.remove(deletingWarehouse,
                    function () {
                        //Pass id of first row


                        self.warehouses.remove( function(item) {
                            return $.inArray(item.id.toString(), self.checkedWarehouses()) !== -1;
                        });
                        if( self.recordCount() === 0) {
                            self.previousPage();
                        }
                        if( self.hasNextPage() && self.recordCount() < self.warehousesPerPage()) {
                            self.showWarehouses();
                        }
                    },
                    function (data) {
                        navService.catchError(data);
                    },
                    function () {
                        self.checkedWarehouses([]);
                    });
            };

            self.nextPage = function () {
                if (!self.hasNextPage()) return;
                var nextPageFirstWarehouseId = self.warehouses()[self.warehouses().length - 1].id + 1;
                warehouseService.list(nextPageFirstWarehouseId, self.warehousesPerPage() + 1, true,
                    function (data) {
                        if (data.length === self.warehousesPerPage() + 1) {
                            self.hasNextPage(true);
                            data.pop();
                        } else {
                            self.hasNextPage(false);
                        }
                        self.hasPreviousPage(true);
                        self.warehouses(data);
                    },
                    function (data) {
                        navService.catchError(data);
                    });

            };

            self.previousPage = function () {
                if (!self.hasPreviousPage()) return;

                //Check case, when user have deleted all rows in table

                var  id = self.warehouses()[0].id;


                warehouseService.list(id, self.warehousesPerPage() + 1, false,
                    function (data) {
                        if (data.length === self.warehousesPerPage() + 1) {
                            self.hasPreviousPage(true);
                            data.shift();
                        } else {
                            self.hasPreviousPage(false);
                        }
                        if(!self.lastId) self.hasNextPage(true);
                        self.lastId = null;
                        self.warehouses(data);
                    },
                    function (data) {
                        navService.catchError(data);
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

