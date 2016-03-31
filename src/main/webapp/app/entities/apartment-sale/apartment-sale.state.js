(function() {
    'use strict';

    angular
        .module('onlinerRealtPagesApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('apartment-sale', {
            parent: 'entity',
            url: '/apartment-sale?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'onlinerRealtPagesApp.apartmentSale.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/apartment-sale/apartment-sales.html',
                    controller: 'ApartmentSaleController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('apartmentSale');
                    $translatePartialLoader.addPart('sellerType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('apartment-sale-detail', {
            parent: 'entity',
            url: '/apartment-sale/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'onlinerRealtPagesApp.apartmentSale.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/apartment-sale/apartment-sale-detail.html',
                    controller: 'ApartmentSaleDetailController',
                    controllerAs: 'vm'
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
        .state('apartment-sale.new', {
            parent: 'apartment-sale',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apartment-sale/apartment-sale-dialog.html',
                    controller: 'ApartmentSaleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
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
                }).result.then(function() {
                    $state.go('apartment-sale', null, { reload: true });
                }, function() {
                    $state.go('apartment-sale');
                });
            }]
        })
        .state('apartment-sale.edit', {
            parent: 'apartment-sale',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apartment-sale/apartment-sale-dialog.html',
                    controller: 'ApartmentSaleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApartmentSale', function(ApartmentSale) {
                            return ApartmentSale.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('apartment-sale', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('apartment-sale.delete', {
            parent: 'apartment-sale',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apartment-sale/apartment-sale-delete-dialog.html',
                    controller: 'ApartmentSaleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApartmentSale', function(ApartmentSale) {
                            return ApartmentSale.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('apartment-sale', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
