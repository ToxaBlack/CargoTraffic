define(['app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./waybill.html"],

    function (navService, bar, ko, $,ttnTemplate) {
        "use strict";

        function ttnViewModel() {

            bar.go(50);
            var self = this;

            self.driver = ko.observable();
            self.car = ko.observable();

            bar.go(100);
            return self;
        }

        return {viewModel: ttnViewModel, template: ttnTemplate};
    });