export const ROUTE_PATH = {
    AUTH: "auth",
    HOME: "home",
    ABOUT: "about",
    RESUME: "resume",
    PORTFOLIO: "portfolio",
    CONTACTS: "contacts",
    LOGIN: "auth/login",
    REGISTER: "auth/register",
};

export const NAV_LOCATION= {
    HEADER: "header",
    FOOTER: "footer",
    SIDE: "side",
};

export enum LANGUAGE {
    EN = "en",
    RU = "ru",
    PL = "pl",
}

export enum EVENT {
    SCROLL = "scroll",
    CLICK = "click",
}

export interface IRoutesData {
    fragment: string;
    title: string;
    showPageTitle?: boolean;
}

export interface MainPortfolioLink {
    id: number;
    title: string;
    content: string;
    link: string;
    icon: string;
}

export interface UIButton {
    id?: number | string;
    link: string;
    label: string;
    isDisabled?: boolean;
    labelClass?: string;
    borderClass?: string;
    bgClass?: string;
    roundedClass?: string;
    hoverClass?: string;
    icon?: string;
    iconPosition?: 'right' | 'left';
}

export interface Testimonial {
    id?: number | string;
    text: string;
    authorName: string;
    authorPosition: string;
    date: string;
}

export interface Lang {
    id: string;
    title: string;
    order: number;
    active?: boolean;
}

export const ICONS = {
    Design: "design",
    Frontend: "frontend",
    Backend: "backend",
    Angular: "angular",
    TypeScript: "ts",
    Java: "java",
    ArrowR: "arrow-right",
    Mice: "mice",
    Home: "home",
    About: "about",
    Login: "login",
    EYE_OFF: "eye-off",
    EYE_ON: "eye-on",
    DOWNLOAD: "download",
} as const;
