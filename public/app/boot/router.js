define(["jquery", "knockout", "crossroads", "history"], function ($, ko, crossroads) {
    "use strict";

    return new Router({
        routes: [
            {url: 'login', params: {page: 'login'}},
            {url: 'account', params: {page: 'account'}},
            {url: 'clients', params: {page: 'clients'}},
            {url: 'companies', params: {page: 'companies'}},
            {url: 'warehouses', params: {page: 'warehouses'}},
            {url: 'packingList', params: {page: 'packingList'}},
            {url: 'home', params: {page: 'home'}},
            {url: 'packingLists', params: {page: 'packingLists'}},
            {url: 'checkPackingList/{id}', params: {page: 'checkPackingList'}},
            {url: 'waybill/{id}', params: {page: 'waybill'}},
            {url: 'settings', params: {page: 'settings'}},
            {url: 'error', params: {page: 'error'}},
            {url: 'employees', params: {page: 'employees'}},
            {url: 'addEmployee', params: {page: 'addEmployee'}},
            {url: 'vehicles', params: {page: 'vehicles'}},
        ]
    });

    function Router(config) {
        var currentRoute = this.currentRoute = ko.observable({});

        ko.utils.arrayForEach(config.routes, function (route) {
            crossroads.addRoute(route.url, function (requestParams) {
                currentRoute(ko.utils.extend(requestParams, route.params));
            });
        });
        activateCrossroads();
        $("body").on("click", "a",
            function (e) {
                var title, urlPath;
                urlPath = $(this).attr("href");
                if (urlPath) {
                    if (urlPath.slice(0, 1) == "#") {
                        return true;
                    }
                    e.preventDefault();
                    title = $(this).text();
                    return History.pushState({
                        urlPath: urlPath
                    }, title, urlPath);
                }
            });
    }

    function activateCrossroads() {
        History.Adapter.bind(window, "statechange", routeCrossRoads);
        crossroads.normalizeFn = crossroads.NORM_AS_OBJECT;
        routeCrossRoads();
    }

    function routeCrossRoads() {
        var State = History.getState();
        $('.modal-backdrop').remove();

        if (State.data.urlPath) {
            return crossroads.parse(State.data.urlPath);
        }
        else {
            if (State.hash.length > 1) {
                var fullHash = State.hash;
                var quesPos = fullHash.indexOf('?');
                if (quesPos > 0) {
                    var hashPath = fullHash.slice(0, quesPos);
                    return crossroads.parse(hashPath);
                }
                else {
                    return crossroads.parse(fullHash);
                }
            }
            else {
                return crossroads.parse('/');
            }
        }
    }
});