import {RenderMode, ServerRoute} from '@angular/ssr';
import {ROUTE_PATH} from "./models/app.model";

export const serverRoutes: ServerRoute[] = [
    {
        path: '',
        renderMode: RenderMode.Prerender, // или Server / Client — в зависимости от вашей стратегии
    },
    {
        path: '**',
        renderMode: RenderMode.Server,
    },
];

// export const serverRoutes: ServerRoute[] = [
//     {
//         path: "",
//         renderMode: RenderMode.Server
//     },
//     {
//         path: "**",
//         renderMode: RenderMode.Server,
//         status: 301,
//     },
//     {
//         path: ROUTE_PATH.HOME,
//         renderMode: RenderMode.Prerender
//     },
//     {
//         path: ROUTE_PATH.ABOUT,
//         renderMode: RenderMode.Prerender
//     },
//     {
//         path: ROUTE_PATH.RESUME,
//         renderMode: RenderMode.Prerender,
//     },
//     {
//         path: ROUTE_PATH.PORTFOLIO,
//         renderMode: RenderMode.Prerender,
//     },
//     {
//         path: ROUTE_PATH.CONTACTS,
//         renderMode: RenderMode.Prerender,
//     },
// ];
