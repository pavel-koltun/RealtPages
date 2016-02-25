'use strict';

angular.module('onlinerrealtpagesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('apartmentSale', {
                parent: 'entity',
                url: '/apartmentSales',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'onlinerrealtpagesApp.apartmentSale.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/apartmentSale/apartmentSales.html',
                        controller: 'ApartmentSaleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('apartmentSale');
                        $translatePartialLoader.addPart('sellerType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('apartmentSale.detail', {
                parent: 'entity',
                url: '/apartmentSale/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'onlinerrealtpagesApp.apartmentSale.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/apartmentSale/apartmentSale-detail.html',
                        controller: 'ApartmentSaleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('apartmentSale');
                        $translatePartialLoader.addPart('sellerType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ApartmentSale', function($stateParams, ApartmentSale) {
                        return ApartmentSale.get({id : $stateParams.id});
                    }]
                }
            })
            .state('apartmentSale.new', {
                parent: 'apartmentSale',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/apartmentSale/apartmentSale-dialog.html',
                        controller: 'ApartmentSaleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    resale: false,
                                    rooms: null,
                                    floor: null,
                                    floors: null,
                                    areaTotal: null,
                                    areaLiving: null,
                                    areaKitchen: null,
                                    seller: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('apartmentSale', null, { reload: true });
                    }, function() {
                        $state.go('apartmentSale');
                    })
                }]
            })
            .state('apartmentSale.edit', {
                parent: 'apartmentSale',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/apartmentSale/apartmentSale-dialog.html',
                        controller: 'ApartmentSaleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ApartmentSale', function(ApartmentSale) {
                                return ApartmentSale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('apartmentSale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('apartmentSale.delete', {
                parent: 'apartmentSale',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/apartmentSale/apartmentSale-delete-dialog.html',
                        controller: 'ApartmentSaleDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ApartmentSale', function(ApartmentSale) {
                                return ApartmentSale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('apartmentSale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
