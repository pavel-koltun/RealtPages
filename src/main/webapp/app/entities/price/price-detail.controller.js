(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('PriceDetailController', PriceDetailController);

    PriceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Price', 'Apartment'];

    function PriceDetailController($scope, $rootScope, $stateParams, entity, Price, Apartment) {
        var vm = this;
        vm.price = entity;
        vm.load = function (id) {
            Price.get({id: id}, function(result) {
                vm.price = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinerRealtPagesApp:priceUpdate', function(event, result) {
            vm.price = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
