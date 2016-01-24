define(['app/service/startupService', 'app/service/navService', 'knockout', 'router', 'bootstrap'], function (startupService, navService, ko, router) {
    "use strict";

    ko.components.register('navbar', {require: 'app/components/navbar/navbar'});

    ko.components.register('login', {require: 'app/pages/login/login'});
    ko.components.register('account', {require: 'app/pages/account/account'});
    ko.components.register('companies', {require: 'app/pages/companies/companies'});
    ko.components.register('home', {require: 'app/pages/home/home'});
    ko.components.register('error', {require: 'app/pages/error/error'});
    ko.components.register('password', {require: 'app/pages/password/password'});


    var roles = ko.observableArray([]);


    startupService.roles(
        function (data) {
            roles(data);
            if (window.location.pathname === "/")
                navService.navigateTo("account");

        }, function () {
            if (window.location.pathname === "/")
                navService.navigateTo("login");
        }, function () {
            ko.applyBindings({
                route: router.currentRoute,
                roles: roles
            });
        });


});