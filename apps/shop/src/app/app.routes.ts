import { Route } from '@angular/router';

export const appRoutes: Route[] = [
  {
    path: '',
    redirectTo: 'products',
    pathMatch: 'full',
  },
  {
    path: 'products',
    loadChildren: () =>
      import('@org/shop/feature-products').then(
          m => {
              console.error(555)
              return m.featureProductsRoutes
          }),
  },
  {
    path: 'products',
    loadChildren: () =>
      import('@org/shop/feature-product-detail').then(
        m => {
          console.error(111)
          return m.featureProductDetailRoutes
        }
      ),
  },
  {
    path: '**',
    redirectTo: 'products',
  },
];
