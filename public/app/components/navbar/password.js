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
                    hideForm(self);
                    accountService.updatePassword(self.oldPassword(), self.newPassword(),
                        function (data) {
                            navService.mainPage();
                        },
                        function (data) {
                            switch (data.status) {
                                case 400:
                                    self.error(data.responseText);
                                    break;
                                default:
                                    navService.catchError(data);
                            }
                        });
                }
            };

            self.cancel = function() {
               hideForm(self);
            };
        }

        function hideForm(self) {
            $('#passwordModal').modal('hide');
            self.oldPassword(null);
            self.newPassword(null);
            self.confirmPassword(null);
            if (validator) validator.resetForm();
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
                    },
                    newPassword: {
                        pattern: "Password should contain only letters and digits"
                    }
                }
            });

            return validator.form();
        }

        return new PasswordViewModel();
    });