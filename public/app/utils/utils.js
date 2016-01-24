define(['jquery'],
    function ($) {
        "use strict";

        function send(url, method, data, doneFunk, failFunk, alwaysFunk) {
            $.ajax({
                url: url,
                method: method,
                data: data,
                contentType: 'application/json'
            }).done(doneFunk)
                .fail(failFunk)
                .always(alwaysFunk);
        }

        function goTo(page) {
            var link = document.createElement('a');
            link.href = page;
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);
        }

        return {
            send: send,
            goTo: goTo
        };
    });
