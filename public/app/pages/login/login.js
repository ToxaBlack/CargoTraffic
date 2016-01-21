define(['app/service/authService', 'app/service/navService', "knockout", "text!./login.html"], function (authService, navService, ko, loginTemplate) {
    "use strict";

    function loginViewModel() {
        var self = this;
        self.user = ko.observable();
        self.password = ko.observable();
        self.error = ko.observable();
        self.login = function (root) {
            authService.login(self.user(), self.password(),
                function (data) {
                    self.error("");
                    root.roles(data);
                    navService.navigateTo("account");
                },
                function (data) {
                    self.error("Invalid login or password");
                })

        };
        return self;
    }

    return {viewModel: loginViewModel, template: loginTemplate};
});
