define(['app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./checkPackingList.html"],

    function (navService, bar, ko, $,ttnTemplate) {
        "use strict";

        function Goods(name, quantity) {
            var self = this;
            self.name = ko.observable(name);
            self.quantity = ko.observable(quantity);
        }

        function ttnViewModel() {

            bar.go(50);
            var self = this;

            self.to = ko.observable({name:""});
            self.from = ko.observable({name:""});

            self.goods = ko.observableArray([
                new Goods("Конфеты 'Аленка'", 100),
                new Goods("Хлеб 'Бородинский'", 500),
                new Goods("Водка 'First Potemkin'", 250)
            ]);

            self.toWaybill = function() {
                window.location.href = 'waybill';
            };

            bar.go(100);
            return self;
        }

        return {viewModel: ttnViewModel, template: ttnTemplate};
    });