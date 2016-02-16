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

        return {
            Goods: Goods,
            Checkpoint: Checkpoint
        };
    });
