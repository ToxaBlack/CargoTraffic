define(['app/service/vehiclesService','app/service/navService', "knockout", 'app/service/barService', "jquery", "text!./vehicles.html"],
    function (vehiclesService, navService, ko, bar, $, listTemplate) {
        "use strict";

        var validator;

        function vehiclesViewModel() {
            bar.go(50);
            var self = this;
            self.vehicles = ko.observableArray([]);
            self.hasNextPage = ko.observable(false);
            self.hasPreviousPage = ko.observable(false);
            self.VEHICLES_PER_PAGE = 10;
            self.modalDialogVehicle =  ko.observable({'vehicleFuel':{}, 'vehicleType':{}, 'company':{}});
            self.vehicleFuels = ko.observableArray(['Diesel','Bio-diesel','Petrol-95', 'Petrol-98']);
            self.selectedFuel = ko.observable();
            self.vehicleTypes = ko.observableArray(['Box','Refrigerator','Tank']);
            self.selectedType = ko.observable();
            self.submitDialogButtonName = ko.observable("");
            self.error = ko.observable();


            vehiclesService.list(1, self.VEHICLES_PER_PAGE + 1, true,
                function (data) {
                    if (data.length === self.VEHICLES_PER_PAGE + 1) {
                        self.hasNextPage(true);
                        data.pop();
                    } else {
                        self.hasNextPage(false);
                    }
                    self.hasPreviousPage(false);
                    self.vehicles(data);
                },
                function (data) {
                    navService.catchError(data);
                },
                function () {
                    bar.go(100);
                }
            );

            self.nextPage = function (always) {
                if (!self.hasNextPage()) return;
                var nextPageFirstVehicleId = self.vehicles()[self.vehicles().length - 1].id + 1;
                vehiclesService.list(
                    nextPageFirstVehicleId,
                    self.VEHICLES_PER_PAGE + 1,
                    true,
                    function (data) {
                        if (data.length === self.VEHICLES_PER_PAGE + 1) {
                            self.hasNextPage(true);
                            data.pop();
                        } else {
                            self.hasNextPage(false);
                        }
                        self.hasPreviousPage(true);
                        self.vehicles(data);
                    },
                    function (data) {
                        navService.catchError(data);
                    },
                    always
                );

            };

            self.previousPage = function (always) {
                if (!self.hasPreviousPage()) return;
                vehiclesService.list(self.vehicles()[0].id, self.VEHICLES_PER_PAGE + 1, false,
                    function (data) {
                        if (data.length === self.VEHICLES_PER_PAGE + 1) {
                            self.hasPreviousPage(true);
                            data.shift();
                        } else {
                            self.hasPreviousPage(false);
                        }
                        self.hasNextPage(true);
                        self.vehicles(data);
                    },
                    function (data) {
                        navService.catchError(data);
                    },
                    always);
            };


            self.addVehicleDialog = function () {
                self.modalDialogVehicle({'vehicleFuel':{}, 'vehicleType':{}, 'company':{}});
                self.selectedType(self.vehicleTypes()[0]);
                self.selectedFuel(self.vehicleFuels()[0]);
                self.submitDialogButtonName("Add");
                $('#vehicleModal').modal();
            };


            self.remove = function (attr) {
                this.disabled = true;
                vehiclesService.remove(
                    attr.id,
                    function () {
                        var tempArray = self.vehicles().slice();
                        for (var i = 0; i < tempArray.length; i++) {
                            if (tempArray[i].id === attr.id) {
                                tempArray.splice(i, 1);
                                i--;
                            }
                        }
                        if (tempArray.length === 0) {
                            if (self.hasPreviousPage() && self.hasNextPage()) {
                                self.previousPage();
                            } else
                            if (self.hasPreviousPage() && !self.hasNextPage()) {
                                self.previousPage(function() {
                                    self.hasNextPage(false);
                                });
                            } else
                            if (!self.hasPreviousPage() && self.hasNextPage()) {
                                self.nextPage(function() {
                                    self.hasPreviousPage(false);
                                });
                            } else {
                                self.vehicles([]);
                            }
                        } else {
                            self.vehicles(tempArray);
                        }
                    },
                    function (data) {navService.catchError(data);}
                );
                this.disabled = false;
            };

            self.onLink = function (vehicle) {
                $.each(self.vehicles(), function (index, element) {
                    if (vehicle.id === element.id) {
                        self.modalDialogVehicle(element);
                        self.submitDialogButtonName("Update");
                        $('#vehicleModal').modal();
                    }
                });
            };


            self.dialogSubmit = function() {
                switch (self.submitDialogButtonName()) {
                    case "Update":
                        self.updateVehicle();
                        break;
                    case "Add":
                        self.addVehicle();
                        break;
                }
            };

            self.updateVehicle = function () {
                if (validate()) {
                    $('#vehicleModal').modal('hide');
                    validator.resetForm();
                    vehiclesService.update(
                        self.modalDialogVehicle(),
                        function (data) {
                            var auxiliaryArray = self.vehicles().slice();
                            $.each(auxiliaryArray, function (index, element) {
                                if ($.inArray(element.id.toString(), data.id.toString()) !== -1) {
                                    auxiliaryArray.splice(index, 1);
                                    auxiliaryArray.splice(index, 0, element);
                                }
                            });
                            self.vehicles([]);
                            self.vehicles(auxiliaryArray);
                            self.modalDialogVehicle({'vehicleFuel': {}, 'vehicleType': {}, 'company': {}});
                            self.selectedType(self.vehicleTypes()[0]);
                            self.selectedFuel(self.vehicleFuels()[0]);
                        },
                        function (data) {
                            navService.catchError(data);
                        });
                }
            };

            self.addVehicle = function () {
                if (validate()) {
                    $('#vehicleModal').modal('hide');
                    validator.resetForm();
                    vehiclesService.add(
                        self.modalDialogVehicle(),
                        function (vehicle) {
                            if (self.vehicles().length < self.VEHICLES_PER_PAGE) {
                                self.vehicles.push(vehicle);
                            } else {
                                self.hasNextPage(true);
                            }
                            self.modalDialogVehicle({'vehicleFuel': {}, 'vehicleType': {}, 'company': {}});
                            self.selectedType(self.vehicleTypes()[0]);
                            self.selectedFuel(self.vehicleFuels()[0]);
                        },
                        function (data) {
                            navService.catchError(data);
                        });
                }
            };

            return self;
        }


        function validate() {
            validator = $('#vehicleForm').validate({
                rules: {
                    manufacturer: "required",
                    model: "required",
                    licensePlate: "required",
                    productsWeight: {
                        required: true,
                        number: true
                    },
                    fuelType: "required",
                    fuelConsumption: {
                        required: true,
                        number: true
                    },
                    vehicleType: "required"
                }
            });
            return validator.form();
        }


        return {viewModel: vehiclesViewModel, template: listTemplate};
    });
