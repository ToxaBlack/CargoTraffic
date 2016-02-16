define(['app/service/waybillService','app/service/navService' ,'app/service/barService', "knockout",
        'jquery',"app/models/models","app/utils/messageUtil","text!./checkDelivery.html"],

    function (waybillService, navService ,bar, ko, $,models,message,template) {
        "use strict";

        function viewModel() {
            bar.go(50);
            var self = this;
            self.products = ko.observableArray();
            self.realCount = ko.observableArray();
            waybillService.getProducts(
                function (data) {
                    self.products(data);
                },
                function (data) {
                    navService.catchError(data);
                });

            bar.go(100);
            return self;
        }

        return {viewModel: viewModel, template: template};
    });