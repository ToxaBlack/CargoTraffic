define(["app/utils/utils", "jquery"],
    function (utils, $) {
        "use strict";
        function StartupService() {

            var roles = function (done, error, always) {
                utils.send(
                    "api/roles",
                    "GET",
                    {},
                    done,
                    error,
                    always
                );
            };

            function init() {
                $.validator.addMethod(
                    "customDate",
                    function(value, element) {
                        return isBirthdayValid(value);
                    },
                    "Please enter date of birth yyyy-mm-dd."
                );
                $.validator.addMethod(
                    "arrivalDateValidation",
                    function(value, element) {
                        return isArrivalDateValid(value);
                    },
                    "Please enter date using pattern yyyy-mm-dd. Date must be after today."
                );
                $.validator.addMethod( "pattern", function( value, element, param ) {
                    if ( this.optional( element ) ) {
                        return true;
                    }
                    if ( typeof param === "string" ) {
                        param = new RegExp( "^(?:" + param + ")$" );
                    }
                    return param.test( value );
                }, "Invalid format." );
                $.ajaxSetup({
                   beforeSend: function(xhr, options) {
                       options.url = window.location.origin + "/" +options.url
                   }
                });
            }


            function isBirthdayValid(dateString) {
                if (dateString === "") return true;
                if (!/^\d{4}-\d{1,2}-\d{1,2}$/.test(dateString))
                    return false;

                var parts = dateString.split("-");
                var day = parseInt(parts[2], 10);
                var month = parseInt(parts[1], 10);
                var year = parseInt(parts[0], 10);

                if (year < 1940 || year > 2010 || month == 0 || month > 12)
                    return false;

                var monthLength = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

                if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
                    monthLength[1] = 29;

                return day > 0 && day <= monthLength[month - 1];
            }

            function isArrivalDateValid(dateString){
                if (dateString === "") return true;
                if (!/^\d{4}-\d{1,2}-\d{1,2}$/.test(dateString))
                    return false;

                var parts = dateString.split("-");
                var year = parseInt(parts[0], 10);
                var month = parseInt(parts[1], 10);
                var day = parseInt(parts[2], 10);

                var now = new Date();
                var newDate = new Date(year,month-1,day+1);
                if (newDate < now || year > 2100 || month == 0 || month > 12)
                    return false;

                var monthLength = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

                if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
                    monthLength[1] = 29;

                return day > 0 && day <= monthLength[month - 1];
            }

            return {
                roles: roles,
                init:init
            }
        }

        return new StartupService();
    });