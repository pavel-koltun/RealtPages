(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('ApartmentSaleDeleteController',ApartmentSaleDeleteController);

    ApartmentSaleDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApartmentSale'];

    function ApartmentSaleDeleteController($uibModalInstance, entity, ApartmentSale) {
        var vm = this;
        vm.apartmentSale = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ApartmentSale.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
