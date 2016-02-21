define(['app/service/waybillService','app/service/accountService', 'app/service/employeesService', 'app/service/vehiclesService','app/service/packingListService', 'app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./waybill.html"],

    function (waybillService, accountService, employeesService, vehiclesService, packingListService, navService, bar, ko, $, waybillTemplate) {
        "use strict";

        function waybillViewModel(requestParams) {

            bar.go(50);
            var self = this;

            self.packingListDate = ko.observable({});

            self.drivers = ko.observableArray([]);
            self.selectedDriver = ko.observable({});

            self.vehicles = ko.observableArray([]);
            self.selectedVehicle = ko.observable({});

            self.goods = ko.observableArray([]);
            self.departureAddress = ko.observable({});
            self.destinationAddress = ko.observable({});

            self.pageInitialised = ko.observable(false);


            self.selectedVehicleDriver = ko.observable();
            self.waybill = ko.observable({
                packingListId: ko.observable(),
                departureDate: ko.observable(),
                arrivalDate: ko.observable(),
                manager : ko.observable(""),
                vehicleDrivers : ko.observableArray([
                ])
            });

            var dateOptions = {
                year: 'numeric',
                month: 'long',
                day: 'numeric',
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric'
            };

            self.localDates = ko.observable({
                departureDate: ko.computed(function(){
                    if(self.waybill().departureDate()!=undefined)
                        return self.waybill().departureDate().toLocaleString("ru", dateOptions);})
            });

            self.driverFullName = function(driver) {
                if(driver!=undefined) return driver.name+' '+driver.surname;
                return "";
            };
            self.vehicleFullNameInHTML = function(vehicle) {return '<b>'+self.vehicleFullName(vehicle)+'</b>'+' ('+vehicle.vehicleType.vehicleType+')'}
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
                self.vehicles.push(vehicleDriver.vehicle());
                self.drivers.push(vehicleDriver.driver());
                self.waybill().vehicleDrivers.remove(vehicleDriver);
            };

            function VehicleDriver(vehicle,driver) {
                var vd = this;
                vd.vehicle = ko.observable(vehicle);
                vd.driver = ko.observable(driver);
                vd.products = ko.observableArray(jQuery.extend(true,[],self.goods()));
                for(var i=0;i<vd.products().length;i++)
                    vd.products()[i].quantity=ko.observable(0);
                vd.onLink = function () {
                    self.selectedVehicleDriver(vd);
                    $('#addingProducts').modal();
                };
                return vd;
            }

            waybillService.getWaybill(
                requestParams.id,
                function (data) {
                    self.goods(data.packingList.products);
                    self.goods().forEach(function(product,i,goods){
                        product.lastQuantity = ko.computed(function(){
                            var quantity = product.quantity;
                            if(self.waybill().vehicleDrivers().length>0)
                                self.waybill().vehicleDrivers().forEach(function(vd,j,vds){
                                    quantity -= vd.products()[i].quantity();
                                });
                            return quantity;
                        })
                    });
                    self.packingListDate(new Date(data.packingList.issueDate).toLocaleString("ru", dateOptions));
                    self.waybill().departureDate(new Date());
                    self.waybill().packingListId(data.packingList.id);
                    self.departureAddress(data.packingList.departureWarehouse.address);
                    self.destinationAddress(data.packingList.destinationWarehouse.address);
                    self.drivers(data.drivers);
                    self.vehicles(data.vehicles);
                    self.waybill().manager(data.manager);

                    self.pageInitialised(true);
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
            self.center = ko.observable() ;
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
                //var center = new google.maps.LatLng(51.508742, -0.120850);
                var geocoder = new google.maps.Geocoder();
                var centerTemp =  new google.maps.LatLng(51.508742, -0.120850);
                var mapProp = {
                    center: centerTemp,
                    scrollwheel: false,
                    zoom: 7,
                    mapTypeId:google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("googleMap"), mapProp);

                var country = self.departureAddress().country;
                var countryCity = country + ', ' + self.departureAddress().city;
                var countryCityStreet = countryCity + ', ' + self.departureAddress().street;
                var address = countryCityStreet + ', ' + self.departureAddress().house;

                codeAddress(geocoder, address, map);
                /*if(self) codeAddress(geocoder, countryCityStreet, map);
                if(center == null) codeAddress(geocoder, countryCity, map);
                if(center == null) codeAddress(geocoder, country, map);
*/
                self.tempWaypoints = ko.observableArray([]);
                self.tempWaypoints = self.waypoints();
                self.waypoints = ko.observableArray([]);
                var lastPoint = self.center();
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
                        origin: self.center,
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

                /*google.maps.event.addListener(marker, 'click', function () {
                    map.setZoom(10);
                    map.setCenter(marker.getPosition());
                });*/
                google.maps.event.addListener(map, 'click', function (event) {
                    placeMarker(event.latLng);
                });

            }

            function geocodeLatLng(geocoder, latlng, i) {
                geocoder.geocode({'location': latlng}, function(results, status) {
                    if (status === google.maps.GeocoderStatus.OK) {
                        if (results[1]) {
                            self.address()[i] = results[1].formatted_address;
                            console.log(self.address()[i]);
                        } else {
                            console.log('No results found');
                            return null;
                        }
                    } else {
                        console.log('Geocoder failed due to: ' + status);
                    }
                });
            }

            function codeAddress(geocoder, address, map) {
                geocoder.geocode( { 'address': address}, function(results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                        var center = results[0].geometry.location;
                        map.setCenter(center);
                        var marker = new google.maps.Marker({
                            position: center,
                            animation: google.maps.Animation.BOUNCE,
                            map: map
                        });
                        self.center = center;
                        console.log(results[0].formatted_address);
                    } else {
                        console.log("Geocode was not successful for the following reason: " + status);
                    }
                });
            }

            $('#addButton').click(
                function () {
                    $('#mapModal').modal();
                    setTimeout(initialize, 500);
                }
            );

            $('#btnSavePoints').click(
                function () {
                    var table = document.getElementById("waypointsTable");
                    $("#waypointsTable").find("tr").remove();
                    self.waypoints().unshift(self.center);
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
                }
            );

            $("#btnCancelPoints").click(
              function(){
                  self.waypoints = self.tempWaypoints;
              }
            );

            $('#btnSendAll').click(
              function(){
                  waybillService.save(
                      ko.toJS(self.waybill().manager),
                      ko.toJS(self.waybill().departureDate),
                      ko.toJS(self.waybill().arrivalDate),
                      ko.toJS(self.waybill().vehicleDrivers),
                      ko.toJS(self.waybill().packingListId),
                      ko.toJS(self.waypoints()),
                      function(){
                          self.waybill = ko.observableArray([]);
                          self.waypoints = ko.observableArray([]);
                      },
                      function (data) {navService.catchError(data);}
                  );
              }
            );
            bar.go(100);
            return self;
        }

        return {viewModel: waybillViewModel, template: waybillTemplate};
    });