(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('ApartmentDeleteController',ApartmentDeleteController);

    ApartmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Apartment'];

    function ApartmentDeleteController($uibModalInstance, entity, Apartment) {
        var vm = this;
        vm.apartment = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Apartment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
