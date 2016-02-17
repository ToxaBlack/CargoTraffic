define(['app/service/packingListService','app/service/navService' ,'app/service/barService', "knockout",
        'jquery',"app/models/models","app/utils/messageUtil","text!./packingList.html"],

    function (packingListService, navService ,bar, ko, $,models,message,template) {
        "use strict";

        function packingViewModel() {

            bar.go(50);
            var self = this;
            self.label = "Click to choose";
            self.storages = ko.observableArray(["Refrigerator","Tank","Boxcar"]);
            self.packingList =  {"issueDate":new Date(), "destinationWarehouse": ko.observable({name: self.label}),
                "departureWarehouse":ko.observable({name: self.label}), "products":ko.observableArray()};

            self.addEnable = ko.computed(function() {
                var flag = true, MAX_NUMBER = 99999999999, MAX_PRICE = 1000000;
                ko.utils.arrayForEach(self.packingList.products(), function(item) {
                    var count = item.quantity(),
                        price = item.price(),
                        fillFields = item.name() && count && item.unit() && item.storage() && price,
                        correctNumbers = 0 < count && count < MAX_NUMBER && 0 < price && price <= MAX_PRICE;

                    if(!(fillFields && correctNumbers)) {
                        flag = false;
                        return false;
                    }
                });
                return flag;
            }, this);

            self.createEnable = ko.computed(function() {
                return self.addEnable() && self.packingList.destinationWarehouse().name != self.label &&
                    self.packingList.departureWarehouse().name != self.label && self.packingList.products().length > 0;
            }, this);


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
                self.packingList.products.push(new models.Goods());
            };

            self.removeGoods = function(goods) {
                self.packingList.products.remove(goods);
            };

            self.choose = function() {
                var context = ko.contextFor($("#warehouses")[0]);
                if(! context.$data.getChosenWarehouse()){
                    return false;
                }
                switch(self.warehousePoint) {
                    case 'from' :
                        self.packingList.departureWarehouse(context.$data.getChosenWarehouse());
                        break;
                    case 'to' :
                        self.packingList.destinationWarehouse(context.$data.getChosenWarehouse());
                        break;
                    default: return false;
                }
                context.$data.checkedWarehouses([]);
                message.deleteMessage();
                $('#warehouses-popup').modal("hide");
            };

            self.create = function() {
                packingListService.save(
                    ko.toJSON(self.packingList),
                    function () {
                        navService.navigateTo("packingLists");
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

        return {viewModel: packingViewModel, template: template};
    });