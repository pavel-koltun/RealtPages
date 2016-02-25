'use strict';

angular.module('onlinerrealtpagesApp')
    .controller('ApartmentSaleController', function ($scope, $state, ApartmentSale, ApartmentSaleSearch, ParseLinks) {

        $scope.apartmentSales = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            ApartmentSale.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.apartmentSales = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ApartmentSaleSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.apartmentSales = result;
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
            $scope.apartmentSale = {
                resale: false,
                rooms: null,
                floor: null,
                floors: null,
                areaTotal: null,
                areaLiving: null,
                areaKitchen: null,
                seller: null,
                id: null
            };
        };
    });
