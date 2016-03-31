(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .controller('PriceDeleteController',PriceDeleteController);

    PriceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Price'];

    function PriceDeleteController($uibModalInstance, entity, Price) {
        var vm = this;
        vm.price = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Price.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
