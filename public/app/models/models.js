define(['knockout'],
    function (ko) {
        "use strict";

        function Goods() {
            var self = this;
            self.name = ko.observable();
            self.quantity = ko.observable();
            self.unit = ko.observable();
            self.storage = ko.observable();
            self.price = ko.observable();
        }

        function Checkpoint(coordinates, address) {
            var self = this;
            self.coordinates = coordinates;
            self.address = address;
        }

        function CheckProduct(id,name,quantity,measure) {
            var self = this;
            self.id = id;
            self.name = name;
            self.quantity = quantity;
            self.measure = measure;
            self.realQuantity = ko.observable();
        }

        return {
            Goods: Goods,
            Checkpoint: Checkpoint,
            CheckProduct: CheckProduct
        };
    });
