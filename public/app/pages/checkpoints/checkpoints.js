define(['app/service/packingListService','app/service/navService' ,'app/service/barService', "knockout",
        'jquery',"app/models/models","app/utils/messageUtil","text!./checkpoints.html"],

    function (packingListService, navService ,bar, ko, $,models,message,template) {
        "use strict";

        function viewModel() {
            bar.go(50);
            var self = this;
            self.checkpoints = [
                new models.Checkpoint("Latitude: 50.09999, Longitude:40.2323", "Minsk"),
                new models.Checkpoint("Latitude: 49.43559, Longitude:38.2323", "Grodno"),
                new models.Checkpoint("Latitude: 52.19499, Longitude:41.2323", "Vitebsk"),
                new models.Checkpoint("Latitude: 43.32569, Longitude:44.5463", "Gomel")
            ];


            self.checkIn = function(checkpoint) {
                //checkpoint;
            };


            self.giveLetter =  function(index) {
                return  String.fromCharCode(index() + 65);
            };

            self.toCheckProducts = function() {
                navService.navigateTo('checkDelivery');
            };

            bar.go(100);
            return self;
        }

        return {viewModel: viewModel, template: template};
    });