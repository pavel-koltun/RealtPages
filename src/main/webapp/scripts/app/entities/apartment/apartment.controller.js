'use strict';

angular.module('onlinerrealtpagesApp')
    .controller('ApartmentController', function ($scope, $state, Apartment, ApartmentSearch, ParseLinks) {

        $scope.apartments = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Apartment.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.apartments = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ApartmentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.apartments = result;
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
            $scope.apartment = {
                apartmentId: null,
                created: null,
                updated: null,
                url: null,
                id: null
            };
        };
    });
