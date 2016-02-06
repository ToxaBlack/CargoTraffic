define(['app/service/packingListService','app/service/navService' ,'app/service/barService', "knockout", 'jquery',"text!./packingList.html"],

    function (packingListService, navService ,bar, ko, $,ttnTemplate) {
        "use strict";

        function Goods(name, quantity,price) {
            var self = this;
            self.name =  name;
            self.quantity = quantity;
            self.unit = null;
            self.price = price;
        }

        function ttnViewModel() {

            bar.go(50);
            var self = this;
            self.units = ko.observableArray(["Kilogram","Liter","Square meter","Piece"]);

            self.packingList = ko.observable({date:{}, to:{}, from:{}, products:[]});

            //self.products = ko.observableArray();
            self.products = ko.observableArray([
                new Goods("Конфеты 'Аленка'", 100,5),
                new Goods("Хлеб 'Бородинский'", 500,3),
                new Goods("Водка 'First Potemkin'", 250,1)
            ]);

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
                self.products.push(new Goods());
            };

            self.removeGoods = function(goods) {
                self.products.remove(goods)
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

            self.create = function() {
                alert(self.products()[3].unit);
                //packingListService.save(
                //    self.products(),
                //    function (data) {
                //        navService.navigateTo("account");
                //    },
                //    function (data) {
                //        switch (data.status) {
                //            case 403:
                //                navService.navigateTo("login");
                //                break;
                //            default:
                //                navService.navigateTo("error");
                //        }
                //    });
            };


            bar.go(100);
            return self;
        }

        return {viewModel: ttnViewModel, template: ttnTemplate};
    });