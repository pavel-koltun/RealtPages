'use strict';

describe('Controller Tests', function() {

    describe('Apartment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockApartment, MockLocation, MockPrice;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockApartment = jasmine.createSpy('MockApartment');
            MockLocation = jasmine.createSpy('MockLocation');
            MockPrice = jasmine.createSpy('MockPrice');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Apartment': MockApartment,
                'Location': MockLocation,
                'Price': MockPrice
            };
            createController = function() {
                $injector.get('$controller')("ApartmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'onlinerRealtPagesApp:apartmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
