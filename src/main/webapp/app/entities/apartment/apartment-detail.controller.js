(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('ApartmentDetailController', ApartmentDetailController);

    ApartmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Apartment', 'Location', 'Price'];

    function ApartmentDetailController($scope, $rootScope, $stateParams, entity, Apartment, Location, Price) {
        var vm = this;
        vm.apartment = entity;
        vm.load = function (id) {
            Apartment.get({id: id}, function(result) {
                vm.apartment = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinerRealtPagesApp:apartmentUpdate', function(event, result) {
            vm.apartment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
