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



            // Waypoints logic

            self.waypoints = ko.observableArray([]);
            self.tempWaypoints = ko.observableArray([]);
            self.checkedWays = ko.observableArray([]);
            self.allChecked = ko.computed(function () {
                var success = $.grep(self.waypoints(), function (element, index) {
                        return $.inArray(element.id.toString(), self.checkedWays()) !== -1;
                    }).length === self.waypoints().length;
                return success;
            }, this);

            function includeJs() {
                var js = document.createElement("script");
                js.type = "text/javascript";
                js.src = "http://maps.googleapis.com/maps/api/js?key=AIzaSyD-OL6Y6UrkY0rhd9rDl70wViuhRXW9OrE";
                document.body.appendChild(js);
            }

            $(document).ready(function () {
                includeJs();
            });
            function initialize() {
                var center = new google.maps.LatLng(51.508742, -0.120850);
                self.tempWaypoints = ko.observableArray([]);
                self.tempWaypoints = self.waypoints();
                self.waypoints = ko.observableArray([]);
                var lastPoint = center;
                var mapProp = {
                    center: center,
                    scrollwheel: false,
                    zoom: 7,
                    mapTypeId:google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
                var marker = new google.maps.Marker({
                    position: mapProp.center,
                    animation: google.maps.Animation.BOUNCE
                });
                marker.setMap(map);

                var directionsDisplay = new google.maps.DirectionsRenderer({
                    map: map
                });
                var directionsService = new google.maps.DirectionsService();

                function placeMarker(location) {
                    self.waypoints().push(location);
                    var infowindow = new google.maps.InfoWindow({
                        content: 'Latitude: ' + location.lat() +
                        '<br>Longitude: ' + location.lng()
                    });
                    lastPoint = location;
                    var waypts = [];
                    for (var i = 0; i < self.waypoints().length - 1; i++) {
                        waypts.push({
                            location: self.waypoints()[i],
                            stopover: true
                        });
                    }
                    directionsService.route({
                        origin: center,
                        destination: lastPoint,
                        waypoints: waypts,
                        optimizeWaypoints: true,
                        travelMode: google.maps.TravelMode.DRIVING
                    }, function (response, status) {
                        if (status === google.maps.DirectionsStatus.OK) {
                            directionsDisplay.setDirections(response);
                            var route = response.routes[0];
                        } else {
                            window.alert('Directions request failed due to ' + status);
                        }
                    });
                }

                google.maps.event.addListener(marker, 'click', function () {
                    map.setZoom(10);
                    map.setCenter(marker.getPosition());
                });
                google.maps.event.addListener(map, 'click', function (event) {
                    placeMarker(event.latLng);
                });

            }


            $('#addButton').click(
                function () {
                    initialize();
                    $('#mapModal').modal();
                }
            );

            $('#btnSavePoints').click(
                function () {
                    var table = document.getElementById("waypointsTable");
                    $("#waypointsTable tr").remove();
                    for (var i = 0;  i < self.waypoints().length; ++i) {
                        var rowCount = table.rows.length;
                        var row = table.insertRow(rowCount);

                        var cell1 = row.insertCell(0);
                        var element1 = document.createElement("input");
                        element1.type = "checkbox";
                        element1.class = "idCheck";
                        cell1.appendChild(element1);

                        var cell2 = row.insertCell(1);
                        var element2 = document.createTextNode('Latitude: ' + self.waypoints()[i].lat() + ' Longitude: ' + self.waypoints()[i].lng());
                        cell2.appendChild(element2);
                    }
                    /*service.save(
                        self.waypoints(),
                        function (data) {
                            self.waypoints([]);
                            window.location.reload();
                        },
                        function (data) {
                            switch (data.status) {
                                case 403:
                                    navService.navigateTo("login");
                                    break;
                                default:
                                    navService.navigateTo("error");
                            }
                        });*/
                }
            );

            $("#btnCancelPoints").click(
              function(){
                  self.waypoints = self.tempWaypoints;
              }
            );


            bar.go(100);
            return self;
        }

        return {viewModel: waybillViewModel, template: waybillTemplate};
    });