define(['app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./packingList.html"],

    function (navService, bar, ko, $,ttnTemplate) {
        "use strict";

        function Goods(name, quantity,price) {
            var self = this;
            self.name = ko.observable(name);
            self.quantity = ko.observable(quantity);
            self.unit = ko.observable();
            self.price = ko.observable(price);
        }

        function ttnViewModel() {

            bar.go(50);
            var self = this;
            self.units = ko.observableArray(["Kilogram","Liter","Square meter","Piece"]);

            self.closeDialog = function () {
                $('#warehouses-popup').modal("hide");
                var context = ko.contextFor($("#warehouses")[0]);
                context.$data.checkedWarehouses([]);
            };

            self.to = ko.observable({name:""});
            self.from = ko.observable({name:""});

            self.openModal = function (param) {
                $('#warehouses-popup').modal("show");
                self.warehousePoint = param ;
            };

            self.addGoods = function() {
                self.goods.push(new Goods("", 0, 0));
            };

            self.removeGoods = function(goods) {
                self.goods.remove(goods)
            };

            self.choose = function() {
                var context = ko.contextFor($("#warehouses")[0]);
                switch(self.warehousePoint) {
                    case 'from' :
                        self.from(context.$data.getChosenWarehouse());
                        break;
                    case 'to' :
                        self.to(context.$data.getChosenWarehouse());
                        break;
                    default: return false;
                }
                context.$data.checkedWarehouses([]);
                $('#warehouses-popup').modal("hide");
            };

            self.goods = ko.observableArray([
                new Goods("Конфеты 'Аленка'", 100,5),
                new Goods("Хлеб 'Бородинский'", 500,3),
                new Goods("Водка 'First Potemkin'", 250,1)
            ]);


            bar.go(100);
            return self;
        }

        return {viewModel: ttnViewModel, template: ttnTemplate};
    });