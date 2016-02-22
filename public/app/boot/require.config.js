var require = {
    baseUrl: "/assets/",
    paths: {
        "bootstrap": "lib/bootstrap.min",
        "crossroads": "lib/navigation/crossroads.min",
        "signals": "lib/navigation/signals.min",
        "history": "lib/navigation/native.history",
        "router": "app/boot/router",
        "jquery": "lib/jquery.min",
        "validation": "lib/jquery.validate.min",
        "knockout": "lib/knockout",
        "text": "lib/text",
        "nanobar": "lib/nanobar.min",
        "highcharts": "lib/highcharts",
        "jqueryUI": "lib/jquery-ui.min",
        "underscore": "lib/underscore.min",
        "JSZip": "lib/jszip.min",
        "require": "lib/require.min",
        "swfobject": "lib/downloadify/swfobject",
        "downloadify": "lib/downloadify/downloadify.min",
        "excel-builder": "lib/excel-builder",
        "image": "lib/image"
    },
    shim: {
        "bootstrap": {
            deps: ["jquery"]
        }
    }
};