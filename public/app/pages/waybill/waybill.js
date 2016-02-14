define(['app/service/accountService', 'app/service/employeesService', 'app/service/vehiclesService','app/service/packingListService', 'app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./waybill.html"],

    function (accountService, employeesService, vehiclesService, packingListService, navService, bar, ko, $, waybillTemplate) {
        "use strict";

        function waybillViewModel(requestParams) {

            bar.go(50);
            var self = this;
            var MAX_COUNT = 10000000;
            self.drivers = ko.observableArray([]);
            self.selectedDriver = ko.observable({});

            self.vehicles = ko.observableArray([]);
            self.selectedVehicle = ko.observable({});

            self.goods = ko.observableArray([]);
            self.departureAddress = ko.observable({});
            self.destinationAddress = ko.observable({});

            self.selectedVehicleDriver = ko.observable({});
            //self.manager = ko.observable("");

            self.Id = function(smth) {return smth.id;};
            self.driverFullName = function(driver) {if(driver!=undefined) return driver.name+' '+driver.surname};
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
                var vd = this;
                vd.vehicle = ko.observable(vehicle);
                vd.driver = ko.observable(driver);
                vd.products = ko.observableArray([]);


                vd.onLink = function () {
                    console.log({"vehicle":vd.vehicle(),"driver":vd.driver(),"products":vd.products()});
                    self.selectedVehicleDriver({"vehicle":vd.vehicle,"driver":vd.driver,
                        "products":ko.observableArray(vd.products())});
                    if(vd.products().length == 0)
                    {
                        console.log( self.selectedVehicleDriver().products);
                        console.log(vd.products);
                        self.selectedVehicleDriver().products(self.goods());

                    }
                    console.log(vd);
                    $('#addingProducts').modal();

                };
            }

            self.waybill = ko.observable({
                date: ko.observable(),
                manager : ko.observable(""),
                vehicleDrivers : ko.observableArray([
                ])
            });

            self.addProduct = function (product) {
                if (self.selectedVehicleDriver()!=null){
                    console.log(product);
                    self.selectedVehicleDriver().products.push(product);
                    console.log("index of product");
                    console.log(self.waybill().products.indexOf(product));
                }

            };

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
                function () {bar.go(100)}
            );

            packingListService.getPackingList(requestParams.id,
                function (data) {
                    self.goods(data.products);
                    self.goods().forEach(function(product,i,goods){
                        product.lastQuantity = product.quantity;
                    });
                    self.waybill().date(new Date(data.issueDate));
                    self.departureAddress(data.departureWarehouse.address);
                    self.destinationAddress(data.destinationWarehouse.address);
                },
                function (data) {navService.catchError(data);},
                function () {bar.go(100);}
            );



            bar.go(100);
            return self;
        }

        return {viewModel: waybillViewModel, template: waybillTemplate};
    });