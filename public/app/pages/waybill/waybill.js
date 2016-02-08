define(['app/service/employeesService', 'app/service/vehiclesService','app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./waybill.html"],

    function (employeesService, vehiclesService, navService, bar, ko, $, waybillTemplate) {
        "use strict";

        function waybillViewModel() {

            bar.go(50);
            var self = this;
            var MAX_COUNT = 10000000;
            self.drivers = ko.observable([]);
            self.driver = ko.observable();
            self.driverFullName = function(driver) {return driver.name+' '+driver.surname};
            self.vehicles = ko.observable([]);
            self.vehicle = ko.observable();
            self.vehicleFullName = function(vehicle) {return vehicle.vehicleProducer + ' ' + vehicle.vehicleModel + ' ' + vehicle.licensePlate;};
            self.Id = function(smth) {return smth.id;};
            //self.vehicleFullName = function(){ko.computed(function(vehicle) {return vehicle.vehicleProducer + ' ' + vehicle.vehicleModel; },self);};
            vehiclesService.list(1, MAX_COUNT, true,
                function (data) {
                    self.vehicles(data);
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
                }
            );

            employeesService.getDrivers(
                function (data) {
                    self.drivers(data);
                    console.log(self.drivers());
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
                }
            );

            bar.go(100);
            return self;
        }

        return {viewModel: waybillViewModel, template: waybillTemplate};
    });