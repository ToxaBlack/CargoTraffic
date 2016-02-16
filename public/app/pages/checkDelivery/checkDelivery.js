define(['app/service/waybillService','app/service/navService' ,'app/service/barService', "knockout",
        'jquery',"app/models/models","app/utils/messageUtil","text!./checkDelivery.html"],

    function (waybillService, navService ,bar, ko, $,models,message,template) {
        "use strict";

        function viewModel() {
            bar.go(50);
            var self = this;
            self.products = ko.observableArray();
            self.check = function (){
                alert(self.products()[0].realQuantity());
            };

            self.checkEnable = ko.computed(function() {
                var flag = true;
                ko.utils.arrayForEach(self.products(), function(item) {
                    if(!(item.realQuantity())) {
                        flag = false;
                        return false;
                    }
                });
                return flag;
            }, this);

            waybillService.getProducts(
                function (data) {
                    $.each(data, function (index, element) {
                        self.products.push(new models.CheckProduct(element.product.id, element.product.name,
                            element.quantity, element.product.measureUnit.name));
                    });
                },
                function (data) {
                    navService.catchError(data);
                });

            bar.go(100);
            return self;
        }

        return {viewModel: viewModel, template: template};
    });