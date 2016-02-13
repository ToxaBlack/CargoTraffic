define(['app/service/navService', "knockout", 'app/service/barService', "jquery", "text!./waypoints.html"],
    function (navService, ko, bar, $, waypointsTemplate) {
        "use strict";

        function waypointsViewModel() {
            bar.go(50);
            var self = this;
            self.waypoints = ko.observableArray([]);
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
            $(document).ready(function(){
                includeJs();
            });
            function initialize() {
                var center = new google.maps.LatLng(51.508742,-0.120850);
                var lastPoint = center;
                var mapProp = {
                    center: center,
                    scrollwheel: false,
                    zoom: 7,
                    //mapTypeId:google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("googleMap"),mapProp);
                var marker=new google.maps.Marker({
                    position: mapProp.center,
                    animation:google.maps.Animation.BOUNCE
                });
                marker.setMap(map);

                var directionsDisplay = new google.maps.DirectionsRenderer({
                    map: map
                });
                var directionsService = new google.maps.DirectionsService();

                function placeMarker(location) {
                    /*var marker = new google.maps.Marker({
                        position: location,
                        map: map,
                        animation:google.maps.Animation.BOUNCE
                    });*/
                    self.waypoints().push(location);
                    var infowindow = new google.maps.InfoWindow({
                        content: 'Latitude: ' + location.lat() +
                        '<br>Longitude: ' + location.lng()
                    });
                    //infowindow.open(map,marker);

                   /* var request = {
                        destination: location,
                        origin: lastPoint,
                        travelMode: google.maps.TravelMode.DRIVING
                    };*/

                    lastPoint = location;
                    var waypts = [];
                    for (var i = 0; i <  self.waypoints().length; i++) {
                        waypts.push({
                                location:  self.waypoints()[i],
                                stopover: true
                            });
                    }
                    directionsService.route({
                        origin: center,
                        destination: lastPoint,
                        waypoints: waypts,
                        optimizeWaypoints: true,
                        travelMode: google.maps.TravelMode.DRIVING
                    }, function(response, status) {
                        if (status === google.maps.DirectionsStatus.OK) {
                            directionsDisplay.setDirections(response);
                            var route = response.routes[0];
                        } else {
                            window.alert('Directions request failed due to ' + status);
                        }
                    });
                }
                google.maps.event.addListener(marker,'click',function() {
                    map.setZoom(9);
                    map.setCenter(marker.getPosition());
                });
                google.maps.event.addListener(map, 'click', function(event) {
                    placeMarker(event.latLng);
                });

            }


            $('#addButton').click(
                function(){
                    initialize();
                    $('#editModal').modal()
                }
            );


            bar.go(100);
            return self;
        }


        return {viewModel: waypointsViewModel, template: waypointsTemplate};
    });
