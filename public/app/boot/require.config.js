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
        "nanobar": "lib/nanobar.min"
    },
    shim: {
        "bootstrap": {
            deps: ["jquery"]
        }
    }
};