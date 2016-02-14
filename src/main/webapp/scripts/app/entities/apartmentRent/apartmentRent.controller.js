'use strict';

angular.module('onlinerrealtpagesApp')
    .controller('ApartmentRentController', function ($scope, $state, ApartmentRent, ApartmentRentSearch, ParseLinks) {

        $scope.apartmentRents = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            ApartmentRent.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.apartmentRents = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ApartmentRentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.apartmentRents = result;
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
            $scope.apartmentRent = {
                type: null,
                isOwner: false,
                id: null
            };
        };
    });
