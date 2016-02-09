define(['app/service/accountService', 'app/service/employeesService', 'app/service/vehiclesService','app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./waybill.html"],

    function (accountService, employeesService, vehiclesService, navService, bar, ko, $, waybillTemplate) {
        "use strict";

        function waybillViewModel() {

            bar.go(50);
            var self = this;
            var MAX_COUNT = 10000000;
            self.drivers = ko.observable([]);
            self.driver = ko.observable();

            self.vehicles = ko.observable([]);
            self.vehicle = ko.observable();


            self.manager = ko.observable("");

            self.Id = function(smth) {return smth.id;};
            self.driverFullName = function(driver) {return driver.name+' '+driver.surname};
            self.vehicleFullName = function(vehicle) {return vehicle.vehicleProducer + ' ' + vehicle.vehicleModel + ' ' + vehicle.licensePlate;};
            self.managerFullName = function() {return self.manager().username + ', ' + self.manager().name + ' ' + self.manager().surname;};
            

            accountService.get(
                function (data) {
                    self.manager(data);
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