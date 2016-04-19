define(['highcharts', 'jqueryUI', 'excel-builder', 'swfobject', 'downloadify', 'app/service/chartService', 'app/service/navService', 'app/service/barService', "knockout", 'jquery',"text!./charts.html"],

    function (highcharts, jqueryUI, EB, swfobject, downloader, chartService, navService, bar, ko, $, chartsTemplate) {
        "use strict";

        function chartViewModel() {

            bar.go(50);
            var self = this;
            var currentDate = new Date();
            self.minDate = ko.observable(new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate()));
            self.maxDate = ko.observable(new Date());
            var WEEK_IN_MILLISECONDS = 7 * 24 * 3600 * 1000;
            self.excelReport = function () {
                var url = "api/reports/excel?minDate=" + self.minDate().getTime() +
                    "&maxDate=" + self.maxDate().getTime();
                window.open(url, 'Report');
            };
            self.pdfReport = function () {
                var url = "api/reports/pdf?minDate=" + self.minDate().getTime() +
                    "&maxDate=" + self.maxDate().getTime();
                window.open(url, 'Report');
            };
            self.csvReport = function () {
                var url = "api/reports/csv?minDate=" + self.minDate().getTime() +
                    "&maxDate=" + self.maxDate().getTime();
                window.open(url, 'Report');
            };

            $(".date").each(function() {
                $(this).datepicker({ dateFormat: 'dd-mm-yy' });
            });


            ko.bindingHandlers.datepicker = {
                init: function(element, valueAccessor, allBindingsAccessor) {
                    var options = allBindingsAccessor().datepickerOptions || {},
                        $el = $(element);
                    $el.datepicker(options);

                    ko.utils.registerEventHandler(element, "change", function() {
                        var observable = valueAccessor();
                        observable($el.datepicker("getDate"));
                        self.updateChart();
                    });

                    ko.utils.domNodeDisposal.addDisposeCallback(element, function() {
                        $el.datepicker("destroy");
                    });

                },
                update: function(element, valueAccessor) {
                    var value = ko.utils.unwrapObservable(valueAccessor()),
                        $el = $(element);
                    var current = $el.datepicker("getDate");
                    if (value - current !== 0) {
                        $el.datepicker("setDate", value);
                    }
                }
            };


            ko.bindingHandlers.minDate = {
                update: function(element, valueAccessor) {
                    var value = ko.utils.unwrapObservable(valueAccessor()),
                        current = $(element).datepicker("option", "minDate", value);
                }
            };

            ko.bindingHandlers.maxDate = {
                update: function(element, valueAccessor) {
                    var value = ko.utils.unwrapObservable(valueAccessor()),
                        current = $(element).datepicker("option", "maxDate", value);
                }
            };


            var chartOptions = {
                chart: {
                    renderTo: 'container',
                    type: 'area'
                },
                title: {
                    text: 'Financial highlights'
                },
                legend: {
                    align: 'left',
                    verticalAlign: 'top',
                    x: 70,
                    floating: true,
                    borderWidth: 0
                },
                xAxis: {
                    tickmarkPlacement: 'on',
                    type: 'datetime',
                    dateTimeLabelFormats: {
                        week: '%e. %b'
                    }
                },
                yAxis: {
                    title: {
                        text: '$'
                    }
                },
                tooltip: {
                    shared: true,
                    valueSuffix: ' $'
                },
                plotOptions: {
                    area: {
                        marker: {
                            radius: 2
                        },
                        lineWidth: 1,
                        states: {
                            hover: {
                                radius: 5
                            }
                        }
                    },
                    areaspline: {
                        fillOpacity: 0.7
                    },
                    series: {
                        marker: {
                            lineWidth: 1
                        }
                    }
                },
                series: [{
                    name: 'Income',
                    data: [],
                    pointStart: Date.UTC(self.minDate().getFullYear(), self.minDate().getMonth(), self.minDate().getDate()),
                    pointInterval: WEEK_IN_MILLISECONDS
                }, {
                    name: 'Loss',
                    data: [],
                    pointStart: Date.UTC(self.minDate().getFullYear(), self.minDate().getMonth(), self.minDate().getDate()),
                    pointInterval: WEEK_IN_MILLISECONDS
                }, {
                    name: 'Profit',
                    data: [],
                    pointStart: Date.UTC(self.minDate().getFullYear(), self.minDate().getMonth(), self.minDate().getDate()),
                    pointInterval: WEEK_IN_MILLISECONDS
                }]
            };

            var chart = new Highcharts.Chart(chartOptions);


            function groupDataByWeek(data) {
                var initialDate = data[0].deliveredDate;
                var groupedData = [{
                    deliveredDate: data[data.length - 1].deliveredDate,
                    transportationIncome: 0,
                    loss: 0,
                    profit: 0
                }];
                var groupedDataIndex = 0;
                for (var i = 0; i < data.length; i++) {
                    if (data[i].deliveredDate > initialDate + WEEK_IN_MILLISECONDS) {
                        initialDate = data[i].deliveredDate;
                        groupedDataIndex++;
                        groupedData[groupedDataIndex] = {
                            deliveredDate: data[i].deliveredDate,
                            transportationIncome: 0,
                            loss: 0,
                            profit: 0
                        };
                    }
                    groupedData[groupedDataIndex].transportationIncome += data[i].transportationIncome;
                    groupedData[groupedDataIndex].loss += data[i].vehicleFuelLoss;
                    groupedData[groupedDataIndex].loss += data[i].productsLoss;
                    groupedData[groupedDataIndex].profit += data[i].profit;
                }
                return groupedData;
            }


            self.updateChart = function() {
                chartService.list(self.minDate().getTime(), self.maxDate().getTime(),
                    function (data) {
                        var groupedData = groupDataByWeek(data);
                        var income = [];
                        var loss = [];
                        var profit = [];
                        for (var i = 0; i < groupedData.length; i++) {
                            income[i] = groupedData[i].transportationIncome;
                            loss[i] = groupedData[i].loss;
                            profit[i] = groupedData[i].profit;
                        }
                        chart.series[0].setData(income, true);
                        chart.series[1].setData(loss, true);
                        chart.series[2].setData(profit, true);
                        var minDate = new Date(groupedData[0].deliveredDate);
                        var maxDate = new Date(groupedData[groupedData.length - 1].deliveredDate);
                        if (minDate.getTime() < self.minDate().getTime())
                            self.minDate(minDate);
                        if (maxDate.getTime() > self.maxDate().getTime())
                            self.maxDate(maxDate);
                        chart.xAxis[0].setExtremes(new Date(self.minDate().getFullYear(), self.minDate().getMonth(), self.minDate().getDate() - 3),
                            new Date(self.maxDate().getFullYear(), self.maxDate().getMonth(), self.maxDate().getDate() + 3));
                    },
                    function (data) {
                        navService.catchError(data);
                    },
                    function () {
                        bar.go(100);
                    }
                );
            };


            /*function prepareDataForXLSX() {

            }


            function prepareXLSX() {
                var artistWorkbook = EB.createWorkbook();
                var albumList = artistWorkbook.createWorksheet({name: 'Financial Report'});
                var stylesheet = artistWorkbook.getStyleSheet();

                var red = 'FFFF0000';
                var importantFormatter = stylesheet.createFormat({
                    font: {
                        bold: true,
                        color: red
                    },
                    border: {
                        bottom: {color: red, style: 'thin'},
                        top: {color: red, style: 'thin'},
                        left: {color: red, style: 'thin'},
                        right: {color: red, style: 'thin'}
                    }
                });

                var themeColor = stylesheet.createFormat({
                    font: {
                        bold: true,
                        color: {theme: 3}
                    }
                });

                var originalData = [
                    [
                        {value: 'Start date', metadata: {style: themeColor.id}},
                        {value: 'Final date', metadata: {style: themeColor.id}},
                        {value: 'Loss', metadata: {style: themeColor.id}},
                        {value: 'Income', metadata: {style: themeColor.id}},
                        {value: 'Profit', metadata: {style: themeColor.id}}
                    ],
                    ['01.01.2010', '07.01.2010', 8.99, 12.3, -1.4],
                    ['08.01.2010', '14.01.2010', 13.99, 34.5, 45.6],
                    ['15.01.2010', '21.01.2010', 11.34, 23.4, 34.5],
                    ['22.01.2010', '28.01.2010', 10.54, 12.3, -23.4],
                    ['29.01.2010', '05.02.2010', 10.64, 34.6, 23.5],
                    ['06.02.2010', '12.02.2010', 8.99, 45.6, 34.6],
                    ['01.01.2010', '12.02.2010', 348.99, 456.6, 345.6]
                ];

                albumList.setData(originalData);
                albumList.setColumns([
                    {width: 30},
                    {width: 20},
                    {width: 10}
                ]);

                artistWorkbook.addWorksheet(albumList);

                var data = EB.createFile(artistWorkbook);

                Downloadify.create('downloader', {
                    filename: 'Financial Report.xlsx',
                    data: data,
                    dataType: 'base64',
                    onComplete: function () {
                        alert('Your File Has Been Saved!');
                    },
                    onCancel: function () {
                        alert('You have cancelled the saving of this file.');
                    },
                    onError: function () {
                        alert('You must put something in the File Contents or there will be nothing to save!');
                    },
                    swf: 'assets/lib/downloadify/downloadify.swf',
                    downloadImage: 'assets/lib/downloadify/images/download.png',
                    width: 100,
                    height: 30,
                    transparent: true,
                    append: false
                });
            }*/

            self.updateChart();
            //prepareXLSX();

            bar.go(100);
            return self;
        }

        return {viewModel: chartViewModel, template: chartsTemplate};
    });