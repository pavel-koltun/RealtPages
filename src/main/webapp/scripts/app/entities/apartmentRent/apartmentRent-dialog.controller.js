'use strict';

angular.module('onlinerrealtpagesApp').controller('ApartmentRentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Apartment', 'ApartmentRent', 'Location', 'Price',
        function($scope, $stateParams, $uibModalInstance, entity, Apartment, ApartmentRent, Location, Price) {

        $scope.apartmentRent = entity;
        $scope.locations = Location.query();
        $scope.prices = Price.query();

        $scope.load = function(id) {
            ApartmentRent.get({id : id}, function(result) {
                $scope.apartmentRent = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('onlinerrealtpagesApp:apartmentRentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.apartmentRent.id != null) {
                ApartmentRent.update($scope.apartmentRent, onSaveSuccess, onSaveError);
            } else {
                ApartmentRent.save($scope.apartmentRent, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.datePickerForCreated = {};

        $scope.datePickerForCreated.status = {
             opened: false
        };

        $scope.datePickerForCreatedOpen = function($event) {
            $scope.datePickerForCreated.status.opened = true;
        };
        $scope.datePickerForUpdated = {};

        $scope.datePickerForUpdated.status = {
            opened: false
        };

        $scope.datePickerForUpdatedOpen = function($event) {
            $scope.datePickerForUpdated.status.opened = true;
        };
}]);
