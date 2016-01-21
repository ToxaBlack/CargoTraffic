define(['app/service/authService', 'app/service/navService', "knockout", "text!./navbar.html"], function (authService, navService, ko, navbarTemplate) {
    "use strict";

    function navbarViewModel() {
        var self = this;

        self.logout = function (root) {
            authService.logout(
                function (data) {
                    root.roles([]);
                    navService.navigateTo("login");
                })
        };

        return self;
    }

    return {viewModel: navbarViewModel, template: navbarTemplate};
});
