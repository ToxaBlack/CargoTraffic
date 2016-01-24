define(['app/service/authService', 'app/service/navService', 'app/service/barService', "knockout", "text!./login.html"],
    function (authService, navService, bar, ko, loginTemplate) {
        "use strict";

        function loginViewModel() {
            bar.go(50);
            var self = this;
            self.user = ko.observable();
            self.password = ko.observable();
            self.error = ko.observable();
            self.login = function () {
                authService.login(self.user(), self.password(),
                    function (data) {
                        self.error("");
                        var context = ko.contextFor($("body")[0]);
                        context.$data.roles(data);
                        navService.navigateTo("account");
                    },
                    function (data) {
                        self.error("Invalid login or password");
                    })
            };

            bar.go(100);
            return self;
        }

        return {viewModel: loginViewModel, template: loginTemplate};
    });
