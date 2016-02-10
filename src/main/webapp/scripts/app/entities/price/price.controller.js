'use strict';

angular.module('onlinerrealtpagesApp')
    .controller('PriceController', function ($scope, $state, Price, PriceSearch) {

        $scope.prices = [];
        $scope.loadAll = function() {
            Price.query(function(result) {
               $scope.prices = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            PriceSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.prices = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.price = {
                priceUsd: null,
                priceRuble: null,
                created: null,
                id: null
            };
        };
    });
