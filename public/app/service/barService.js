define(["nanobar"],
    function (Bar) {
        "use strict";
        var options = {
            target: document.getElementById('bar')
        };

        return new Bar(options);
    });