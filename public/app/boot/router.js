define(["jquery", "knockout", "crossroads", "history"], function ($, ko, crossroads) {
    "use strict";

    return new Router({
        routes: [
            {url: 'login', params: {page: 'login'}},
            {url: 'account', params: {page: 'account'}},
            {url: 'clients', params: {page: 'clients'}},
            {url: 'companies', params: {page: 'companies'}},
            {url: 'warehouses', params: {page: 'warehouses'}},
            {url: 'home', params: {page: 'home'}},
            {url: 'settings', params: {page: 'settings'}},
            {url: 'error', params: {page: 'error'}},
            {url: 'password', params: {page: 'password'}},
            {url: 'employees', params: {page: 'employees'}},
            {url: 'addClient', params: {page: 'addClient'}}
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
                if (urlPath !== undefined) {
                    if (urlPath.slice(0, 1) == "#") {
                        return true;
                    }
                    e.preventDefault();
                    title = $(document).find("title").text();
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