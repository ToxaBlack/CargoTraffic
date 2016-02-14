define(['app/service/startupService', 'app/service/navService', 'knockout', 'router', 'bootstrap', 'validation'],
    function (startupService, navService, ko, router) {
        "use strict";

        ko.components.register('navbar', {require: 'app/components/navbar/navbar'});
        ko.components.register('warehouses', {require: 'app/components/warehouses/warehouses'});

        ko.components.register('login', {require: 'app/pages/login/login'});
        ko.components.register('account', {require: 'app/pages/account/account'});
        ko.components.register('clients', {require: 'app/pages/clients/clients'});
        ko.components.register('companies', {require: 'app/pages/companies/companies'});
        ko.components.register('packingList', {require: 'app/pages/packingList/packingList'});
        ko.components.register('packingLists', {require: 'app/pages/packingLists/packingLists'});
        ko.components.register('checkPackingList', {require: 'app/pages/checkPackingList/checkPackingList'});
        ko.components.register('waybill', {require: 'app/pages/waybill/waybill'});
        ko.components.register('home', {require: 'app/pages/home/home'});
        ko.components.register('error', {require: 'app/pages/error/error'});
        ko.components.register('employees', {require: 'app/pages/employees/employees'});
        ko.components.register('addEmployee', {require: 'app/pages/addEmployee/addEmployee'});
        ko.components.register('vehicles', {require: 'app/pages/vehicles/vehicles'});

        var roles = ko.observableArray([]);

        startupService.init();

        startupService.roles(
            function (data) {
                roles(data);
            }, function () {

            }, function () {
                if (window.location.pathname === "/")
                    navService.mainPage(roles());
                ko.applyBindings({
                    route: router.currentRoute,
                    roles: roles
                });
            });

    });