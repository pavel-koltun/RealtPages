(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('ApartmentRentDeleteController',ApartmentRentDeleteController);

    ApartmentRentDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApartmentRent'];

    function ApartmentRentDeleteController($uibModalInstance, entity, ApartmentRent) {
        var vm = this;
        vm.apartmentRent = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ApartmentRent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
