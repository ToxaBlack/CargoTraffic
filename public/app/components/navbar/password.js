define(['app/service/accountService', 'app/service/navService', "knockout"],
    function (accountService, navService, ko) {
        "use strict";


        var validator;

        function PasswordViewModel() {
            var self = this;
            self.oldPassword = ko.observable();
            self.newPassword = ko.observable();
            self.confirmPassword = ko.observable();
            self.error = ko.observable();

            self.updatePassword = function () {
                if (validate()) {
                    $('#passwordModal').modal('hide');
                    validator.resetForm();
                    accountService.updatePassword(self.oldPassword(), self.newPassword(),
                        function (data) {
                            navService.mainPage();
                        },
                        function (data) {
                            switch (data.status) {
                                case 400:
                                    self.error(data.responseText);
                                    break;
                                case 403:
                                    navService.navigateTo("login");
                                    break;
                                default:
                                    navService.navigateTo("error");
                            }
                        });
                }
            };

            self.cancel = function() {
                $('#passwordModal').modal('hide');
                if (validator) validator.resetForm();
            };
        }

        function validate() {
            validator = $('#passwordForm').validate({
                rules: {
                    confirmPassword: {
                        equalTo: '#new-password'
                    }
                },
                messages: {
                    confirmPassword: {
                        equalTo: "Please enter same password"
                    }
                }
            });

            return validator.form();
        }

        return new PasswordViewModel();
    });