define(['app/service/accountService', 'app/service/employeesService', 'app/service/vehiclesService','app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./waybill.html"],

    function (accountService, employeesService, vehiclesService, navService, bar, ko, $, waybillTemplate) {
        "use strict";

        function waybillViewModel() {

            bar.go(50);
            var self = this;
            var MAX_COUNT = 10000000;
            self.drivers = ko.observableArray([]);
            self.selectedDriver = ko.observable();

            self.vehicles = ko.observableArray([]);
            self.selectedVehicle = ko.observable();


            //self.manager = ko.observable("");

            self.Id = function(smth) {return smth.id;};
            self.driverFullName = function(driver) {return driver.name+' '+driver.surname};
            self.vehicleFullName = function(vehicle) {return vehicle.vehicleProducer + ' ' + vehicle.vehicleModel + ' ' + vehicle.licensePlate;};
            self.managerFullName = function() {return self.waybill().manager().username + ', ' + self.waybill().manager().name + ' ' + self.waybill().manager().surname;};

            self.addVehicleDriver = function(){
                if (self.selectedVehicle()!=null && self.selectedDriver()!=null){
                    self.waybill().vehicleDrivers.push(new VehicleDriver(self.selectedVehicle(),self.selectedDriver()));
                    self.drivers.remove(self.selectedDriver());
                    self.vehicles.remove(self.selectedVehicle());
                    self.selectedDriver("");
                    self.selectedVehicle("");
                }
            };

            self.removeVehicleDriver = function(vehicleDriver){
                console.log(vehicleDriver);
                self.vehicles.push(vehicleDriver.vehicle());
                self.drivers.push(vehicleDriver.driver());
                self.waybill().vehicleDrivers.remove(vehicleDriver);
            };

            function VehicleDriver(vehicle,driver) {
                var self = this;
                self.vehicle = ko.observable(vehicle);
                self.driver = ko.observable(driver);
                self.products = ko.observableArray([]);
            }

            self.waybill = ko.observable({
                date: new Date(),
                manager : ko.observable(""),
                vehicleDrivers : ko.observableArray([
                ])
            });

            accountService.get(
                function (data) {
                    self.waybill().manager(data);
                },
                function (data) {navService.catchError(data)},
                function () {
                    bar.go(100);
                }
            );

            vehiclesService.list(1, MAX_COUNT, true,
                function (data) {
                    self.vehicles(data);
                    if(self.vehicles().length == 0) $("#noVehicles").text("No vehicles in your company");
                },
                function (data) {navService.catchError(data)},
                function () {
                    bar.go(100);
                }
            );

            employeesService.getDrivers(
                function (data) {
                    self.drivers(data);
                    if(self.drivers().length == 0) $("#noDrivers").text("No drivers in your company");
                },
                function (data) {navService.catchError(data)},
                function () {
                    bar.go(100);
                }
            );

            bar.go(100);
            return self;
        }

        return {viewModel: waybillViewModel, template: waybillTemplate};
    });