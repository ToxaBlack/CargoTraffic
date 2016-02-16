define(['app/service/waybillService','app/service/navService' ,'app/service/barService', "knockout",
        'jquery',"app/models/models","app/utils/messageUtil","text!./checkDelivery.html"],

    function (waybillService, navService ,bar, ko, $,models,message,template) {
        "use strict";

        function viewModel() {
            bar.go(50);
            var self = this;
            self.checked = ko.observable(false);
            self.products = ko.observableArray();
            self.lostProducts = ko.observableArray();
            self.check = function (){
                self.checked(true);
                ko.utils.arrayForEach(self.products(), function(item) {
                    var diff = item.quantity - item.realQuantity();
                    if(diff > 0){
                        self.lostProducts.push(new models.CheckProduct(null,item.name,diff, item.measure));
                    }
                });
                console.log(self.lostProducts());
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


            self.confirm = function () {
                //TODO
            };

            self.createAct = function () {
                self.lostProducts([]);
                //TODO
            };

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