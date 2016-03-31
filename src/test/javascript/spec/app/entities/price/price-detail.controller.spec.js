'use strict';

describe('Controller Tests', function() {

    describe('Price Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPrice, MockApartment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPrice = jasmine.createSpy('MockPrice');
            MockApartment = jasmine.createSpy('MockApartment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Price': MockPrice,
                'Apartment': MockApartment
            };
            createController = function() {
                $injector.get('$controller')("PriceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'onlinerRealtPagesApp:priceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
