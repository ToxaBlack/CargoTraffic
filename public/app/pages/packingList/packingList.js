define(['app/service/packingListService','app/service/navService' ,'app/service/barService', "knockout", 'jquery',"text!./packingList.html"],

    function (packingListService, navService ,bar, ko, $,ttnTemplate) {
        "use strict";

        function Goods(name, quantity,price) {
            var self = this;
            self.name =  name;
            self.quantity = quantity;
            self.unit = null;
            self.storage = null;
            self.price = price;
        }

        function packingViewModel() {

            bar.go(50);
            var self = this;
            self.units = ko.observableArray(["Kilogram","Liter","Square meter","Pieces"]);

            self.packingList = ko.observable();

            self.packingList.date = new Date();
            self.packingList.to = ko.observable({name:""});
            self.packingList.from = ko.observable({name:""});
            self.packingList.products = ko.observableArray([
                new Goods("Конфеты 'Аленка'", 100,5),
                new Goods("Хлеб 'Бородинский'", 500,3),
                new Goods("Водка 'First Potemkin'", 250,1)
            ]);

            self.closeDialog = function () {
                $('#warehouses-popup').modal("hide");
                var context = ko.contextFor($("#warehouses")[0]);
                context.$data.checkedWarehouses([]);
            };

            self.openModal = function (param) {
                $('#warehouses-popup').modal("show");
                self.warehousePoint = param ;
            };

            self.addGoods = function() {
                self.packingList.products.push(new Goods());
            };

            self.removeGoods = function(goods) {
                self.packingList.products.remove(goods)
            };

            self.choose = function() {
                var context = ko.contextFor($("#warehouses")[0]);
                switch(self.warehousePoint) {
                    case 'from' :
                        self.packingList.from(context.$data.getChosenWarehouse());
                        break;
                    case 'to' :
                        self.packingList.to(context.$data.getChosenWarehouse());
                        break;
                    default: return false;
                }
                context.$data.checkedWarehouses([]);
                $('#warehouses-popup').modal("hide");
            };

            self.create = function() {
                packingListService.save(
                    self.packingList.to(),
                    self.packingList.from(),
                    self.packingList.products(),
                    self.packingList.date,
                    function (data) {
                        navService.navigateTo("account");
                    },
                    function (data) {
                        switch (data.status) {
                            case 403:
                                navService.navigateTo("login");
                                break;
                            default:
                                navService.navigateTo("error");
                        }
                    });
            };


            bar.go(100);
            return self;
        }

        return {viewModel: packingViewModel, template: ttnTemplate};
    });