define(['app/service/waybillService', 'app/service/navService', "knockout", 'app/service/barService', "jquery", "text!./waypoints.html"],
    function (waybilService, navService, ko, bar, $, waypointsTemplate) {
        "use strict";
        function waypointsViewModel() {
            bar.go(50);
            var self = this;
            self.controlPoints = ko.observableArray([]);
            self.waypoints = ko.observableArray([]);
            self.address = ko.observableArray([]);
            self.checkedWays = ko.observableArray([]);
            self.allChecked = ko.computed(function () {
                var success = $.grep(self.controlPoints(), function (element, index) {
                        return $.inArray(element.id.toString(), self.checkedWays()) !== -1;
                    }).length === self.controlPoints().length;
                return success;
            }, this);


            function includeJs() {
                var js = document.createElement("script");
                js.type = "text/javascript";
                js.src = "http://maps.googleapis.com/maps/api/js?key=AIzaSyD-OL6Y6UrkY0rhd9rDl70wViuhRXW9OrE";
                js.id = "googleScript";
                document.body.appendChild(js);
            }

            function initialize() {
                var center;
                if(self.controlPoints().length > 0 )
                    center = new google.maps.LatLng(self.controlPoints()[0].lat,self.controlPoints()[0].lng);
                else center =  new google.maps.LatLng(51.508742, -0.120850);
                var lastPoint = center;
                var mapProp = {
                    center: center,
                    scrollwheel: false,
                    zoom: 7
                };
                var map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
                var geocoder = new google.maps.Geocoder();
                var marker = new google.maps.Marker({
                    position: mapProp.center,
                    animation: google.maps.Animation.BOUNCE,
                    map: map
                });

                self.address.removeAll();
                for(var i = 0; i < self.controlPoints().length; ++i){
                    map.setZoom(11);
                    marker = new google.maps.Marker({
                        position: self.controlPoints()[i],
                        animation: google.maps.Animation.BOUNCE,
                        map: map
                    });
                   geocodeLatLng(geocoder, self.controlPoints()[i], i);
                }

                var directionsDisplay = new google.maps.DirectionsRenderer({
                    map: map
                });
                var directionsService = new google.maps.DirectionsService();

                function drawWay() {
                    if(self.waypoints().length > 0) {
                        lastPoint = self.waypoints()[self.waypoints().length - 1];
                        var waypts = [];
                        for (var i = 0; i < self.waypoints().length - 1; i++) {
                            waypts.push({
                                location: self.waypoints()[i],
                                stopover: true
                            });
                        }
                    }else lastPoint = center;
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
                            console.log('Directions request failed due to ' + status);
                            //window.alert('Directions request failed due to ' + status);
                        }
                    });

                }

                google.maps.event.addListener(marker, 'click', function () {
                    map.setZoom(10);
                    map.setCenter(marker.getPosition());
                });
                $( ":checkbox").click(
                    function(){
                        self.waypoints = ko.observableArray([]);
                        for(var i = 0; i < self.checkedWays().length; ++i){
                            var j = 0;
                            for(; j < self.controlPoints().length; ++j){
                                if(self.controlPoints()[j].id == self.checkedWays()[i]){
                                    break;
                                }
                            }
                            if(j !== self.controlPoints().length) {
                                self.waypoints().push(self.controlPoints()[j]);
                            }
                        }
                        drawWay();
                    }
                );
                if(self.waypoints().length > 0) drawWay();
            }

            function geocodeLatLng(geocoder, latlng, i) {
                geocoder.geocode({'location': latlng}, function(results, status) {
                    if (status === google.maps.GeocoderStatus.OK) {
                        if (results[1]) {
                            self.address.push(results[1].formatted_address);
                        } else {
                            //window.alert('No results found');
                        }
                    } else {
                       // setTimeout(geocodeLatLng(geocoder, latlng, i) , 1000);
                        //window.alert('Geocoder failed due to: ' + status);
                    }
                });
            }

            $(document).ready(function(){
                waybilService.getWaypints(
                    function(data){
                        self.controlPoints(data);
                        $.each(self.controlPoints(), function (index, element) {
                            if (element.status == 'CHECKED') {
                                self.checkedWays.push(element.id.toString());
                                self.waypoints.push(element);
                            }
                        });
                        if($('#googleScript').length == 0) includeJs();
                        setTimeout(initialize, 500);
                    },
                    function (data) {navService.catchError(data);}
                );
            });

            $('#selectAllCheckbox').on('click', function () {
                if (!self.allChecked()) {
                    $.each(self.controlPoints(), function (index, element) {
                        if ($.inArray(element.id.toString(), self.checkedWays()) === -1) {
                            self.checkedWays.push(element.id.toString());
                        }
                    });
                } else {
                    $.each(self.controlPoints(), function (index, element) {
                        if ($.inArray(element.id.toString(), self.checkedWays()) !== -1) {
                            self.checkedWays.remove(element.id.toString());
                        }
                    });
                }
            });

            $('#btnSave').on('click', function(){
                waybilService.putWaypoints(
                    self.checkedWays(),
                    self.controlPoints(),
                    function(){
                        //self.checkedWays = ko.observableArray([]);
                        //self.controlPoints = ko.observableArray([]);
                        //self.waypoints = ko.observableArray([]);
                        navService.navigateTo("checkDelivery");
                    },
                    function (data) {navService.catchError(data);}
            )
            });
            bar.go(100);
            return self;
        }
        return {viewModel: waypointsViewModel, template: waypointsTemplate};
    });