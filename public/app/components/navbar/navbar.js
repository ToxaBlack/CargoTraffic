define(['app/service/authService', 'app/service/navService', "knockout", "text!./navbar.html"],
    function (authService, navService, ko, navbarTemplate) {
        "use strict";

        function navbarViewModel() {
            var self = this;

            self.mainPage = function () {
                navService.mainPage();
            }

            self.logout = function () {
                authService.logout(
                    function () {
                        var context = ko.contextFor($("body")[0]);
                        context.$data.roles([]);
                        navService.navigateTo("login");
                    })
            };

            return self;
        }

        return {viewModel: navbarViewModel, template: navbarTemplate};
    });
