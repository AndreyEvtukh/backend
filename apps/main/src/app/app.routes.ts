import { Routes } from '@angular/router';
import { ICONS, IRoutesData, ROUTE_PATH } from "./models/app.model";
import { authRoutes } from "./auth/auth.routes";

export const routes: Routes = [
    {
        path: "",
        title: "Full-Stack | Home",
        data: <IRoutesData>{
            fragment: ROUTE_PATH.HOME,
            title: "Home",
            icon: ICONS.Home,
            showPageTitle: true,
        },
        children: [...authRoutes],
    },
    {
        title: "Full-Stack | About",
        path: "",
        redirectTo: ROUTE_PATH.ABOUT,
        pathMatch: 'full',
        data: <IRoutesData>{
            fragment: ROUTE_PATH.ABOUT,
            title: "About",
            icon: ICONS.About,
            showPageTitle: true,
        },
    },
    {
        path: '**',
        redirectTo: "/",
    },
];
