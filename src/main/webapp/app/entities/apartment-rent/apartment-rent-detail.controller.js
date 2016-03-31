(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('ApartmentRentDetailController', ApartmentRentDetailController);

    ApartmentRentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ApartmentRent'];

    function ApartmentRentDetailController($scope, $rootScope, $stateParams, entity, ApartmentRent) {
        var vm = this;
        vm.apartmentRent = entity;
        vm.load = function (id) {
            ApartmentRent.get({id: id}, function(result) {
                vm.apartmentRent = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinerRealtPagesApp:apartmentRentUpdate', function(event, result) {
            vm.apartmentRent = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
