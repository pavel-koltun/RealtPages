(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('ApartmentSaleDetailController', ApartmentSaleDetailController);

    ApartmentSaleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ApartmentSale'];

    function ApartmentSaleDetailController($scope, $rootScope, $stateParams, entity, ApartmentSale) {
        var vm = this;
        vm.apartmentSale = entity;
        vm.load = function (id) {
            ApartmentSale.get({id: id}, function(result) {
                vm.apartmentSale = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinerRealtPagesApp:apartmentSaleUpdate', function(event, result) {
            vm.apartmentSale = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
