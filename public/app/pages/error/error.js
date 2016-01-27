define(['app/utils/utils', "knockout", "text!./error.html"],
    function (utils, ko, errorTemplate) {
        "use strict";

        function errorViewModel() {
            var self = this;

            return self;
        }

        return {viewModel: errorViewModel, template: errorTemplate};
    });
